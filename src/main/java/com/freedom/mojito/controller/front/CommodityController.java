package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.CommodityDto;
import com.freedom.mojito.pojo.Commodity;
import com.freedom.mojito.service.CommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-08-06 下午 1:42</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Api(tags = "商品相关API（客户端）")
@RestController("front-commodityController")
@RequestMapping("/front/commodity")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;


    @ApiOperation(value = "根据条件查询商品信息", notes = "条件：商品分类id（categoryId）/ 商品名称关键字（name）/ 商品状态（status）")
    @GetMapping("/list")
    public Result<List<CommodityDto>> getCommodityList(Commodity commodity) {
        List<CommodityDto> commodityDtoList = commodityService.getWithConfigsByCondition(commodity);
        return Result.succeed(commodityDtoList);
    }
}
