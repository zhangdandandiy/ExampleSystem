package org.jeecg.modules.txal.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.txal.entity.TxalLearninfo;
import org.jeecg.modules.txal.service.ITxalLearninfoService;

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
 * @Description: 学习情况
 * @Author: jeecg-boot
 * @Date:   2023-06-11
 * @Version: V1.0
 */
@Api(tags="学习情况")
@RestController
@RequestMapping("/txal/txalLearninfo")
@Slf4j
public class TxalLearninfoController extends JeecgController<TxalLearninfo, ITxalLearninfoService> {
	@Autowired
	private ITxalLearninfoService txalLearninfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param txalLearninfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "学习情况-分页列表查询")
	@ApiOperation(value="学习情况-分页列表查询", notes="学习情况-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TxalLearninfo>> queryPageList(TxalLearninfo txalLearninfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TxalLearninfo> queryWrapper = QueryGenerator.initQueryWrapper(txalLearninfo, req.getParameterMap());
		Page<TxalLearninfo> page = new Page<TxalLearninfo>(pageNo, pageSize);
		IPage<TxalLearninfo> pageList = txalLearninfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param txalLearninfo
	 * @return
	 */
	@AutoLog(value = "学习情况-添加")
	@ApiOperation(value="学习情况-添加", notes="学习情况-添加")
//	@RequiresPermissions("txal:txal_learninfo:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TxalLearninfo txalLearninfo) {
		txalLearninfoService.save(txalLearninfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param txalLearninfo
	 * @return
	 */
	@AutoLog(value = "学习情况-编辑")
	@ApiOperation(value="学习情况-编辑", notes="学习情况-编辑")
	@RequiresPermissions("txal:txal_learninfo:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TxalLearninfo txalLearninfo) {
		txalLearninfoService.updateById(txalLearninfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学习情况-通过id删除")
	@ApiOperation(value="学习情况-通过id删除", notes="学习情况-通过id删除")
	@RequiresPermissions("txal:txal_learninfo:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		txalLearninfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "学习情况-批量删除")
	@ApiOperation(value="学习情况-批量删除", notes="学习情况-批量删除")
	@RequiresPermissions("txal:txal_learninfo:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.txalLearninfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "学习情况-通过id查询")
	@ApiOperation(value="学习情况-通过id查询", notes="学习情况-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TxalLearninfo> queryById(@RequestParam(name="id",required=true) String id) {
		TxalLearninfo txalLearninfo = txalLearninfoService.getById(id);
		if(txalLearninfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(txalLearninfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param txalLearninfo
    */
    @RequiresPermissions("txal:txal_learninfo:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TxalLearninfo txalLearninfo) {
        return super.exportXls(request, txalLearninfo, TxalLearninfo.class, "学习情况");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("txal:txal_learninfo:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TxalLearninfo.class);
    }

}
