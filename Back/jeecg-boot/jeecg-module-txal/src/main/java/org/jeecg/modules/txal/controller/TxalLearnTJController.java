package org.jeecg.modules.txal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.txal.entity.TxalLearnTjData;
import org.jeecg.modules.txal.entity.TxalLearninfo;
import org.jeecg.modules.txal.service.ITxalLearnTjDataService;
import org.jeecg.modules.txal.service.ITxalLearninfoService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: wonshine
 * @Date: 2023/6/12 20:49
 * @Version: 1.0
 * @Content:
 */
@Api(tags = "学习统计")
@RestController
@RequestMapping("/txal/txalLearnTj")
@Slf4j
public class TxalLearnTJController extends JeecgController<TxalLearnTjData, ITxalLearnTjDataService> {

    @ApiOperation(value = "学习统计-按部门统计", notes = "学习统计-按部门统计")
    @PostMapping(value = "/org")
    public Result<List<TxalLearnTjData>> queryOrgTj(@RequestBody Map<String, Object> map) {
        return Result.OK(service.queryOrgTj(map));
    }

    @ApiOperation(value = "学习统计-按人员统计", notes = "学习统计-按人员统计")
    @PostMapping(value = "/user")
    public Result<List<TxalLearnTjData>> queryUserTj(@RequestBody Map<String, Object> map) {
        return Result.OK(service.queryUserTj(map));
    }

    @ApiOperation(value = "学习统计-按人员统计Top10", notes = "学习统计-按人员统计Top10")
    @PostMapping(value = "/user10")
    public Result<List<TxalLearnTjData>> queryUserTop10Tj(@RequestBody Map<String, Object> map) {
        return Result.OK(service.queryUserTjTop10(map));
    }
}
