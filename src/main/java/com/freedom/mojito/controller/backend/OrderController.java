package com.freedom.mojito.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.OrderDto;
import com.freedom.mojito.pojo.Order;
import com.freedom.mojito.service.OrderService;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-08-22 下午 11:39</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Api(tags = "订单相关API（管理端）")
@RestController("backend-orderController")
@RequestMapping("/backend/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @ApiOperation("分页+条件查询订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "number", value = "订单号", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "beginTime", value = "开始时间", paramType = "query", dataTypeClass = LocalDateTime.class),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataTypeClass = LocalDateTime.class)
    })
    @GetMapping("/page")
    public Result<Page<OrderDto>> getOrderPage(Integer page, Integer pageSize, String number,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Page<OrderDto> pageInfo = orderService.getPageInfo(page, pageSize, number, beginTime, endTime);
        return Result.succeed(pageInfo);
    }


    @ApiOperation("修改订单信息")
    @PutMapping
    public Result<Object> updateOrder(@RequestBody @Validated Order order, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }
        orderService.updateOrder(order);
        return Result.succeed(null);
    }
}
