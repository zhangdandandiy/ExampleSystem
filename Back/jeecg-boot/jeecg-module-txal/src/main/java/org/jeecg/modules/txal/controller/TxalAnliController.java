package org.jeecg.modules.txal.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.txal.entity.TxalAnli;
import org.jeecg.modules.txal.entity.TxalDoclink;
import org.jeecg.modules.txal.service.ITxalAnliService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @Description: 案例
 * @Author: jeecg-boot
 * @Date: 2023-06-09
 * @Version: V1.0
 */
@Api(tags = "案例")
@RestController
@RequestMapping("/txal/txalAnli")
@Slf4j
public class TxalAnliController extends JeecgController<TxalAnli, ITxalAnliService> {
    @Autowired
    private ITxalAnliService txalAnliService;

    /**
     * 分页列表查询
     *
     * @param txalAnli
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "案例-分页列表查询")
    @ApiOperation(value = "案例-分页列表查询", notes = "案例-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<TxalAnli>> queryPageList(TxalAnli txalAnli,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        QueryWrapper<TxalAnli> queryWrapper = QueryGenerator.initQueryWrapper(txalAnli, req.getParameterMap());
        Page<TxalAnli> page = new Page<TxalAnli>(pageNo, pageSize);
        IPage<TxalAnli> pageList = txalAnliService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param txalAnli
     * @return
     */
    @AutoLog(value = "案例-添加")
    @ApiOperation(value = "案例-添加", notes = "案例-添加")
    @RequiresPermissions("txal:txal_anli:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestParam(value = "data") String data,
                              @RequestParam(value = "docinfo", required = false) String docinfo,
                              @RequestPart(value = "files[]", required = false) MultipartFile[] files) throws InvocationTargetException,
            IllegalAccessException {
        TxalAnli anli = JSONObject.toJavaObject(JSON.parseObject(data), TxalAnli.class);
        TxalDoclink[] doclink = JSON.parseArray(docinfo).toJavaObject(TxalDoclink[].class);
        if(txalAnliService.save(anli, doclink, files)) {
            return Result.OK("添加成功！");
        }else{
            return Result.error("添加失败！\r\n案例名称已经存在");
        }
    }

    /**
     * 编辑
     *
     * @param txalAnli
     * @return
     */
    @AutoLog(value = "案例-编辑")
    @ApiOperation(value = "案例-编辑", notes = "案例-编辑")
    @RequiresPermissions("txal:txal_anli:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestParam(value = "data") String data,
                               @RequestParam(value = "docinfo", required = false) String docinfo,
                               @RequestPart(value = "files[]", required = false) MultipartFile[] files) {
        TxalAnli anli = JSONObject.toJavaObject(JSON.parseObject(data), TxalAnli.class);
        TxalDoclink[] doclink = JSON.parseArray(docinfo).toJavaObject(TxalDoclink[].class);
        if(txalAnliService.updateById(anli,doclink,files)) {
            return Result.OK("编辑成功!");
        }else{
            return Result.error("编辑失败！\r\n案例名称已经存在");
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "案例-通过id删除")
    @ApiOperation(value = "案例-通过id删除", notes = "案例-通过id删除")
    @RequiresPermissions("txal:txal_anli:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        txalAnliService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "案例-批量删除")
    @ApiOperation(value = "案例-批量删除", notes = "案例-批量删除")
    @RequiresPermissions("txal:txal_anli:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.txalAnliService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "案例-通过id查询")
    @ApiOperation(value = "案例-通过id查询", notes = "案例-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<TxalAnli> queryById(@RequestParam(name = "id", required = true) String id) {
        TxalAnli txalAnli = txalAnliService.getById(id);
        if (txalAnli == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(txalAnli);
    }

    @GetMapping(value = "/queryExists")
    public Result<Boolean> queryById(@RequestParam(name = "id", required = false) String id,
                                     @RequestParam("name") String name) {
        return Result.OK(service.nameExists(name,id));
    }



    /**
     * 导出excel
     *
     * @param request
     * @param txalAnli
     */
    @RequiresPermissions("txal:txal_anli:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TxalAnli txalAnli) {
        return super.exportXls(request, txalAnli, TxalAnli.class, "案例");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("txal:txal_anli:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TxalAnli.class);
    }


    @ApiOperation(value = "案例-获取新code", notes = "案例-获取新code")
    @GetMapping(value = "/{pcode}/{code}")
    public Result<String> newAnliCode(@PathVariable("pcode") String pcode, @PathVariable("code") String code) {
        return Result.OK(null, service.newAnliCode(pcode, code));
    }

}
