package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.ShoppingCart;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.ShoppingCartService;
import com.freedom.mojito.util.ValidateData;
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

@RestController
@RequestMapping("/front/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 将商品添加到购物车中
     *
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping
    public Result<Object> addCart(@RequestBody @Validated ShoppingCart shoppingCart, BindingResult validResults, HttpSession session) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }
        User user = (User) session.getAttribute("user");
        shoppingCart.setUserId(user.getId());
        shoppingCartService.add(shoppingCart);
        return Result.succeed(null);
    }

    /**
     * 获取购物车中的商品信息
     *
     * @param session
     * @return
     */
    @GetMapping
    public Result<List<ShoppingCart>> getCart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<ShoppingCart> carts = shoppingCartService.getByUserId(user.getId());
        return Result.succeed(carts);
    }

    /**
     * 删除购物车中的商品
     *
     * @param session
     * @return
     */
    @DeleteMapping
    public Result<Object> clearCart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        shoppingCartService.removeByUserId(user.getId());
        return Result.succeed(null);
    }

    /**
     * 修改购物车中指定商品的数量
     *
     * @param shoppingCart
     * @return
     */
    @PutMapping
    public Result<Object> updateCart(@RequestBody @Validated ShoppingCart shoppingCart, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }
        shoppingCartService.updateCartNumber(shoppingCart);
        return Result.succeed(null);
    }
}
