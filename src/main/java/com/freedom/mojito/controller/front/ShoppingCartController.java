package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.ShoppingCart;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.ShoppingCartService;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-08-07 下午 8:41</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Api(tags = "用户购物车相关API")
@RestController
@RequestMapping("/front/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private HttpSession session;


    @ApiOperation("将商品添加到购物车中")
    @PostMapping
    public Result<Object> addShoppingCart(@RequestBody @Validated ShoppingCart shoppingCart, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }
        User user = (User) session.getAttribute("user");
        shoppingCart.setUserId(user.getId());
        shoppingCartService.add(shoppingCart);
        return Result.succeed(null);
    }


    @ApiOperation("查询购物车中的商品信息")
    @GetMapping
    public Result<List<ShoppingCart>> getShoppingCart() {
        User user = (User) session.getAttribute("user");
        List<ShoppingCart> carts = shoppingCartService.getByUserId(user.getId());
        return Result.succeed(carts);
    }


    @ApiOperation("清空购物车中的商品")
    @DeleteMapping("/all")
    public Result<Object> clearShoppingCart() {
        User user = (User) session.getAttribute("user");
        shoppingCartService.removeByUserId(user.getId());
        return Result.succeed(null);
    }


    @ApiOperation("修改购物车中指定商品的数量")
    @PutMapping
    public Result<Object> updateShoppingCart(@RequestBody @Validated ShoppingCart shoppingCart, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }
        shoppingCartService.updateCartNumber(shoppingCart);
        return Result.succeed(null);
    }
}
