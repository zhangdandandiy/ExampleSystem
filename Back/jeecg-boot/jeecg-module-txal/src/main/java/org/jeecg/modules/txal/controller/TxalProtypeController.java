package org.jeecg.modules.txal.controller;

import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.txal.entity.TxalProtype;
import org.jeecg.modules.txal.service.ITxalProtypeService;

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
 * @Description: 产品类型
 * @Author: jeecg-boot
 * @Date:   2023-06-06
 * @Version: V1.0
 */
@Api(tags="产品类型")
@RestController
@RequestMapping("/txal/txalProtype")
@Slf4j
public class TxalProtypeController extends JeecgController<TxalProtype, ITxalProtypeService> {
	@Autowired
	private ITxalProtypeService txalProtypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param txalProtype
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "产品类型-分页列表查询")
	@ApiOperation(value="产品类型-分页列表查询", notes="产品类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TxalProtype>> queryPageList(TxalProtype txalProtype,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TxalProtype> queryWrapper = QueryGenerator.initQueryWrapper(txalProtype, req.getParameterMap());
		Page<TxalProtype> page = new Page<TxalProtype>(pageNo, pageSize);
		page.setOrders(new ArrayList<OrderItem>(){{add(new OrderItem("code",true));}});
		IPage<TxalProtype> pageList = txalProtypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param txalProtype
	 * @return
	 */
	@AutoLog(value = "产品类型-添加")
	@ApiOperation(value="产品类型-添加", notes="产品类型-添加")
	@RequiresPermissions("txal:txal_protype:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TxalProtype txalProtype) {
		txalProtypeService.save(txalProtype);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param txalProtype
	 * @return
	 */
	@AutoLog(value = "产品类型-编辑")
	@ApiOperation(value="产品类型-编辑", notes="产品类型-编辑")
	@RequiresPermissions("txal:txal_protype:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TxalProtype txalProtype) {
		txalProtypeService.updateById(txalProtype);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品类型-通过id删除")
	@ApiOperation(value="产品类型-通过id删除", notes="产品类型-通过id删除")
	@RequiresPermissions("txal:txal_protype:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		txalProtypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "产品类型-批量删除")
	@ApiOperation(value="产品类型-批量删除", notes="产品类型-批量删除")
	@RequiresPermissions("txal:txal_protype:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.txalProtypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "产品类型-通过id查询")
	@ApiOperation(value="产品类型-通过id查询", notes="产品类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TxalProtype> queryById(@RequestParam(name="id",required=true) String id) {
		TxalProtype txalProtype = txalProtypeService.getById(id);
		if(txalProtype==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(txalProtype);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param txalProtype
    */
    @RequiresPermissions("txal:txal_protype:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TxalProtype txalProtype) {
        return super.exportXls(request, txalProtype, TxalProtype.class, "产品类型");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("txal:txal_protype:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TxalProtype.class);
    }

}
