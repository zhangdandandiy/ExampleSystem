package org.jeecg.modules.txal.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.txal.entity.TxalAnli;
import org.jeecg.modules.txal.entity.TxalDoclink;
import org.jeecg.modules.txal.entity.TxalTxjob;
import org.jeecg.modules.txal.service.ITxalTxjobService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @Description: 体系作业
 * @Author: jeecg-boot
 * @Date: 2023-07-04
 * @Version: V1.0
 */
@Api(tags = "体系作业")
@RestController
@RequestMapping("/txal/txalTxjob")
@Slf4j
public class TxalTxjobController extends JeecgController<TxalTxjob, ITxalTxjobService> {
    @Autowired
    private ITxalTxjobService txalTxjobService;

    /**
     * 分页列表查询
     *
     * @param txalTxjob
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "体系作业-分页列表查询")
    @ApiOperation(value = "体系作业-分页列表查询", notes = "体系作业-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<TxalTxjob>> queryPageList(TxalTxjob txalTxjob,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<TxalTxjob> queryWrapper = QueryGenerator.initQueryWrapper(txalTxjob, req.getParameterMap());
        Page<TxalTxjob> page = new Page<TxalTxjob>(pageNo, pageSize);
        IPage<TxalTxjob> pageList = txalTxjobService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param txalTxjob
     * @return
     */
    @AutoLog(value = "体系作业-添加")
    @ApiOperation(value = "体系作业-添加", notes = "体系作业-添加")
    @RequiresPermissions("txal:txal_txjob:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestParam(value = "data") String data,
                              @RequestParam(value = "docinfo", required = false) String docinfo,
                              @RequestPart(value = "file", required = false) MultipartFile file) throws InvocationTargetException,
            IllegalAccessException, IOException {
        TxalTxjob anli = JSONObject.toJavaObject(JSON.parseObject(data), TxalTxjob.class);
        if (txalTxjobService.save(anli, file)) {
            return Result.OK("添加成功！");
        } else {
            return Result.error("添加失败！\r\n该版本作业已经存在！");
        }
    }


    /**
     * 编辑
     *
     * @param txalTxjob
     * @return
     */
    @AutoLog(value = "体系作业-编辑")
    @ApiOperation(value = "体系作业-编辑", notes = "体系作业-编辑")
    @RequiresPermissions("txal:txal_txjob:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody TxalTxjob txalTxjob) {
        if (txalTxjobService.updateById(txalTxjob)) {
            return Result.OK("编辑成功!");
        } else {
            return Result.error("编辑失败！\r\n该版本作业已经存在！");
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "体系作业-通过id删除")
    @ApiOperation(value = "体系作业-通过id删除", notes = "体系作业-通过id删除")
    @RequiresPermissions("txal:txal_txjob:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        txalTxjobService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "体系作业-批量删除")
    @ApiOperation(value = "体系作业-批量删除", notes = "体系作业-批量删除")
    @RequiresPermissions("txal:txal_txjob:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.txalTxjobService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "体系作业-通过id查询")
    @ApiOperation(value = "体系作业-通过id查询", notes = "体系作业-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<TxalTxjob> queryById(@RequestParam(name = "id", required = true) String id) {
        TxalTxjob txalTxjob = txalTxjobService.getById(id);
        if (txalTxjob == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(txalTxjob);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param txalTxjob
     */
//    @RequiresPermissions("txal:txal_txjob:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TxalTxjob txalTxjob) {
        return super.exportXls(request, txalTxjob, TxalTxjob.class, "体系作业");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("txal:txal_txjob:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TxalTxjob.class);
    }

    @GetMapping(value = "/pname")
    public Result<List<String>> queryPnameList() {
        return Result.ok(service.queryPnameList());
    }
}
