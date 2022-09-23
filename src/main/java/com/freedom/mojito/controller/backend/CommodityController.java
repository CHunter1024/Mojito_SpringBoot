package com.freedom.mojito.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.CommodityDto;
import com.freedom.mojito.pojo.Commodity;
import com.freedom.mojito.service.CommodityService;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * <p>CreateTime: 2022-07-26 下午 11:06</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Api(tags = "商品相关API（管理端）")
@RestController("backend-commodityController")
@RequestMapping("/backend/commodity")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;


    @ApiOperation("添加商品")
    @PostMapping
    public Result<Object> saveCommodity(@RequestBody @Validated CommodityDto commodityDto, BindingResult validResults) throws IOException {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        commodityService.saveWithConfigs(commodityDto);

        return Result.succeed(null);
    }


    @ApiOperation(value = "分页+条件查询商品信息", notes = "条件：商品名称关键字（name）/ 商品分类ids（categoryIds）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, paramType = "query", dataTypeClass = Integer.class)
    })
    @GetMapping("/page")
    public Result<Page<Commodity>> getCommoditiesPage(Integer page, Integer pageSize, CommodityDto commodityDto) {
        Page<Commodity> pageInfo = commodityService.getPageInfo(page, pageSize, commodityDto);
        return Result.succeed(pageInfo);
    }


    @ApiOperation(value = "单量或批量删除商品", notes = "参数（json）：商品主键ids（ids）")
    @DeleteMapping
    public Result<Object> deleteCommodity(@RequestBody List<Long> ids) {
        commodityService.removeWithConfigs(ids);
        return Result.succeed(null);
    }


    @ApiOperation(value = "单量或批量修改商品状态", notes = "参数（json）：商品主键ids（ids）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "新状态", required = true, paramType = "path", dataTypeClass = Integer.class)
    })
    @PutMapping("/{status}")
    public Result<Object> updateCommodityStatus(@PathVariable("status") Integer status, @RequestBody List<Long> ids) {
        commodityService.updateStatusBatch(ids, status);
        return Result.succeed(null);
    }


    @ApiOperation("根据id查询商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @GetMapping("/{id}")
    public Result<CommodityDto> getCommodityById(@PathVariable("id") Long id) {
        CommodityDto commodityDto = commodityService.getWithConfigsById(id);

        return commodityDto != null ? Result.succeed(commodityDto) : Result.fail("参数有误");
    }


    @ApiOperation("修改商品信息")
    @PutMapping
    public Result<Object> updateCommodity(@RequestBody @Validated CommodityDto commodityDto, BindingResult validResults) throws IOException {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        commodityService.updateWithConfigs(commodityDto);

        return Result.succeed(null);
    }


    @ApiOperation(value = "根据条件查询商品信息", notes = "条件：商品分类id（categoryId）/ 商品名称关键字（name）/ 商品状态（status）")
    @GetMapping("/list")
    public Result<List<Commodity>> getCommodityList(Commodity commodity) {
        List<Commodity> commodityList = commodityService.getByCondition(commodity);
        return Result.succeed(commodityList);
    }
}
