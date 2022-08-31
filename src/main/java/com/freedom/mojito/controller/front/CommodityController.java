package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.CommodityDto;
import com.freedom.mojito.pojo.Commodity;
import com.freedom.mojito.service.CommodityService;
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

@RestController("front-commodityController")
@RequestMapping("/front/commodity")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    /**
     * 根据条件查询商品信息
     *
     * @param commodity
     * @return
     */
    @GetMapping("/list")
    public Result<List<CommodityDto>> getCommodityList(Commodity commodity) {
        List<CommodityDto> commodityDtoList = commodityService.getWithConfigsByCondition(commodity);
        return Result.succeed(commodityDtoList);
    }
}
