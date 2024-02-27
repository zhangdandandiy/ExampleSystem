package org.jeecg.modules.txal.mapper;

import org.jeecg.modules.txal.entity.TxalTxjob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: 体系作业
 * @Author: jeecg-boot
 * @Date:   2023-07-04
 * @Version: V1.0
 */
public interface TxalTxjobMapper extends BaseMapper<TxalTxjob> {
    List<String> queryPnameList();
}
