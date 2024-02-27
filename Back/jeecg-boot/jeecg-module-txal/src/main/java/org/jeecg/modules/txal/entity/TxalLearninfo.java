package org.jeecg.modules.txal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 学习情况
 * @Author: jeecg-boot
 * @Date: 2023-06-11
 * @Version: V1.0
 */
@Data
@TableName("txal_learninfo")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "txal_learninfo对象", description = "学习情况")
public class TxalLearninfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 学习时间
     */
    @Excel(name = "学习时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "学习时间")
    private java.util.Date learnTime;
    /**
     * 所属部门
     */
    @Excel(name = "所属部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "所属部门")
    private java.lang.String orgCode;
    /**
     * 人员id
     */
    @Excel(name = "人员id", width = 15)
    @ApiModelProperty(value = "人员id")
    private java.lang.String learnUserId;
    /**
     * 人员账号
     */
    @Excel(name = "人员账号", width = 15)
    @ApiModelProperty(value = "人员账号")
    private java.lang.String learnUserCode;
    /**
     * 姓名
     */
    @Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String learnUserName;
    /**
     * 案例Id
     */
    @Excel(name = "案例Id", width = 15, dictTable = "txal_anli", dicText = "name", dicCode = "code")
    @Dict(dictTable = "txal_anli", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "案例Id")
    private java.lang.String anliId;
    /**
     * 年度
     */
    @Excel(name = "年度", width = 15)
    @ApiModelProperty(value = "年度")
    private java.lang.Integer nd;
    /**
     * 月份
     */
    @Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
    private java.lang.Integer yf;
}
