package org.jeecg.modules.txal.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 体系作业明细
 * @Author: jeecg-boot
 * @Date:   2023-07-07
 * @Version: V1.0
 */
@Data
@TableName("txal_txjobd")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="txal_txjobd对象", description="体系作业明细")
public class TxalTxjobd implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**作业编号*/
	@Excel(name = "作业编号", width = 15)
    @ApiModelProperty(value = "作业编号")
    private java.lang.String code;

    @Excel(name = "主题", width = 15)
    @ApiModelProperty(value = "主题")
    private java.lang.String title;
	/**作业内容*/
	@Excel(name = "作业内容", width = 15)
    @ApiModelProperty(value = "作业内容")
    private java.lang.String context;
	/**主导部门*/
	@Excel(name = "主导部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "主导部门")
    private java.lang.String maOrgCode;
	/**关联部门*/
	@Excel(name = "关联部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "关联部门")
    private java.lang.String lkOrgCode;
	/**体系作业*/
	@Excel(name = "体系作业", width = 15)
    @ApiModelProperty(value = "体系作业")
    private java.lang.String fid;
}
