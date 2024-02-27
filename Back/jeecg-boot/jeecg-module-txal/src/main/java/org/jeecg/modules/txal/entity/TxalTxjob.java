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
 * @Description: 体系作业
 * @Author: jeecg-boot
 * @Date:   2023-07-04
 * @Version: V1.0
 */
@Data
@TableName("txal_txjob")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="txal_txjob对象", description="体系作业")
public class TxalTxjob implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**三阶文件名称*/
	@Excel(name = "三阶文件名称", width = 15)
    @ApiModelProperty(value = "三阶文件名称")
    private java.lang.String fname;
	/**三阶文件编号*/
	@Excel(name = "三阶文件编号", width = 15)
    @ApiModelProperty(value = "三阶文件编号")
    private java.lang.String fcode;
	/**三阶级文件最新版本*/
	@Excel(name = "三阶级文件最新版本", width = 15)
    @ApiModelProperty(value = "三阶级文件最新版本")
    private java.lang.String fver;
	/**归属程序名称*/
	@Excel(name = "归属程序名称", width = 15)
    @ApiModelProperty(value = "归属程序名称")
    private java.lang.String pname;
	/**作业办法关联部门*/
    @Excel(name = "作业办法关联部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "作业办法关联部门")
    private java.lang.String orgs;
}
