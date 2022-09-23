package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.ComboDto;
import com.freedom.mojito.pojo.Combo;
import com.freedom.mojito.service.ComboService;
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

@Api(tags = "套餐相关API（客户端）")
@RestController("front-comboController")
@RequestMapping("/front/combo")
public class ComboController {

    @Autowired
    private ComboService comboService;


    @ApiOperation(value = "根据条件查询套餐信息", notes = "条件：套餐分类id（categoryId）/ 套餐状态（status）")
    @GetMapping("/list")
    public Result<List<ComboDto>> getCommodityList(Combo combo) {
        List<ComboDto> comboDtoList = comboService.getWithConfigsAndCommoditiesByCondition(combo);
        return Result.succeed(comboDtoList);
    }
}
