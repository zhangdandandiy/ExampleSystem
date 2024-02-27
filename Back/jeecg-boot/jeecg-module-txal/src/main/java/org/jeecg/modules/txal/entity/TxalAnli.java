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
 * @Description: 案例
 * @Author: jeecg-boot
 * @Date:   2023-06-09
 * @Version: V1.0
 */
@Data
@TableName("txal_anli")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="txal_anli对象", description="案例")
public class TxalAnli implements Serializable {
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
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**案例库*/
	@Excel(name = "案例库", width = 15,dictTable = "txal_anlik", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "案例库")
    @Dict(dictTable = "txal_anlik", dicText = "name", dicCode = "code")
    private java.lang.String anlikId;
	/**案例名称*/
	@Excel(name = "案例名称", width = 15)
    @ApiModelProperty(value = "案例名称")
    private java.lang.String name;
	/**上传部门*/
	@Excel(name = "上传部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "上传部门")
    private java.lang.String deptCode;
	/**专案*/
	@Excel(name = "专案", width = 15)
    @ApiModelProperty(value = "专案")
    private java.lang.String percode;
	/**产品类型*/
	@Excel(name = "产品类型", width = 15, dictTable = "txal_protype", dicText = "name", dicCode = "code")
	@Dict(dictTable = "txal_protype", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "产品类型")
    private java.lang.String protypeId;
	/**属性类型*/
	@Excel(name = "属性类型", width = 15, dictTable = "txal_techtype where pid=0", dicText = "sxmc", dicCode = "code")
	@Dict(dictTable = "txal_techtype where pid=0", dicText = "sxmc", dicCode = "code")
    @ApiModelProperty(value = "属性类型")
    private java.lang.String techtypePid;
	/**子类型属性*/
	@Excel(name = "子类型属性", width = 15)
    @ApiModelProperty(value = "子类型属性")
    private java.lang.String techtypeSxmc;
	/**子类型名称*/
	@Excel(name = "子类型名称", width = 15,dictTable = "txal_techtype ", dicText = "name", dicCode = "code")
    @Dict(dictTable = "txal_techtype ", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "子类型名称")
    private java.lang.String techtypeId;
	/**关键词*/
	@Excel(name = "关键词", width = 15)
    @ApiModelProperty(value = "关键词")
    private java.lang.String keywords;
	/**案例编号*/
	@Excel(name = "案例编号", width = 15)
    @ApiModelProperty(value = "案例编号")
    private java.lang.String code;
	/**问题描述*/
	@Excel(name = "问题描述", width = 15)
    @ApiModelProperty(value = "问题描述")
    private java.lang.String question;
	/**根本原因*/
	@Excel(name = "根本原因", width = 15)
    @ApiModelProperty(value = "根本原因")
    private java.lang.String reason;
	/**改善措施*/
	@Excel(name = "改善措施", width = 15)
    @ApiModelProperty(value = "改善措施")
    private java.lang.String solution;
	/**评价*/
	@Excel(name = "评价", width = 15, dicCode = "taxl_alievalt")
	@Dict(dicCode = "taxl_alievalt")
    @ApiModelProperty(value = "评价")
    private java.lang.String evalt;

    @Excel(name = "关联内容", width = 30)
    @ApiModelProperty(value = "关联内容")
    private java.lang.String linktext;

    /**总结-技术*/
    @Excel(name = "总结-技术", width = 15)
    @ApiModelProperty(value = "总结-技术")
    private java.lang.String sbTech;
    /**总结-管理*/
    @Excel(name = "总结-管理", width = 15)
    @ApiModelProperty(value = "总结-管理")
    private java.lang.String sbMng;
    /**设计before*/
    @Excel(name = "变更内容-设计before", width = 15)
    @ApiModelProperty(value = "设计before")
    private java.lang.String bgDesB;
    /**设计after*/
    @Excel(name = "变更内容-设计after", width = 15)
    @ApiModelProperty(value = "设计after")
    private java.lang.String bgDesA;
    /**检测before*/
    @Excel(name = "变更内容-检测before", width = 15)
    @ApiModelProperty(value = "检测before")
    private java.lang.String bgJcB;
    /**检测after*/
    @Excel(name = "变更内容-检测after", width = 15)
    @ApiModelProperty(value = "检测after")
    private java.lang.String bgJcA;
    /**流程before*/
    @Excel(name = "变更内容-流程before", width = 15)
    @ApiModelProperty(value = "流程before")
    private java.lang.String bgLcB;
    /**流程after*/
    @Excel(name = "变更内容-流程after", width = 15)
    @ApiModelProperty(value = "流程after")
    private java.lang.String bgLcA;
    /**管理方式before*/
    @Excel(name = "变更内容-管理方式before", width = 15)
    @ApiModelProperty(value = "管理方式before")
    private java.lang.String bgGlfsB;
    /**管理方式after*/
    @Excel(name = "变更内容-管理方式after", width = 15)
    @ApiModelProperty(value = "管理方式after")
    private java.lang.String bgGlfsA;
    /**良率before*/
    @Excel(name = "变更后关键指标-良率before", width = 15)
    @ApiModelProperty(value = "良率before")
    private java.lang.String bghLlB;
    /**良率after*/
    @Excel(name = "变更后关键指标-良率after", width = 15)
    @ApiModelProperty(value = "良率after")
    private java.lang.String bghLlA;
    /**CT提升before*/
    @Excel(name = "变更后关键指标-CT提升或停机变少before", width = 15)
    @ApiModelProperty(value = "CT提升before")
    private java.lang.String bghCtB;
    /**CT提升after*/
    @Excel(name = "变更后关键指标-CT提升或停机变少after", width = 15)
    @ApiModelProperty(value = "CT提升after")
    private java.lang.String bghCtA;
    /**人力减少before*/
    @Excel(name = "变更后关键指标-人力减少before", width = 15)
    @ApiModelProperty(value = "人力减少before")
    private java.lang.String bghRlB;
    /**人力减少after*/
    @Excel(name = "变更后关键指标-人力减少after", width = 15)
    @ApiModelProperty(value = "人力减少after")
    private java.lang.String bghRlA;
    /**布局优化before*/
    @Excel(name = "变更后关键指标-布局优化before", width = 15)
    @ApiModelProperty(value = "布局优化before")
    private java.lang.String bghLyB;
    /**布局优化after*/
    @Excel(name = "变更后关键指标-布局优化after", width = 15)
    @ApiModelProperty(value = "布局优化after")
    private java.lang.String bghLyA;
}
