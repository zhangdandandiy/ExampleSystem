package org.jeecg.modules.txal.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.txal.entity.TxalTxjobd;
import org.jeecg.modules.txal.service.ITxalTxjobdService;

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
 * @Description: 体系作业明细
 * @Author: jeecg-boot
 * @Date:   2023-07-07
 * @Version: V1.0
 */
@Api(tags="体系作业明细")
@RestController
@RequestMapping("/txal/txalTxjobd")
@Slf4j
public class TxalTxjobdController extends JeecgController<TxalTxjobd, ITxalTxjobdService> {
	@Autowired
	private ITxalTxjobdService txalTxjobdService;

	 /**
	  *
	  * @param fid
	  * @return
	  */
	//@AutoLog(value = "体系作业明细-分页列表查询")
	@ApiOperation(value="体系作业明细", notes="体系作业明细")
	@GetMapping(value = "/{fid}")
	public Result<List<TxalTxjobd>> queryPageList(@PathVariable("fid")String fid) {
		QueryWrapper<TxalTxjobd> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("fid",fid);
		queryWrapper.orderByAsc("code");

		return Result.OK(txalTxjobdService.list(queryWrapper));
	}
	
	/**
	 *   添加
	 *
	 * @param txalTxjobd
	 * @return
	 */
	@AutoLog(value = "体系作业明细-添加")
	@ApiOperation(value="体系作业明细-添加", notes="体系作业明细-添加")
	@RequiresPermissions("txal:txal_txjobd:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TxalTxjobd txalTxjobd) {
		txalTxjobdService.save(txalTxjobd);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param txalTxjobd
	 * @return
	 */
	@AutoLog(value = "体系作业明细-编辑")
	@ApiOperation(value="体系作业明细-编辑", notes="体系作业明细-编辑")
	@RequiresPermissions("txal:txal_txjobd:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TxalTxjobd txalTxjobd) {
		txalTxjobdService.updateById(txalTxjobd);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "体系作业明细-通过id删除")
	@ApiOperation(value="体系作业明细-通过id删除", notes="体系作业明细-通过id删除")
	@RequiresPermissions("txal:txal_txjobd:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		txalTxjobdService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "体系作业明细-批量删除")
	@ApiOperation(value="体系作业明细-批量删除", notes="体系作业明细-批量删除")
	@RequiresPermissions("txal:txal_txjobd:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.txalTxjobdService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "体系作业明细-通过id查询")
	@ApiOperation(value="体系作业明细-通过id查询", notes="体系作业明细-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TxalTxjobd> queryById(@RequestParam(name="id",required=true) String id) {
		TxalTxjobd txalTxjobd = txalTxjobdService.getById(id);
		if(txalTxjobd==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(txalTxjobd);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param txalTxjobd
    */
    @RequiresPermissions("txal:txal_txjobd:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TxalTxjobd txalTxjobd) {
        return super.exportXls(request, txalTxjobd, TxalTxjobd.class, "体系作业明细");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("txal:txal_txjobd:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TxalTxjobd.class);
    }

}
