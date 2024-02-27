package org.jeecg.modules.txal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.txal.entity.TxalTechtype;

import java.util.List;
import java.util.Map;

/**
 * @Description: 工艺类型
 * @Author: jeecg-boot
 * @Date: 2023-06-07
 * @Version: V1.0
 */
public interface TxalTechtypeMapper extends BaseMapper<TxalTechtype> {

    /**
     * 编辑节点状态
     *
     * @param id
     * @param status
     */
    void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

    /**
     * 【vue3专用】根据父级ID查询树节点数据
     *
     * @param pid
     * @param query
     * @return
     */
    List<SelectTreeModel> queryListByPid(@Param("pid") String pid, @Param("query") Map<String, String> query);

}
