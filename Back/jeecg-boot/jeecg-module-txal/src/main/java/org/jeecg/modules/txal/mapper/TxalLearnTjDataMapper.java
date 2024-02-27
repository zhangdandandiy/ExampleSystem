package org.jeecg.modules.txal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.txal.entity.TxalLearnTjData;
import org.jeecg.modules.txal.entity.TxalLearninfo;

import java.util.List;
import java.util.Map;

/**
 * @Author: wonshine
 * @Date: 2023/6/12 20:56
 * @Version: 1.0
 * @Content:
 */
public interface TxalLearnTjDataMapper extends BaseMapper<TxalLearnTjData> {
    List<TxalLearnTjData> queryOrgTj(Map<String,Object> map);

    List<TxalLearnTjData> queryUserTjTop10(Map<String,Object> map);

    List<TxalLearnTjData> queryUserTj(Map<String,Object> map);
}
