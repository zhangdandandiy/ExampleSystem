package org.jeecg.modules.txal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.txal.entity.TxalDoclink;
import org.jeecg.modules.txal.service.ITxalDoclinkService;
import org.jodconverter.core.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 附件表
 * @Author: jeecg-boot
 * @Date: 2023-06-11
 * @Version: V1.0
 */
@Api(tags = "附件表")
@RestController
@RequestMapping("/txal/txalDoclink")
@Slf4j
public class TxalDoclinkController extends JeecgController<TxalDoclink, ITxalDoclinkService> {
    @Autowired
    private ITxalDoclinkService txalDoclinkService;

    /**
     * 分页列表查询
     *
     * @param txalDoclink
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "附件表-分页列表查询")
    @ApiOperation(value = "附件表-分页列表查询", notes = "附件表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<TxalDoclink>> queryPageList(TxalDoclink txalDoclink,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<TxalDoclink> queryWrapper = QueryGenerator.initQueryWrapper(txalDoclink, req.getParameterMap());
        Page<TxalDoclink> page = new Page<TxalDoclink>(pageNo, pageSize);
        IPage<TxalDoclink> pageList = txalDoclinkService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param txalDoclink
     * @return
     */
    @AutoLog(value = "附件表-添加")
    @ApiOperation(value = "附件表-添加", notes = "附件表-添加")
    @RequiresPermissions("txal:txal_doclink:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody TxalDoclink txalDoclink) {
        txalDoclinkService.save(txalDoclink);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param txalDoclink
     * @return
     */
    @AutoLog(value = "附件表-编辑")
    @ApiOperation(value = "附件表-编辑", notes = "附件表-编辑")
    @RequiresPermissions("txal:txal_doclink:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody TxalDoclink txalDoclink) {
        txalDoclinkService.updateById(txalDoclink);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "附件表-通过id删除")
    @ApiOperation(value = "附件表-通过id删除", notes = "附件表-通过id删除")
    @RequiresPermissions("txal:txal_doclink:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        txalDoclinkService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "附件表-批量删除")
    @ApiOperation(value = "附件表-批量删除", notes = "附件表-批量删除")
    @RequiresPermissions("txal:txal_doclink:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.txalDoclinkService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "附件表-通过id查询")
    @ApiOperation(value = "附件表-通过id查询", notes = "附件表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<TxalDoclink> queryById(@RequestParam(name = "id", required = true) String id) {
        TxalDoclink txalDoclink = txalDoclinkService.getById(id);
        if (txalDoclink == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(txalDoclink);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param txalDoclink
     */
    @RequiresPermissions("txal:txal_doclink:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TxalDoclink txalDoclink) {
        return super.exportXls(request, txalDoclink, TxalDoclink.class, "附件表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("txal:txal_doclink:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TxalDoclink.class);
    }

    @ApiOperation(value = "附件表-通过业务表查询", notes = "附件表-通过业务表查询")
    @GetMapping(value = "/query/{linktable}")
    public Result<List<TxalDoclink>> queryByLinks(@PathVariable("linktable") String linktable,
                                                  @RequestParam(name = "ids", required = true) String ids) {
        QueryWrapper<TxalDoclink> map=new QueryWrapper<>();
        map.eq("link_table", linktable);
        map.in("link_id", Arrays.stream(ids.split(",")).collect(Collectors.toList()));
        return Result.OK(service.list(map));
    }


    @ApiOperation(value = "附件表-通过业务表查询", notes = "附件表-通过业务表查询")
    @GetMapping(value = "/query/{linktable}/{linkid}")
    public Result<List<TxalDoclink>> queryByLink(@PathVariable("linktable") String linktable,
                                                 @PathVariable("linkid") String linkid) {
        return Result.OK(service.listByMap(new HashMap<String, Object>() {{
            put("link_table", linktable);
            put("link_id", linkid);
        }}));
    }

    @ApiOperation(value = "附件表-下载文件", notes = "附件表-下载文件")
    @GetMapping(value = "/download/{id}")
    public void download(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("charset", "utf-8");
        try (OutputStream os = response.getOutputStream()) {
            String fname = service.download(os, id);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fname + "\"");
        }
    }

    @ApiOperation(value = "附件表-下载文件PDF", notes = "附件表-下载文件PDF")
    @GetMapping(value = "/download/pdf/{id}")
    public void downloadPdf(@PathVariable("id") String id, HttpServletResponse response) throws IOException, OfficeException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("charset", "utf-8");
        try (OutputStream os = response.getOutputStream()) {
            String fname = service.downloadAsPdf(os, id);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fname + "\"");
        }
    }
}
