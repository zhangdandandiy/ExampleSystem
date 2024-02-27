package org.jeecg.modules.txal.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Base64;
import java.util.Enumeration;

/**
 * Created by Wonshine on 2017-10-19.
 */
@Slf4j
public class FileUtils {
    public static void saveToFile(final String filetext, final String filename) throws IOException {
        int index = filename.lastIndexOf("/");
        if (index < 1) {
            index = filename.lastIndexOf("\\");
        }
        if (index > 1) {
            new File(filename.substring(0, index)).mkdirs();
        }
        saveToFile(filetext, new File(filename));
    }

    public static void saveToFile(final String filetext, final File file) throws IOException {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(filetext);
        }
    }

    public static void saveBase64ToImg(final String base64String, final File file) throws IOException {
        Base64.Decoder decoder = Base64.getMimeDecoder();
        byte[] b = decoder.decode(base64String);
        int iLen = b.length;
        for (int i = 0; i < iLen; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(b);
            os.flush();
        }
    }

    public static void unzipFile(File file, String unzippath) throws IOException {
        try (ZipFile zipFile = new ZipFile(file,"utf-8")) {
            Enumeration<ZipEntry> e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry zipEnt = e.nextElement();
                String name = zipEnt.getName();
                log.debug(name);
                String filename = String.format("%s/%s", unzippath, name);
                File f = new File(filename);
                if (zipEnt.isDirectory()) {
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                } else {
                    try (BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEnt))) {// 建目录
                        File p = new File(f.getParentFile().getPath());
                        if (!p.exists()) {
                            p.mkdirs();
                        }
                        try (FileOutputStream fos = new FileOutputStream(f)) {
                            int len;
                            byte[] buff = new byte[1024];
                            while ((len = bis.read(buff)) != -1) {
                                fos.write(buff, 0, len);
                            }
                        }
                    }
                }
            }
        }
    }

//    /**
//     * 解压缩
//     *
//     * @param is
//     * @param unzippath
//     */
//    public static void unzipFile(InputStream is, String unzippath) throws IOException {
//        ZipInputStream zis = new ZipInputStream(is,Charset.forName("GBK") );//Charset.forName("GB2312")
//        ZipEntry ze;
//        while ((ze = zis.getNextEntry()) != null) {
//            String name = ze.getName();
//            String filename = String.format("%s/%s", unzippath, name);
//            File f = new File(filename);
//
//            if (ze.isDirectory()) {
//                if (!f.exists()) {
//                    f.mkdirs();
//                }
//                zis.closeEntry();
//            } else {
//                File p = new File(f.getParentFile().getPath());
//                if (!p.exists()) {
//                    p.mkdirs();
//                }
//                try (FileOutputStream os = new FileOutputStream(f)) {
//                    int i;
//                    byte[] ch = new byte[4096];
//                    while ((i = zis.read(ch)) != -1) {
//                        os.write(ch, 0, i);
//                    }
//                    zis.closeEntry();
//                }
//            }
//        }
//    }

//
//    private static void zipFile(ZipOutputStream zos, String path, File... files) throws IOException {
//        String ph = path;
//        if (ph != null && !"".equals(ph)) {
//            ph = ph.replaceAll("\\*", "/");
//            if (!ph.endsWith("/")) {
//                ph += "/";
//            }
//        } else {
//            ph = "";
//        }
//        for (File f : files) {
//            if (!f.isDirectory()) {
//                try (FileInputStream fis = new FileInputStream(f)) {
//                    zos.putNextEntry(new ZipEntry(ph + f.getName()));
//                    int len;
//                    byte[] buf = new byte[2048];
//                    while ((len = fis.read(buf)) != -1) {
//                        zos.write(buf, 0, len);
//                    }
//                }
//            } else {
//                String sph = f.getName();
//                sph = sph.replaceAll("\\*", "/");
//                if (!sph.endsWith("/")) {
//                    sph += "/";
//                }
//                zos.putNextEntry(new ZipEntry(ph + sph));
//                zipFile(zos, ph + sph, f.listFiles());
//            }
//        }
//    }
//
//    /**
//     * 压缩
//     *
//     * @param os
//     * @param zippath
//     * @param hasRootDir
//     */
//    public static void zipFile(OutputStream os, String zippath, boolean hasRootDir) throws IOException {
//        try(ZipOutputStream out = new ZipOutputStream(os)){
//            if (hasRootDir) {
//                zipFile(out, "", new File(zippath));
//            } else {
//                zipFile(out, "", new File(zippath).listFiles());
//            }
//            out.flush();
//        }
//    }
    public static void main(String[] args) throws Exception {
//        unzipFile(Files.newInputStream(Paths.get("E:\\project\\99.其他资料\\09
//        .du\\体系文件及案例库管理\\文档\\体系标准化\\LXZJQW-IT-001系统看板开发作业办法Rev2.zip"))
//                ,"E:\\project\\99.其他资料\\09.du\\体系文件及案例库管理\\文档\\体系标准化\\LXZJQW-IT-001系统看板开发作业办法Rev2");
                unzipFile(new File("E:\\project\\99.其他资料\\09.du\\体系文件及案例库管理\\文档\\体系标准化\\LXZJQW-IT-001系统看板开发作业办法Rev2" +
                                ".zip")
                ,"E:\\project\\99.其他资料\\09.du\\体系文件及案例库管理\\文档\\体系标准化\\LXZJQW-IT-001系统看板开发作业办法Rev2");
//        SortedMap<String, Charset> map = Charset.availableCharsets();
//        for (String alias : map.keySet()) {
//            // 输出字符集的别名
//            System.out.println(alias);
//
//        }
    }
}
