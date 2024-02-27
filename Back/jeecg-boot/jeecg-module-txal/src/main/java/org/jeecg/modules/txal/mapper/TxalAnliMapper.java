package org.jeecg.modules.txal.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.txal.entity.TxalAnli;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 案例
 * @Author: jeecg-boot
 * @Date:   2023-06-09
 * @Version: V1.0
 */
public interface TxalAnliMapper extends BaseMapper<TxalAnli> {
    String newAnliCode(@Param("pcode") String pcode, @Param("code") String code);
}
