package org.jeecg.modules.txal.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.txal.entity.TxalContextype;
import org.jeecg.modules.txal.service.ITxalContextypeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 内容分类
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Api(tags="内容分类")
@RestController
@RequestMapping("/txal/txalContextype")
@Slf4j
public class TxalContextypeController extends JeecgController<TxalContextype, ITxalContextypeService> {
	@Autowired
	private ITxalContextypeService txalContextypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param txalContextype
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "内容分类-分页列表查询")
	@ApiOperation(value="内容分类-分页列表查询", notes="内容分类-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TxalContextype>> queryPageList(TxalContextype txalContextype,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TxalContextype> queryWrapper = QueryGenerator.initQueryWrapper(txalContextype, req.getParameterMap());
		Page<TxalContextype> page = new Page<TxalContextype>(pageNo, pageSize);
		IPage<TxalContextype> pageList = txalContextypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param txalContextype
	 * @return
	 */
	@AutoLog(value = "内容分类-添加")
	@ApiOperation(value="内容分类-添加", notes="内容分类-添加")
	@RequiresPermissions("txal:txal_contextype:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TxalContextype txalContextype) {
		txalContextypeService.save(txalContextype);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param txalContextype
	 * @return
	 */
	@AutoLog(value = "内容分类-编辑")
	@ApiOperation(value="内容分类-编辑", notes="内容分类-编辑")
	@RequiresPermissions("txal:txal_contextype:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TxalContextype txalContextype) {
		txalContextypeService.updateById(txalContextype);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "内容分类-通过id删除")
	@ApiOperation(value="内容分类-通过id删除", notes="内容分类-通过id删除")
	@RequiresPermissions("txal:txal_contextype:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		txalContextypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "内容分类-批量删除")
	@ApiOperation(value="内容分类-批量删除", notes="内容分类-批量删除")
	@RequiresPermissions("txal:txal_contextype:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.txalContextypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "内容分类-通过id查询")
	@ApiOperation(value="内容分类-通过id查询", notes="内容分类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TxalContextype> queryById(@RequestParam(name="id",required=true) String id) {
		TxalContextype txalContextype = txalContextypeService.getById(id);
		if(txalContextype==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(txalContextype);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param txalContextype
    */
    @RequiresPermissions("txal:txal_contextype:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TxalContextype txalContextype) {
        return super.exportXls(request, txalContextype, TxalContextype.class, "内容分类");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("txal:txal_contextype:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TxalContextype.class);
    }

}
