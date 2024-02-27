package org.jeecg.modules.txal.service;

import org.jeecg.modules.txal.entity.TxalDoclink;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jodconverter.core.office.OfficeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description: 附件表
 * @Author: jeecg-boot
 * @Date:   2023-06-10
 * @Version: V1.0
 */
public interface ITxalDoclinkService extends IService<TxalDoclink> {
    boolean upload(InputStream is, TxalDoclink extra) throws IOException;

    String download(OutputStream os,String id)throws IOException;

    /**
     * 下载pdf格式，如果不是，则进行转化
     * @param os
     * @param id
     * @return
     * @throws IOException
     */
    String downloadAsPdf(OutputStream os,String id) throws IOException, OfficeException;

    /**
     * 将文件转换成pdf文件
     * @param filename
     */
    void convertFileToPdf(String filename) throws OfficeException;


}
