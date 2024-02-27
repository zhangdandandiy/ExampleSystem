package org.jeecg.modules.txal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 工艺类型
 * @Author: jeecg-boot
 * @Date: 2023-06-07
 * @Version: V1.0
 */
@Data
@TableName("txal_techtype")
@ApiModel(value = "txal_techtype对象", description = "工艺类型")
public class TxalTechtype implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     * ASSIGN_ID：使用雪花算法生成主键
     * AUTO_INCREMENT：自增长，数据库自动分配主键（适用于MySQL、SQL Server等）
     * IDENTITY：自增长，数据库自动分配主键（适用于Oracle）
     * UUID：使用UUID字符串作为主键
     * NONE：不指定主键生成策略，需手动设置主键值
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * JsonFormat：定义Java对象在序列化为JSON字符串时的日期格式
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 父级节点
     */
    @Excel(name = "父级节点", width = 15)
    @ApiModelProperty(value = "父级节点")
    private java.lang.String pid;
    /**
     * 是否有子节点
     */
    @Excel(name = "是否有子节点", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否有子节点")
    private java.lang.String hasChild;
    /**
     * 属性编码
     */
    @Excel(name = "属性编码", width = 15)
    @ApiModelProperty(value = "属性编码")
    private java.lang.String code;
    /**
     * 属性类型
     */
    @Excel(name = "属性类型", width = 15)
    @ApiModelProperty(value = "属性类型")
    private java.lang.String sxmc;
    /**
     * 类型名称
     */
    @Excel(name = "类型名称", width = 15)
    @ApiModelProperty(value = "类型名称")
    private java.lang.String name;
    /**
     * 开始流水号
     */
    @Excel(name = "开始流水号", width = 15)
    @ApiModelProperty(value = "开始流水号")
    private java.lang.String minidx;
    /**
     * 结束流水号
     */
    @Excel(name = "结束流水号", width = 15)
    @ApiModelProperty(value = "结束流水号")
    private java.lang.String maxidx;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
}
