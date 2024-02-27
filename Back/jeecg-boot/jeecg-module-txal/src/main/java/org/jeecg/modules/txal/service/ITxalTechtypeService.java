package org.jeecg.modules.txal.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.txal.entity.TxalTechtype;

import java.util.List;

/**
 * @Description: 工艺类型
 * @Author: jeecg-boot
 * @Date: 2023-06-07
 * @Version: V1.0
 */
public interface ITxalTechtypeService extends IService<TxalTechtype> {

    /**
     * 根节点父ID的值
     */
    public static final String ROOT_PID_VALUE = "0";

    /**
     * 树节点有子节点状态值
     */
    public static final String HASCHILD = "1";

    /**
     * 树节点无子节点状态值
     */
    public static final String NOCHILD = "0";

    /**
     * 新增节点
     *
     * @param txalTechtype
     */
    void addTxalTechtype(TxalTechtype txalTechtype);

    /**
     * 修改节点
     *
     * @param txalTechtype
     * @throws JeecgBootException
     */
    void updateTxalTechtype(TxalTechtype txalTechtype) throws JeecgBootException;

    /**
     * 删除节点
     *
     * @param id
     * @throws JeecgBootException
     */
    void deleteTxalTechtype(String id) throws JeecgBootException;

    /**
     * 查询所有数据，无分页
     *
     * @param queryWrapper
     * @return List<TxalTechtype>
     */
    List<TxalTechtype> queryTreeListNoPage(QueryWrapper<TxalTechtype> queryWrapper);

    /**
     * 【vue3专用】根据父级编码加载分类字典的数据
     *
     * @param parentCode
     * @return
     */
    List<SelectTreeModel> queryListByCode(String parentCode);

    /**
     * 【vue3专用】根据pid查询子节点集合
     *
     * @param pid
     * @return
     */
    List<SelectTreeModel> queryListByPid(String pid);

}
