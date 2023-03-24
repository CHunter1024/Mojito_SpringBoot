package com.freedom.mojito.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.ComboDto;
import com.freedom.mojito.pojo.Combo;
import com.freedom.mojito.service.ComboService;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-07-26 下午 11:06</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Api(tags = "套餐相关API（管理端）")
@RestController("backend-comboController")
@RequestMapping("/backend/combo")
public class ComboController {

    @Autowired
    private ComboService comboService;


    @ApiOperation("添加套餐")
    @PostMapping
    public Result<Object> saveCombo(@RequestBody @Validated ComboDto comboDto, BindingResult validResults) throws IOException {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        comboService.saveWithCommoditiesAndConfigs(comboDto);

        return Result.succeed(null);
    }


    @ApiOperation(value = "分页+条件查询套餐信息", notes = "条件：套餐名称关键字（name）/ 套餐分类ids（categoryIds）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, paramType = "query", dataTypeClass = Integer.class)
    })
    @GetMapping("/page")
    public Result<Page<Combo>> getCommoditiesPage(Integer page, Integer pageSize, ComboDto comboDto) {
        Page<Combo> pageInfo = comboService.getPageInfo(page, pageSize, comboDto);
        return Result.succeed(pageInfo);
    }


    @ApiOperation(value = "单量或批量删除套餐", notes = "参数（json）：套餐主键ids（ids）")
    @DeleteMapping
    public Result<Object> deleteCombo(@RequestBody List<Long> ids) {
        comboService.removeWithCommoditiesAndConfigs(ids);
        return Result.succeed(null);
    }


    @ApiOperation(value = "单量或批量修改套餐状态", notes = "参数（json）：套餐主键ids（ids）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "新状态", required = true, paramType = "path", dataTypeClass = Integer.class)
    })
    @PutMapping("/{status}")
    public Result<Object> updateComboStatus(@PathVariable("status") Integer status, @RequestBody List<Long> ids) {
        comboService.updateStatusBatch(ids, status);
        return Result.succeed(null);
    }


    @ApiOperation("根据id查询套餐信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @GetMapping("/{id}")
    public Result<ComboDto> getComboById(@PathVariable("id") Long id) {
        ComboDto comboDto = comboService.getWithCommoditiesAndConfigsById(id);

        return comboDto != null ? Result.succeed(comboDto) : Result.fail("参数有误");
    }


    @ApiOperation("修改套餐信息")
    @PutMapping
    public Result<Object> updateCombo(@RequestBody @Validated ComboDto comboDto, BindingResult validResults) throws IOException {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        comboService.updateWithCommoditiesAndConfigs(comboDto);

        return Result.succeed(null);
    }
}
