package org.jeecg.modules.txal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.txal.entity.TxalDoclink;
import org.jeecg.modules.txal.entity.TxalTxjob;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description: 体系作业
 * @Author: jeecg-boot
 * @Date:   2023-07-04
 * @Version: V1.0
 */
public interface ITxalTxjobService extends IService<TxalTxjob> {
    boolean save(TxalTxjob job, MultipartFile file) throws IOException;

    boolean updateById(TxalTxjob entity, TxalDoclink[] doclink, MultipartFile[] files);

    List<String> queryPnameList();
}
