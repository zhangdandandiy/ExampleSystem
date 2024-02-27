package org.jeecg.modules.txal.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.modules.txal.entity.TxalAnli;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.txal.entity.TxalDoclink;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 案例
 * @Author: jeecg-boot
 * @Date:   2023-06-09
 * @Version: V1.0
 */
public interface ITxalAnliService extends IService<TxalAnli> {
    String newAnliCode(String pTTCode, String ttCode);

    boolean save(TxalAnli anli, TxalDoclink[] doclink, MultipartFile[] files);

    boolean updateById(TxalAnli entity, TxalDoclink[] doclink, MultipartFile[] files);

    boolean nameExists(String name, String id);
}
