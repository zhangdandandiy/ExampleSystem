package org.jeecg.modules.txal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.txal.entity.TxalTechtype;
import org.jeecg.modules.txal.mapper.TxalTechtypeMapper;
import org.jeecg.modules.txal.service.ITxalTechtypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 工艺类型
 * @Author: jeecg-boot
 * @Date: 2023-06-07
 * @Version: V1.0
 */
@Service
public class TxalTechtypeServiceImpl extends ServiceImpl<TxalTechtypeMapper, TxalTechtype> implements ITxalTechtypeService {

    /**
     * 新增节点
     *
     * @param txalTechtype
     */
    @Override
    public void addTxalTechtype(TxalTechtype txalTechtype) {
        // 新增时设置hasChild为0
        txalTechtype.setHasChild(ITxalTechtypeService.NOCHILD);
        if (oConvertUtils.isEmpty(txalTechtype.getPid())) {
            // 如果当前节点父ID为空(pid为空)，则为根节点
            txalTechtype.setPid(ITxalTechtypeService.ROOT_PID_VALUE);
        } else {
            // 如果当前节点父ID不为空 则设置父节点的hasChildren 为1
            TxalTechtype parent = baseMapper.selectById(txalTechtype.getPid());
            if (parent != null && !"1".equals(parent.getHasChild())) {
                parent.setHasChild("1");
                baseMapper.updateById(parent);
            }
        }
        baseMapper.insert(txalTechtype);
    }

    /**
     * 修改节点
     *
     * @param txalTechtype
     * @throws JeecgBootException
     */
    @Override
    public void updateTxalTechtype(TxalTechtype txalTechtype) {
        TxalTechtype entity = this.getById(txalTechtype.getId());
        if (entity == null) {
            throw new JeecgBootException("未找到对应实体");
        }
        String old_pid = entity.getPid();
        String new_pid = txalTechtype.getPid();
        if (!old_pid.equals(new_pid)) {
            // 如果更改了父节点，则要更新父节点
            updateOldParentNode(old_pid);
            if (oConvertUtils.isEmpty(new_pid)) {
                // 如果没传入pid，即说明他就是根节点
                txalTechtype.setPid(ITxalTechtypeService.ROOT_PID_VALUE);
            }
            if (!ITxalTechtypeService.ROOT_PID_VALUE.equals(txalTechtype.getPid())) {
                // 如果传入的pid的值不为0，则说明他不是根节点，就要将他的根节点更新为有子节点
                baseMapper.updateTreeNodeStatus(txalTechtype.getPid(), ITxalTechtypeService.HASCHILD);
            }
        }
        baseMapper.updateById(txalTechtype);
    }

    /**
     * 删除节点
     *
     * @param id
     * @throws JeecgBootException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTxalTechtype(String id) throws JeecgBootException {
        // 查询选中节点下所有子节点
        id = this.queryTreeChildIds(id);
        if (id.indexOf(",") > 0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if (idVal != null) {
                    TxalTechtype txalTechtype = this.getById(idVal);
                    String pidVal = txalTechtype.getPid();
                    // 查询此节点的父节点是否还有其他子节点
                    List<TxalTechtype> dataList = baseMapper.selectList(new QueryWrapper<TxalTechtype>().eq("pid", pidVal).notIn("id", Arrays.asList(idArr)));
                    boolean flag = (dataList == null || dataList.size() == 0) && !Arrays.asList(idArr).contains(pidVal) && !sb.toString().contains(pidVal);
                    if (flag) {
                        // 如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            // 批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            // 修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for (String pid : pidArr) {
                this.updateOldParentNode(pid);
            }
        } else {
            TxalTechtype txalTechtype = this.getById(id);
            if (txalTechtype == null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(txalTechtype.getPid());
            baseMapper.deleteById(id);
        }
    }

    /**
     * 查询所有数据，无分页
     *
     * @param queryWrapper
     * @return List<TxalTechtype>
     */
    @Override
    public List<TxalTechtype> queryTreeListNoPage(QueryWrapper<TxalTechtype> queryWrapper) {
        List<TxalTechtype> dataList = baseMapper.selectList(queryWrapper);
        List<TxalTechtype> mapList = new ArrayList<>();
        for (TxalTechtype data : dataList) {
            String pidVal = data.getPid();
            // 递归查询子节点的根节点
            if (pidVal != null && !ITxalTechtypeService.NOCHILD.equals(pidVal)) {
                TxalTechtype rootVal = this.getTreeRoot(pidVal);
                if (rootVal != null && !mapList.contains(rootVal)) {
                    mapList.add(rootVal);
                }
            } else {
                if (!mapList.contains(data)) {
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }

    /**
     * 【vue3专用】根据父级编码加载分类字典的数据
     *
     * @param parentCode
     * @return
     */
    @Override
    public List<SelectTreeModel> queryListByCode(String parentCode) {
        String pid = ROOT_PID_VALUE;
        if (oConvertUtils.isNotEmpty(parentCode)) {
            LambdaQueryWrapper<TxalTechtype> queryWrapper = new LambdaQueryWrapper<>();
            // 设置等同于条件
            queryWrapper.eq(TxalTechtype::getPid, parentCode);
            List<TxalTechtype> list = baseMapper.selectList(queryWrapper);
            if (list == null || list.size() == 0) {
                throw new JeecgBootException("该编码【" + parentCode + "】不存在，请核实!");
            }
            if (list.size() > 1) {
                throw new JeecgBootException("该编码【" + parentCode + "】存在多个，请核实!");
            }
            pid = list.get(0).getId();
        }
        // 查询节点数据，树状结构
        return baseMapper.queryListByPid(pid, null);
    }

    /**
     * 【vue3专用】根据pid查询子节点集合
     *
     * @param pid
     * @return
     */
    @Override
    public List<SelectTreeModel> queryListByPid(String pid) {
        if (oConvertUtils.isEmpty(pid)) {
            pid = ROOT_PID_VALUE;
        }
        return baseMapper.queryListByPid(pid, null);
    }

    /**
     * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
     *
     * @param pid
     */
    private void updateOldParentNode(String pid) {
        // 如果父节点不为根结点
        if (!ITxalTechtypeService.ROOT_PID_VALUE.equals(pid)) {
            // 查询父节点的子节点的数量
            Long count = baseMapper.selectCount(new QueryWrapper<TxalTechtype>().eq("pid", pid));
            if (count == null || count <= 1) {
                // 如果查到pid与父节点相同的数量为空或者为1，则说明父节点没有子节点了
                baseMapper.updateTreeNodeStatus(pid, ITxalTechtypeService.NOCHILD);
            }
        }
    }

    /**
     * 递归查询节点的根节点
     *
     * @param pidVal
     * @return
     */
    private TxalTechtype getTreeRoot(String pidVal) {
        TxalTechtype data = baseMapper.selectById(pidVal);
        if (data != null && !ITxalTechtypeService.ROOT_PID_VALUE.equals(data.getPid())) {
            return this.getTreeRoot(data.getPid());
        } else {
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     *
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        // 获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if (pidVal != null) {
                if (!sb.toString().contains(pidVal)) {
                    if (sb.toString().length() > 0) {
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    // 根据递归查询所有子节点
                    this.getTreeChildIds(pidVal, sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     *
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal, StringBuffer sb) {
        List<TxalTechtype> dataList = baseMapper.selectList(new QueryWrapper<TxalTechtype>().eq("pid", pidVal));
        if (dataList != null && dataList.size() > 0) {
            for (TxalTechtype tree : dataList) {
                if (!sb.toString().contains(tree.getId())) {
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(), sb);
            }
        }
        return sb;
    }

}
