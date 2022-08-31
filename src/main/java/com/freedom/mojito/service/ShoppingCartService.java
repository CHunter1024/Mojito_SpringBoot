package com.freedom.mojito.service;

import com.freedom.mojito.pojo.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.freedom.mojito.pojo.User;

import java.util.List;

/**
 * Description: 针对表【shopping_cart(购物车)】的数据库操作Service
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加购物车业务
     *
     * @param shoppingCart
     */
    void add(ShoppingCart shoppingCart);

    /**
     * 根据用户Id查询购物车信息
     *
     * @param userId 用户id
     * @return 购物车信息
     */
    List<ShoppingCart> getByUserId(Long userId);

    /**
     * 根据用户id删除购物车信息
     *
     * @param userId 用户id
     */
    void removeByUserId(Long userId);

    /**
     * 更新购物车指定商品的数量
     *
     * @param shoppingCart
     */
    void updateCartNumber(ShoppingCart shoppingCart);
}
