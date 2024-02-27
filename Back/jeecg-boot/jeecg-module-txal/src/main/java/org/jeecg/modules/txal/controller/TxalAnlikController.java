package org.jeecg.modules.txal.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.txal.entity.TxalAnlik;
import org.jeecg.modules.txal.service.ITxalAnlikService;

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
 * @Description: 案例库分类
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Api(tags="案例库分类")
@RestController
@RequestMapping("/txal/txalAnlik")
@Slf4j
public class TxalAnlikController extends JeecgController<TxalAnlik, ITxalAnlikService> {
	@Autowired
	private ITxalAnlikService txalAnlikService;
	
	/**
	 * 分页列表查询
	 *
	 * @param txalAnlik
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "案例库分类-分页列表查询")
	@ApiOperation(value="案例库分类-分页列表查询", notes="案例库分类-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TxalAnlik>> queryPageList(TxalAnlik txalAnlik,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TxalAnlik> queryWrapper = QueryGenerator.initQueryWrapper(txalAnlik, req.getParameterMap());
		Page<TxalAnlik> page = new Page<TxalAnlik>(pageNo, pageSize);
		IPage<TxalAnlik> pageList = txalAnlikService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param txalAnlik
	 * @return
	 */
	@AutoLog(value = "案例库分类-添加")
	@ApiOperation(value="案例库分类-添加", notes="案例库分类-添加")
	@RequiresPermissions("txal:txal_anlik:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TxalAnlik txalAnlik) {
		txalAnlikService.save(txalAnlik);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param txalAnlik
	 * @return
	 */
	@AutoLog(value = "案例库分类-编辑")
	@ApiOperation(value="案例库分类-编辑", notes="案例库分类-编辑")
	@RequiresPermissions("txal:txal_anlik:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TxalAnlik txalAnlik) {
		txalAnlikService.updateById(txalAnlik);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "案例库分类-通过id删除")
	@ApiOperation(value="案例库分类-通过id删除", notes="案例库分类-通过id删除")
	@RequiresPermissions("txal:txal_anlik:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		txalAnlikService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "案例库分类-批量删除")
	@ApiOperation(value="案例库分类-批量删除", notes="案例库分类-批量删除")
	@RequiresPermissions("txal:txal_anlik:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.txalAnlikService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "案例库分类-通过id查询")
	@ApiOperation(value="案例库分类-通过id查询", notes="案例库分类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TxalAnlik> queryById(@RequestParam(name="id",required=true) String id) {
		TxalAnlik txalAnlik = txalAnlikService.getById(id);
		if(txalAnlik==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(txalAnlik);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param txalAnlik
    */
    @RequiresPermissions("txal:txal_anlik:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TxalAnlik txalAnlik) {
        return super.exportXls(request, txalAnlik, TxalAnlik.class, "案例库分类");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("txal:txal_anlik:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TxalAnlik.class);
    }

}
