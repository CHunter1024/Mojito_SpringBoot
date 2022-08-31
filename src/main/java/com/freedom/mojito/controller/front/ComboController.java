package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.ComboDto;
import com.freedom.mojito.pojo.Combo;
import com.freedom.mojito.service.ComboService;
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

@RestController("front-comboController")
@RequestMapping("/front/combo")
public class ComboController {

    @Autowired
    private ComboService comboService;

    /**
     * 根据条件查询套餐信息
     *
     * @param commodity
     * @return
     */
    @GetMapping("/list")
    public Result<List<ComboDto>> getCommodityList(Combo combo) {
        List<ComboDto> comboDtoList = comboService.getWithConfigsAndCommoditiesByCondition(combo);
        return Result.succeed(comboDtoList);
    }
}
