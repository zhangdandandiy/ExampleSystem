package org.jeecg.modules.txal.service.impl;

import org.jeecg.modules.txal.entity.TxalTxjobd;
import org.jeecg.modules.txal.mapper.TxalTxjobdMapper;
import org.jeecg.modules.txal.service.ITxalDoclinkService;
import org.jeecg.modules.txal.service.ITxalTxjobdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 体系作业明细
 * @Author: jeecg-boot
 * @Date: 2023-07-07
 * @Version: V1.0
 */
@Service
public class TxalTxjobdServiceImpl extends ServiceImpl<TxalTxjobdMapper, TxalTxjobd> implements ITxalTxjobdService {
    @Autowired
    ITxalDoclinkService doclinkService;

    @Override
    public boolean removeById(Serializable id) {
        doclinkService.removeByMap(new HashMap<String, Object>() {{
            put("link_table", "txal_txjobd");
            put("link_id", id);
        }});
        return super.removeById(id);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        list.forEach(o -> {
            doclinkService.removeByMap(new HashMap<String, Object>() {{
                put("link_table", "txal_txjobd");
                put("link_id", o);
            }});
        });
        return super.removeBatchByIds(list);
    }
}
