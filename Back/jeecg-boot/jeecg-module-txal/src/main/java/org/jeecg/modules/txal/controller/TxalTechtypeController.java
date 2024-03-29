package org.jeecg.modules.txal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.txal.entity.TxalTechtype;
import org.jeecg.modules.txal.service.ITxalTechtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 工艺类型
 * @Author: jeecg-boot
 * @Date: 2023-06-07
 * @Version: V1.0
 */
@Api(tags = "工艺类型")
@RestController
@RequestMapping("/txal/txalTechtype")
@Slf4j
public class TxalTechtypeController extends JeecgController<TxalTechtype, ITxalTechtypeService> {
    @Autowired
    private ITxalTechtypeService txalTechtypeService;

    /**
     * 分页列表查询
     *
     * @param txalTechtype
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "工艺类型-分页列表查询")
    @ApiOperation(value = "工艺类型-分页列表查询", notes = "工艺类型-分页列表查询")
    @GetMapping(value = "/rootList")
    public Result<IPage<TxalTechtype>> queryPageList(TxalTechtype txalTechtype,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        String hasQuery = req.getParameter("hasQuery");
        if (hasQuery != null && "true".equals(hasQuery)) {
            // 执行查询树形结构列表的逻辑
            QueryWrapper<TxalTechtype> queryWrapper = QueryGenerator.initQueryWrapper(txalTechtype, req.getParameterMap());
            queryWrapper.orderByAsc("code");
            List<TxalTechtype> list = txalTechtypeService.queryTreeListNoPage(queryWrapper);
            IPage<TxalTechtype> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } else {
            // 执行分页查询的逻辑
            String parentId = txalTechtype.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            txalTechtype.setPid(null);
            QueryWrapper<TxalTechtype> queryWrapper = QueryGenerator.initQueryWrapper(txalTechtype, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
//			queryWrapper.orderByAsc("code");
            Page<TxalTechtype> page = new Page<TxalTechtype>(pageNo, pageSize);
            page.setOrders(new ArrayList<OrderItem>() {{
                add(new OrderItem("code", true));
            }});
            IPage<TxalTechtype> pageList = txalTechtypeService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
    }

    /**
     * 【vue3专用】加载节点的子数据
     *
     * @param pid
     * @return
     */
    @RequestMapping(value = "/loadTreeChildren", method = RequestMethod.GET)
    public Result<List<SelectTreeModel>> loadTreeChildren(@RequestParam(name = "pid") String pid) {
        Result<List<SelectTreeModel>> result = new Result<>();
        try {
            List<SelectTreeModel> ls = txalTechtypeService.queryListByPid(pid);
            result.setResult(ls);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 【vue3专用】加载一级节点/如果是同步 则所有数据
     *
     * @param async
     * @param pcode
     * @return
     */
    @RequestMapping(value = "/loadTreeRoot", method = RequestMethod.GET)
    public Result<List<SelectTreeModel>> loadTreeRoot(@RequestParam(name = "async") Boolean async, @RequestParam(name = "pcode") String pcode) {
        Result<List<SelectTreeModel>> result = new Result<>();
        try {
            List<SelectTreeModel> ls = txalTechtypeService.queryListByCode(pcode);
            if (!async) {
                loadAllChildren(ls);
            }
            result.setResult(ls);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 【vue3专用】递归求子节点 同步加载用到
     *
     * @param ls
     */
    private void loadAllChildren(List<SelectTreeModel> ls) {
        for (SelectTreeModel tsm : ls) {
            List<SelectTreeModel> temp = txalTechtypeService.queryListByPid(tsm.getKey());
            if (temp != null && temp.size() > 0) {
                tsm.setChildren(temp);
                loadAllChildren(temp);
            }
        }
    }

    /**
     * 获取子数据
     *
     * @param txalTechtype
     * @param req
     * @return
     */
    //@AutoLog(value = "工艺类型-获取子数据")
    @ApiOperation(value = "工艺类型-获取子数据", notes = "工艺类型-获取子数据")
    @GetMapping(value = "/childList")
    public Result<IPage<TxalTechtype>> queryPageList(TxalTechtype txalTechtype, HttpServletRequest req) {
        QueryWrapper<TxalTechtype> queryWrapper = QueryGenerator.initQueryWrapper(txalTechtype, req.getParameterMap());
        List<TxalTechtype> list = txalTechtypeService.list(queryWrapper);
        IPage<TxalTechtype> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
        return Result.OK(pageList);
    }

    /**
     * 批量查询子节点
     *
     * @param parentIds 父ID（多个采用半角逗号分割）
     * @param parentIds
     * @return 返回 IPage
     * @return
     */
    //@AutoLog(value = "工艺类型-批量获取子数据")
    @ApiOperation(value = "工艺类型-批量获取子数据", notes = "工艺类型-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<TxalTechtype> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            queryWrapper.orderByAsc("code");
            List<TxalTechtype> list = txalTechtypeService.list(queryWrapper);
            IPage<TxalTechtype> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }

    /**
     * 添加
     *
     * @param txalTechtype
     * @return
     */
    @AutoLog(value = "工艺类型-添加")
    @ApiOperation(value = "工艺类型-添加", notes = "工艺类型-添加")
    @RequiresPermissions("txal:txal_techtype:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody TxalTechtype txalTechtype) {
        txalTechtypeService.addTxalTechtype(txalTechtype);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param txalTechtype
     * @return
     */
    @AutoLog(value = "工艺类型-编辑")
    @ApiOperation(value = "工艺类型-编辑", notes = "工艺类型-编辑")
    @RequiresPermissions("txal:txal_techtype:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody TxalTechtype txalTechtype) {
        txalTechtypeService.updateTxalTechtype(txalTechtype);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "工艺类型-通过id删除")
    @ApiOperation(value = "工艺类型-通过id删除", notes = "工艺类型-通过id删除")
    @RequiresPermissions("txal:txal_techtype:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        txalTechtypeService.deleteTxalTechtype(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "工艺类型-批量删除")
    @ApiOperation(value = "工艺类型-批量删除", notes = "工艺类型-批量删除")
    @RequiresPermissions("txal:txal_techtype:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.txalTechtypeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "工艺类型-通过id查询")
    @ApiOperation(value = "工艺类型-通过id查询", notes = "工艺类型-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<TxalTechtype> queryById(@RequestParam(name = "id", required = true) String id) {
        TxalTechtype txalTechtype = txalTechtypeService.getById(id);
        if (txalTechtype == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(txalTechtype);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param txalTechtype
     */
    @RequiresPermissions("txal:txal_techtype:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TxalTechtype txalTechtype) {
        return super.exportXls(request, txalTechtype, TxalTechtype.class, "工艺类型");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("txal:txal_techtype:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TxalTechtype.class);
    }

    @GetMapping()
    public Result<List<TxalTechtype>> queryForList() {
        return Result.OK(service.list());
    }


}
