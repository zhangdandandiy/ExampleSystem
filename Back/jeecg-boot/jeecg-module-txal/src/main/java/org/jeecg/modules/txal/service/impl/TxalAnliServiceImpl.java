package org.jeecg.modules.txal.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.jeecg.modules.txal.entity.TxalAnli;
import org.jeecg.modules.txal.entity.TxalDoclink;
import org.jeecg.modules.txal.mapper.TxalAnliMapper;
import org.jeecg.modules.txal.service.ITxalAnliService;
import org.jeecg.modules.txal.service.ITxalDoclinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 案例
 * @Author: jeecg-boot
 * @Date: 2023-06-09
 * @Version: V1.0
 */
@Service
public class TxalAnliServiceImpl extends ServiceImpl<TxalAnliMapper, TxalAnli> implements ITxalAnliService {

    @Autowired
    ITxalDoclinkService doclinkService;

    @Override
    public String newAnliCode(String pTTCode, String ttCode) {
        String code = baseMapper.newAnliCode(pTTCode, ttCode);
        if (StringUtils.hasText(code)) {
            StringBuilder id =
                    new StringBuilder(String.valueOf(Long.parseLong(code.substring(code.lastIndexOf("-") + 1)) + 1));
            while (id.length() < 3) {
                id.insert(0, "0");
            }
            return String.format("%s-%s-%s", pTTCode, ttCode, id);
        } else {
            return String.format("%s-%s-001", pTTCode, ttCode);
        }

    }

    @Override
    public boolean save(TxalAnli anli, TxalDoclink[] doclink, MultipartFile[] files) {
        if(baseMapper.exists(new QueryWrapper<TxalAnli>(){{
            and(i->i.eq("name",anli.getName()));
        }
        })){
            return false;
        }
        String code = newAnliCode(anli.getTechtypePid(), anli.getTechtypeId());
        anli.setCode(code);
        int result = baseMapper.insert(anli);
        String id = anli.getId();
        StringBuilder sb=new StringBuilder();
        if (doclink != null || doclink.length > 0) {
            for (int i = 0; i < doclink.length; i++) {
                TxalDoclink link = doclink[i];
                link.setLinkTable("txal_anli");
                link.setLinkId(id);
                StringBuilder idx = new StringBuilder(String.valueOf(i + 1));
                while (idx.length() < 3) {
                    idx.insert(0, "0");
                }
                link.setCode(code + "-" + idx);
                sb.append(link.getCode()+" "+link.getName());
                sb.append("\r\n");
                try {
                    doclinkService.upload(files[i].getInputStream(), link);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        anli.setLinktext(sb.toString().trim());
        baseMapper.updateById(anli);
        return SqlHelper.retBool(result);
    }

    @Override
    public boolean updateById(TxalAnli entity, TxalDoclink[] doclink, MultipartFile[] files) {
        String code = entity.getCode();
        String id = entity.getId();
        if(StringUtils.hasText(entity.getName()) && baseMapper.exists(new QueryWrapper<TxalAnli>(){{
            and(i->i.eq("name",entity.getName()));
            and(i->i.ne("id",id));
        }
        })){
            return false;
        }
        StringBuilder sb=new StringBuilder();
        if (doclink != null || doclink.length > 0) {
            List<TxalDoclink> links = doclinkService.listByMap(new HashMap<String, Object>() {{
                put("link_table", "txal_anli");
                put("link_id", id);
            }});
            List<String> ids = null;
            if (links != null && !links.isEmpty()) {
                ids = links.stream().map(TxalDoclink::getId).collect(Collectors.toList());
                List<String> removeIds =
                        ids.stream().filter(o -> Arrays.stream(doclink).noneMatch(d -> d.getId().equals(o))).collect(Collectors.toList());
                if (!removeIds.isEmpty()) {
                    doclinkService.removeBatchByIds(removeIds);
                }
            }
            int i = 0;
            int index = 0;
            for (TxalDoclink link : doclink) {
                index++;
                link.setLinkTable("txal_anli");
                link.setLinkId(id);
                StringBuilder idx = new StringBuilder(String.valueOf(index));
                while (idx.length() < 3) {
                    idx.insert(0, "0");
                }
                link.setCode(code + "-" + idx);
                try {
                    if (ids != null && !ids.isEmpty() && ids.contains(link.getId())) {
                        doclinkService.updateById(link);
                    } else {
                        doclinkService.upload(files[i++].getInputStream(), link);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                sb.append(link.getCode()+" "+link.getName());
                sb.append("\r\n");
            }

        } else {
            //删除附件
            doclinkService.removeByMap(new HashMap<String, Object>() {{
                put("link_table", "txal_anli");
                put("link_id", id);
            }});
        }
        entity.setLinktext(sb.toString().trim());
        int result = baseMapper.updateById(entity);
        return SqlHelper.retBool(result);
    }

    @Override
    public boolean nameExists(String name, String id) {
        return baseMapper.exists(new QueryWrapper<TxalAnli>(){{
            and(i->i.eq("name",name));
            if(StringUtils.hasText(id)){
                and(i->i.ne("id",id));
            }
        }
        });
    }

    @Override
    public boolean removeById(Serializable id) {
        doclinkService.removeByMap(new HashMap<String, Object>() {{
            put("link_table", "txal_anli");
            put("link_id", id);
        }});
        return super.removeById(id);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        list.forEach(o -> {
            doclinkService.removeByMap(new HashMap<String, Object>() {{
                put("link_table", "txal_anli");
                put("link_id", o);
            }});
        });

        return super.removeBatchByIds(list);
    }

}
