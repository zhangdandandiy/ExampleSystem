package org.jeecg.modules.txal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: wonshine
 * @Date: 2023/6/12 20:50
 * @Version: 1.0
 * @Content:
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="txal_anli对象", description="案例")
public class TxalLearnTjData implements Serializable {
    private String code;
    private String name;

    private Long total;
}
