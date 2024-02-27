package org.jeecg.modules.txal.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import org.jeecg.modules.txal.entity.TxalDoclink;
import org.jeecg.modules.txal.mapper.TxalDoclinkMapper;
import org.jeecg.modules.txal.service.ITxalDoclinkService;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 附件表
 * @Author: jeecg-boot
 * @Date: 2023-06-10
 * @Version: V1.0
 */
@Service
public class TxalDoclinkServiceImpl extends ServiceImpl<TxalDoclinkMapper, TxalDoclink> implements ITxalDoclinkService {

    @Value("${msgtu.anlik.path:/data/path}")
    String savePath;

    @Value("${msgtu.libreoffice.path:D:\\LibreOffice\\}")
    String librOfficePath;

    List<String> canCovertExt = Arrays.asList("ppt", "pptx", "doc", "docx", "xlsx", "xls", "txt", "png", "jpg", "jpeg");

    List<String> pngExt = Arrays.asList("png", "jpg", "jpeg");


    @Override
    public boolean upload(InputStream is, TxalDoclink extra) throws IOException {

        String id = UUID.fastUUID().toString().replaceAll("-", "");
        String separator = File.separator;
        String fullPath = String.format("%s%s%s%s%s", savePath, separator, id.substring(0, 2), separator,
                id.substring(2, 4));
        String ext = StringUtils.getFilenameExtension(extra.getName());
        String savename = id;
        if (StringUtils.hasText(ext)) {
            savename += "." + ext;
        }
        File f = new File(fullPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        byte[] buffer = new byte[1024];
        long size = 0;
        fullPath += separator + savename;
        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            int rc;
            while ((rc = is.read(buffer, 0, 1024)) > 0) {
                size += rc;
                fos.write(buffer, 0, rc);
                fos.flush();
            }
        }
        extra.setId(id);
        extra.setSize(new BigDecimal(size));
        extra.setType(ext);
        extra.setPath(fullPath);
        extra.setSavename(savename);
        extra.setTransstate("none");
//        extra.setId()
        return SqlHelper.retBool(baseMapper.insert(extra));
    }

    @Override
    public String download(OutputStream os, String id) throws IOException {
        TxalDoclink link = this.getById(id);
        try (InputStream is = Files.newInputStream(Paths.get(link.getPath()))) {
            byte[] buffer = new byte[1024];
            int rc;
            while ((rc = is.read(buffer, 0, 1024)) > 0) {
                os.write(buffer, 0, rc);
                os.flush();
            }
        }
        return link.getName();
    }

    /**
     * 下载pdf格式，如果不是，则进行转化
     *
     * @param os
     * @param id
     * @return
     * @throws IOException
     */
    @Override
    public String downloadAsPdf(OutputStream os, String id) throws IOException {
        TxalDoclink link = this.getById(id);
        String filepath = link.getPath();
        String ext = StringUtils.getFilenameExtension(filepath);
        if (StringUtils.hasText(ext) && canCovertExt.contains(ext.toLowerCase())) {
            String newFile = StringUtils.stripFilenameExtension(filepath) + ".pdf";
            if ("none".equals(link.getTransstate())) {
                link.setTransstate("running");
                updateById(link);
                try {
                    convertFileToPdf(filepath);
                    link.setTransstate("done");
                    updateById(link);
                } catch (OfficeException e) {
                    link.setTransstate("none");
                    updateById(link);
                    throw new RuntimeException(e);
                }
            } else if ("running".equals(link.getTransstate())) {
                throw new IOException("文件正在转化中请稍后查看");
            }

            try (InputStream is = Files.newInputStream(Paths.get(newFile))) {
                byte[] buffer = new byte[1024];
                int rc;
                while ((rc = is.read(buffer, 0, 1024)) > 0) {
                    os.write(buffer, 0, rc);
                    os.flush();
                }
            }
            return StringUtils.stripFilenameExtension(link.getName()) + ".pdf";
        }
        return download(os, id);
    }


    /**
     * 将文件转换成pdf文件
     *
     * @param filename
     */
    @Override
    public void convertFileToPdf(String filename) throws OfficeException {
        assert StringUtils.getFilenameExtension(filename) != null;
        String ext = StringUtils.getFilenameExtension(filename).toLowerCase();
        if (!canCovertExt.contains(ext)) {
            return;
        }

        String newName = StringUtils.stripFilenameExtension(filename) + ".pdf";
        if (pngExt.contains(ext)) {
            convertImageToPdf(filename, newName);
            return;
        }
        LocalOfficeManager manager = LocalOfficeManager.builder().officeHome(librOfficePath).install().build();
        try {
            manager.start();
            JodConverter.convert(new File(filename)).to(new File(newName)).execute();
        } finally {
            manager.stop();
        }
    }

    private void convertImageToPdf(String filename, String newFilename) {
        try {
            Image image = new Image(ImageDataFactory.create(filename)).setAutoScale(true);
            try (PdfDocument pdfDoc = new PdfDocument(new PdfWriter(newFilename));
                 Document document = new Document(pdfDoc, new PageSize(image.getImageWidth()+10,
                         image.getImageHeight()+10));
            ) {
                document.add(image);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        List<TxalDoclink> link = this.listByIds((Collection<String>) list);
        link.forEach(o -> {
            FileSystemUtils.deleteRecursively(new File(o.getPath()));
            //同步删除.pdf文件
            FileSystemUtils.deleteRecursively(new File(StringUtils.stripFilenameExtension(o.getPath()) + ".pdf"));
        });
        return super.removeBatchByIds(list);
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        List<TxalDoclink> link = this.listByMap(columnMap);
        link.forEach(o -> {
            FileSystemUtils.deleteRecursively(new File(o.getPath()));
            //同步删除.pdf文件
            FileSystemUtils.deleteRecursively(new File(StringUtils.stripFilenameExtension(o.getPath()) + ".pdf"));
        });
        return super.removeByMap(columnMap);
    }

    public static void main(String[] agrs) throws OfficeException {
//        String id = UUIDGenerator.generate();
//
//        System.out.println(id);
//        System.out.println(id.length());
//        System.out.println(File.separator);
//        System.out.println(UUID.fastUUID().toString().replaceAll("-", ""));
//        System.out.println(UUID.fastUUID().toString().replaceAll("-", ""));
//        System.out.println(UUID.fastUUID().toString().replaceAll("-", "").length());
        LocalOfficeManager manager = LocalOfficeManager.builder().officeHome("D:\\LibreOffice").install().build();
        manager.start();
        JodConverter.convert(new File("E:\\project\\99.其他资料\\09.du\\体系文件及案例库管理\\文档\\胶水管控系统案例\\胶水管控系统案例\\1. " +
                        "LXZJQW-IT-009胶水管控系统开发作业办法.doc")).
                to(new File("E:\\project\\99.其他资料\\09.du\\体系文件及案例库管理\\文档\\胶水管控系统案例\\胶水管控系统案例\\1. " +
                        "LXZJQW-IT-009胶水管控系统开发作业办法.pdf")).execute();
        manager.stop();

//       System.out.println(StringUtils.stripFilenameExtension("E:\\project\\99.其他资料\\09" +
//               ".du\\体系文件及案例库管理\\文档\\胶水管控系统案例\\胶水管控系统案例\\1. " +
//               "LXZJQW-IT-009胶水管控系统开发作业办法.doc"));
    }
}
