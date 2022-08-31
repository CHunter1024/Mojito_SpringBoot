package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.pojo.ShoppingCart;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.ComboService;
import com.freedom.mojito.service.CommodityService;
import com.freedom.mojito.service.ShoppingCartService;
import com.freedom.mojito.mapper.ShoppingCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 针对表【shopping_cart(购物车)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-12 下午 2:12</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private CommodityService commodityService;
    @Autowired
    private ComboService comboService;

    @Override
    public void add(ShoppingCart shoppingCart) {
        // 查询价格
        BigDecimal price = shoppingCart.getType() == 0 ? commodityService.getPriceById(shoppingCart.getGoodsId())
                : comboService.getPriceById(shoppingCart.getGoodsId());

        // 查询当前用户的购物车里是否有这条记录
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(ShoppingCart::getGoodsId, shoppingCart.getGoodsId())
                .eq(ShoppingCart::getType, shoppingCart.getType())
                .eq(ShoppingCart::getConfig, shoppingCart.getConfig())
                .last("LIMIT 1");
        ShoppingCart cart = getOne(wrapper);

        // 如果有这条记录则更新，没有则插入
        if (cart == null) {
            shoppingCart.setAmount(price);
            save(shoppingCart);
        } else {
            cart.setNumber(cart.getNumber() + 1);
            cart.setAmount(price.multiply(new BigDecimal(cart.getNumber())));
            updateById(cart);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCart> getByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId).orderByAsc(ShoppingCart::getCreateTime);
        return list(wrapper);
    }

    @Override
    public void removeByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        remove(wrapper);
    }

    @Override
    public void updateCartNumber(ShoppingCart shoppingCart) {
        // 如果指定商品的数量小于1，则删除这条记录，反之则更新
        if (shoppingCart.getNumber() < 1) {
            removeById(shoppingCart.getId());
        } else {
            // 查询该商品的价格
            BigDecimal price = shoppingCart.getType() == 0 ? commodityService.getPriceById(shoppingCart.getGoodsId())
                    : comboService.getPriceById(shoppingCart.getGoodsId());
            // 更改金额
            shoppingCart.setAmount(price.multiply(new BigDecimal(shoppingCart.getNumber())));
            updateById(shoppingCart);
        }
    }
}




