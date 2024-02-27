package org.jeecg.modules.txal.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.modules.txal.entity.TxalAnli;
import org.jeecg.modules.txal.entity.TxalDoclink;
import org.jeecg.modules.txal.entity.TxalTxjob;
import org.jeecg.modules.txal.entity.TxalTxjobd;
import org.jeecg.modules.txal.mapper.TxalTxjobMapper;
import org.jeecg.modules.txal.service.ITxalDoclinkService;
import org.jeecg.modules.txal.service.ITxalTxjobService;
import org.jeecg.modules.txal.service.ITxalTxjobdService;
import org.jeecg.modules.txal.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 体系作业
 * @Author: jeecg-boot
 * @Date: 2023-07-04
 * @Version: V1.0
 */
@Service
public class TxalTxjobServiceImpl extends ServiceImpl<TxalTxjobMapper, TxalTxjob> implements ITxalTxjobService {

    @Value("${msgtu.anlik.ziptmpdir:E:\\project\\99.其他资料\\09.du\\体系文件及案例库管理\\tmp}")
    private String tmpdir;

    @Autowired
    ITxalTxjobdService dservice;

    @Autowired
    ITxalDoclinkService doclinkService;

    @Autowired
    ISysBaseAPI sapi;

    @Override
    public boolean save(TxalTxjob job, MultipartFile file) throws IOException {
        if(recordExists(job)){
            return false;
        }
        String uuid = UUID.fastUUID().toString().replaceAll("-", "");
        String path = tmpdir + File.separator + uuid;
        Files.createDirectories(Paths.get(path));
        String fname = path + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        File f = new File(fname);
        file.transferTo(f);
        FileUtils.unzipFile(f, path);
        f.delete();
        baseMapper.insert(job);
        try (Stream<Path> sp = Files.find(Paths.get(path), 4, (fp, fileattr) -> {
            String fn = fp.getFileName().toString();
            if (fn.startsWith(job.getFcode()) && (fn.toLowerCase().endsWith(".docx") || fn.toLowerCase().endsWith(
                    ".doc"))) {
                return true;
            }
            return false;
        })) {
            Optional<Path> op = sp.findAny();
            if (op.isPresent()) {
                Path docpath = op.get();
                TxalDoclink link = new TxalDoclink();
                link.setLinkTable("txal_txjob");
                link.setLinkId(job.getId());
                link.setCode("jobdoc");
                link.setName(docpath.getFileName().toString());
                try (InputStream dis = Files.newInputStream(docpath)) {
                    doclinkService.upload(dis, link);
                }
            }
        }
        try (Stream<Path> sp = Files.find(Paths.get(path), 4, (fp, fileattr) -> {
            String fn = fp.getFileName().toString();
            if (fn.startsWith(job.getFcode()) && (fn.toLowerCase().endsWith("导入总表.xlsx") || fn.toLowerCase().endsWith(
                    "导入总表.xls"))) {
                return true;
            }
            return false;
        })) {
            Optional<Path> op = sp.findAny();
            if (op.isPresent()) {
                Path xlsxPath = op.get();
                CompletableFuture.runAsync(() -> {
                    try {
                        importDataFromExcel(xlsxPath, job.getId());
                        FileUtil.del(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        ;
//        Files.find()
        return true;
    }

    private void importDataFromExcel(Path xlsxPath, String fid) throws IOException {
        List<SysDepartModel> dps = sapi.getAllSysDepart();
        try (XSSFWorkbook workbook = new XSSFWorkbook(Files.newInputStream(xlsxPath))) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
            for (int i = 1; i < rowCount; i++) {//不读标题行
                Row row = sheet.getRow(i);
                //从0开始 列2 步骤 6.1  列3 主题 列4 作业内容 列5 主导部门 列6 关联部门
                String code = row.getCell(2).toString();
                TxalTxjobd dobj = new TxalTxjobd();
                dobj.setFid(fid);
                dobj.setCode(code);
                dobj.setTitle(row.getCell(3).toString());
                dobj.setContext(row.getCell(4).toString());
                String ogs = row.getCell(5).toString();
                if (StringUtils.hasText(ogs)) {
                    ogs =
                            Arrays.stream(ogs.split("/")).map(o -> {
                                Optional<SysDepartModel> ots =
                                        dps.stream().filter(d -> o.equals(d.getDepartName())).findFirst();
                                return ots.isPresent() ? ots.get().getOrgCode() : o;
                            }).collect(Collectors.joining(","));
                    dobj.setMaOrgCode(ogs);
                }
                ogs = row.getCell(6).toString();
                if (StringUtils.hasText(ogs)) {
                    ogs =
                            Arrays.stream(ogs.split("/")).map(o -> {
                                Optional<SysDepartModel> ots =
                                        dps.stream().filter(d -> o.equals(d.getDepartName())).findFirst();
                                return ots.isPresent() ? ots.get().getOrgCode() : o;
                            }).collect(Collectors.joining(","));
                    dobj.setLkOrgCode(ogs);
                }
                dservice.save(dobj);
                try (Stream<Path> sp = Files.find(xlsxPath.getParent(), 2,
                        (cp, bfa) -> cp.getFileName().toString().startsWith(code))) {
                    sp.forEach(spo -> {
                        String filename = spo.getFileName().toString();
                        TxalDoclink link = new TxalDoclink();
                        link.setLinkTable("txal_txjobd");
                        link.setLinkId(dobj.getId());
                        if (filename.contains("输入内容")) {
                            link.setCode("in");
                        } else {
                            link.setCode("out");
                        }
                        link.setName(filename);
                        try (InputStream dis = Files.newInputStream(spo)) {
                            doclinkService.upload(dis, link);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

            }
        }
    }


    private boolean recordExists(TxalTxjob job){
        return baseMapper.exists(new QueryWrapper<TxalTxjob>(){{
            and(i->i.eq("fname",job.getFname()));
            and(i->i.eq("fcode",job.getFcode()));
            and(i->i.eq("fver",job.getFver()));
            if(StringUtils.hasText(job.getId())){
                and(i->i.ne("id",job.getId()));
            }
        }
        });
    }

    @Override
    public boolean updateById(TxalTxjob entity) {
        if(recordExists(entity)){
            return false;
        }
        return super.updateById(entity);
    }

    @Override
    public boolean updateById(TxalTxjob entity, TxalDoclink[] doclink, MultipartFile[] files) {
        return false;
    }

    @Override
    public List<String> queryPnameList() {
        return baseMapper.queryPnameList();
    }

    @Override
    public boolean removeById(Serializable id) {
        doclinkService.removeByMap(new HashMap<String, Object>() {{
            put("link_table", "txal_txjob");
            put("link_id", id);
        }});
        List<TxalTxjobd> clst= dservice.listByMap(new HashMap<String,Object>(){{
            put("fid",id);
        }});
        if(clst!=null && clst.size()>0){
            dservice.removeBatchByIds(clst.stream().map(TxalTxjobd::getId).collect(Collectors.toList()));
        }
        return super.removeById(id);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        list.forEach(o -> {
            doclinkService.removeByMap(new HashMap<String, Object>() {{
                put("link_table", "txal_txjob");
                put("link_id", o);
            }});
           List<TxalTxjobd> clst= dservice.listByMap(new HashMap<String,Object>(){{
                put("fid",o);
            }});
            if(clst!=null && clst.size()>0){
                dservice.removeBatchByIds(clst.stream().map(TxalTxjobd::getId).collect(Collectors.toList()));
            }
        });
        return super.removeBatchByIds(list);
    }
}
