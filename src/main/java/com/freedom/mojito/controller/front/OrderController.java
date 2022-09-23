package com.freedom.mojito.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.OrderDto;
import com.freedom.mojito.pojo.Order;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.OrderService;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-08-17 上午 12:18</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Api(tags = "订单相关API（客户端）")
@RestController("front-orderController")
@RequestMapping("/front/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpSession session;


    @ApiOperation(value = "当前用户提交订单", notes = "请求处理成功返回订单主键")
    @PostMapping
    public Result<Long> addOrder(@RequestBody @Validated Order order, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }
        User user = (User) session.getAttribute("user");
        order.setUserId(user.getId());
        Long id = orderService.saveOrder(order);
        return id != null ? Result.succeed(id) : Result.fail("订单有误");
    }


    @ApiOperation("分页获取当前用户的订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, paramType = "query", dataTypeClass = Integer.class)
    })
    @GetMapping("/page")
    public Result<Page<OrderDto>> getOrdersPage(Integer page, Integer pageSize) {
        User user = (User) session.getAttribute("user");
        Page<OrderDto> pageInfo = orderService.getPageInfoByUserId(page, pageSize, user.getId());
        return Result.succeed(pageInfo);
    }


    @ApiOperation("当前用户再来一单")
    @PostMapping("/again")
    public Result<Object> addOrderAgain(@RequestBody Order order) {
        User user = (User) session.getAttribute("user");
        order.setUserId(user.getId());
        orderService.addAgain(order);
        return Result.succeed(null);
    }


    @ApiOperation("根据id获取订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @GetMapping("/{id}")
    public Result<OrderDto> getOrderById(@PathVariable("id") Long id) {
        OrderDto orderDto = orderService.getOrderDtoById(id);
        return orderDto != null ? Result.succeed(orderDto) : Result.fail("参数有误");
    }


    @ApiOperation("根据id删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @DeleteMapping("/{id}")
    public Result<Object> deleteOrderById(@PathVariable("id") Long id) {
        boolean result = orderService.removeOrderById(id);
        return result ? Result.succeed(null) : Result.fail("删除失败");
    }
}
