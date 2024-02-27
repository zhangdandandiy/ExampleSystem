package org.jeecg.modules.txal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.txal.entity.TxalLearnTjData;
import org.jeecg.modules.txal.entity.TxalLearninfo;

import java.util.List;
import java.util.Map;

/**
 * @Author: wonshine
 * @Date: 2023/6/12 21:12
 * @Version: 1.0
 * @Content:
 */
public interface ITxalLearnTjDataService extends IService<TxalLearnTjData> {
    List<TxalLearnTjData> queryOrgTj(Map<String,Object> map);

    List<TxalLearnTjData> queryUserTj(Map<String,Object> map);

    List<TxalLearnTjData> queryUserTjTop10(Map<String,Object> map);
}
