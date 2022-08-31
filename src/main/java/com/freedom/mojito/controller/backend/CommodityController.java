package com.freedom.mojito.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.CommodityDto;
import com.freedom.mojito.pojo.Commodity;
import com.freedom.mojito.service.CommodityService;
import com.freedom.mojito.util.ValidateData;
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

@RestController("backend-commodityController")
@RequestMapping("/backend/commodity")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    /**
     * 保存商品信息
     *
     * @param commodityDto
     * @param validResults
     * @return
     * @throws IOException
     */
    @PostMapping
    public Result<Object> saveCommodity(@RequestBody @Validated CommodityDto commodityDto, BindingResult validResults) throws IOException {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        commodityService.saveWithConfigs(commodityDto);

        return Result.succeed(null);
    }

    /**
     * 分页 + 条件 查询商品信息
     *
     * @param page
     * @param pageSize
     * @param commodityDto
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Commodity>> getCommoditiesPage(Integer page, Integer pageSize, CommodityDto commodityDto) {
        Page<Commodity> pageInfo = commodityService.getPageInfo(page, pageSize, commodityDto);
        return Result.succeed(pageInfo);
    }

    /**
     * 单量或批量删除商品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<Object> deleteCommodity(@RequestBody List<Long> ids) {
        commodityService.removeWithConfigs(ids);
        return Result.succeed(null);
    }

    /**
     * 单量或批量修改商品状态
     *
     * @param status
     * @param ids
     * @return
     */
    @PutMapping("/{status}")
    public Result<Object> updateCommodityStatus(@PathVariable("status") Integer status, @RequestBody List<Long> ids) {
        // stream 流处理参数
        List<Commodity> commodityList = ids.stream().map(id -> {
            Commodity commodity = new Commodity();
            commodity.setId(id);
            commodity.setStatus(status);
            return commodity;
        }).collect(Collectors.toList());
        boolean result = commodityService.updateBatchById(commodityList);

        return result ? Result.succeed(null) : Result.fail("参数有误");
    }

    /**
     * 根据商品id查询商品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<CommodityDto> getCommodityById(@PathVariable("id") Long id) {
        CommodityDto commodityDto = commodityService.getWithConfigsById(id);

        return commodityDto != null ? Result.succeed(commodityDto) : Result.fail("参数有误");
    }


    /**
     * 修改商品信息
     *
     * @param commodityDto
     * @param validResults
     * @return
     * @throws IOException
     */
    @PutMapping
    public Result<Object> updateCommodity(@RequestBody @Validated CommodityDto commodityDto, BindingResult validResults) throws IOException {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        commodityService.updateWithConfigs(commodityDto);

        return Result.succeed(null);
    }

    /**
     * 根据条件查询商品信息
     *
     * @param commodity
     * @return
     */
    @GetMapping("/list")
    public Result<List<Commodity>> getCommodityList(Commodity commodity) {
        List<Commodity> commodityList = commodityService.getByCondition(commodity);
        return Result.succeed(commodityList);
    }
}
