package org.jeecg.modules.txal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.txal.entity.TxalLearnTjData;
import org.jeecg.modules.txal.entity.TxalLearninfo;
import org.jeecg.modules.txal.mapper.TxalLearnTjDataMapper;
import org.jeecg.modules.txal.mapper.TxalLearninfoMapper;
import org.jeecg.modules.txal.service.ITxalLearnTjDataService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wonshine
 * @Date: 2023/6/12 21:13
 * @Version: 1.0
 * @Content:
 */
@Service
public class TxalLearnTjDataServiceImpl extends ServiceImpl<TxalLearnTjDataMapper, TxalLearnTjData> implements ITxalLearnTjDataService {
    @Override
    public List<TxalLearnTjData> queryOrgTj(Map<String, Object> map) {
        Map<String, Object> params = paramsGen(map);
        return baseMapper.queryOrgTj(params);
    }

    private Map<String, Object> paramsGen(Map<String, Object> map) {
        Map<String, Object> params = new HashMap<>(map);
        if (params.containsKey("learntime")) {
            List<String> time = (List<String>) params.get("learntime");
            params.remove("learntime");
            if (time != null && time.size() == 2) {
                params.put("startTime", LocalDateTime.of(LocalDate.parse(time.get(0),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.MIN));
                params.put("endTime", LocalDateTime.of(LocalDate.parse(time.get(1),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.MAX));
            }
        }
        return params;
    }

    @Override
    public List<TxalLearnTjData> queryUserTj(Map<String, Object> map) {
        Map<String, Object> params = paramsGen(map);
        return baseMapper.queryUserTj(params);
    }

    @Override
    public List<TxalLearnTjData> queryUserTjTop10(Map<String, Object> map) {
        Map<String, Object> params = paramsGen(map);
        return baseMapper.queryUserTjTop10(params);
    }

}
