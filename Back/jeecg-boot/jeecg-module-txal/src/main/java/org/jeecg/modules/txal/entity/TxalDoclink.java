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
 * @Description: 附件表
 * @Author: jeecg-boot
 * @Date:   2023-06-10
 * @Version: V1.0
 */
@Data
@TableName("txal_doclink")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="txal_doclink对象", description="附件表")
public class TxalDoclink implements Serializable {
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
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**文件编号*/
	@Excel(name = "文件编号", width = 15)
    @ApiModelProperty(value = "文件编号")
    private java.lang.String code;
	/**文件名称*/
	@Excel(name = "文件名称", width = 15)
    @ApiModelProperty(value = "文件名称")
    private java.lang.String name;
	/**文件大小*/
	@Excel(name = "文件大小", width = 15)
    @ApiModelProperty(value = "文件大小")
    private java.math.BigDecimal size;
	/**文件类型*/
	@Excel(name = "文件类型", width = 15)
    @ApiModelProperty(value = "文件类型")
    private java.lang.String type;
	/**文件路径*/
	@Excel(name = "文件路径", width = 15)
    @ApiModelProperty(value = "文件路径")
    private java.lang.String path;
	/**存储文件名*/
	@Excel(name = "存储文件名", width = 15)
    @ApiModelProperty(value = "存储文件名")
    private java.lang.String savename;
	/**缩略图*/
	@Excel(name = "缩略图", width = 15)
    @ApiModelProperty(value = "缩略图")
    private java.lang.String thumb;
	/**业务表*/
	@Excel(name = "业务表", width = 15)
    @ApiModelProperty(value = "业务表")
    private java.lang.String linkTable;
	/**业务表ID*/
	@Excel(name = "业务表ID", width = 15)
    @ApiModelProperty(value = "业务表ID")
    private java.lang.String linkId;
	/**校验码*/
	@Excel(name = "校验码", width = 15)
    @ApiModelProperty(value = "校验码")
    private java.lang.String validcode;

    @Excel(name = "转化状态", width = 15)
    @ApiModelProperty(value = "转化状态")
    private java.lang.String transstate;
}
