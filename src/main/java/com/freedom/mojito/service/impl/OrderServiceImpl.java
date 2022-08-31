package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.dto.OrderDto;
import com.freedom.mojito.mapper.OrderMapper;
import com.freedom.mojito.pojo.AddressBook;
import com.freedom.mojito.pojo.Order;
import com.freedom.mojito.pojo.OrderDetail;
import com.freedom.mojito.pojo.ShoppingCart;
import com.freedom.mojito.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Description: 针对表【order(订单信息)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-12 下午 2:12</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    // 由于这两个组件相互依赖，因此需要让两者依赖注入的时间错开
    // @Lazy：当需要调用AddressBookService的时候再进行注入
    @Autowired
    @Lazy
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ComboService comboService;

    @Override
    public Long saveOrder(Order order) {
        // 查询当前用户购物车数据
        List<ShoppingCart> shoppingCarts = shoppingCartService.getByUserId(order.getUserId());
        if (CollectionUtils.isEmpty(shoppingCarts)) {
            return null;
        }
        // 查询下单地址是否存在
        AddressBook address = addressBookService.getById(order.getAddressBookId());
        if (address == null) {
            return null;
        }

        // 生成订单号
        order.setNumber(IdWorker.getIdStr());
        // 生成订单金额（使用AtomicReference保证BigDecimal在多线程情况下计算的准确性和安全性）
        // AtomicReference：对象进行原子操作，提供了一种读和写都是原子性的对象引用变量
        AtomicReference<BigDecimal> amount = new AtomicReference<>(new BigDecimal(0));
        BinaryOperator<BigDecimal> add = BigDecimal::add;
        shoppingCarts.forEach(cart -> amount.accumulateAndGet(cart.getAmount(), add));
        amount.accumulateAndGet(new BigDecimal(3), add);  // 配送费3元
        order.setAmount(amount.get());
        // 未引入支付接口，因此这里设置订单状态为已支付，支付时间为现在
        order.setStatus(2);
        order.setOrderTime(LocalDateTime.now());
        order.setPayTime(LocalDateTime.now());
        save(order);

        // 生成订单明细数据
        List<OrderDetail> details = shoppingCarts.stream().map(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail, "id", "userId", "createTime");
            orderDetail.setOrderId(order.getId());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(details);

        // 购物车中的商品增加销量（待优化，可能存在并发问题）
        shoppingCarts.forEach(cart -> {
            if (cart.getType() == 0) {
                commodityService.incrSalesById(cart.getGoodsId(), cart.getNumber());
            } else {
                comboService.incrSalesById(cart.getGoodsId(), cart.getNumber());
            }
        });

        // 清空用户购物车
        shoppingCartService.removeByUserId(order.getUserId());

        return order.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getPageInfoByUserId(Integer page, Integer pageSize, Long userId) {
        Page<Order> orderPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId).orderByDesc(Order::getOrderTime).eq(Order::getUserIsDeleted, 0);
        page(orderPage, wrapper);

        // 获取OrderDto的分页数据（包含订单明细）
        List<Order> orderList = orderPage.getRecords();
        List<OrderDto> orderDtoList = orderList.stream().map(this::buildDetailsDto).collect(Collectors.toList());
        Page<OrderDto> orderDtoPage = new Page<>();
        BeanUtils.copyProperties(orderPage, orderDtoPage, "records");
        orderDtoPage.setRecords(orderDtoList);

        return orderDtoPage;
    }

    @Override
    public void addAgain(Order order) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, order.getId());
        List<OrderDetail> details = orderDetailService.list(wrapper);

        // 将订单明细中的商品添加到用户的购物车中，此前先清除购物车
        shoppingCartService.removeByUserId(order.getUserId());
        List<ShoppingCart> shoppingCarts = details.stream().map(orderDetail -> {
            ShoppingCart cart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, cart, "id", "orderId");
            cart.setUserId(order.getUserId());
            return cart;
        }).collect(Collectors.toList());
        shoppingCartService.saveBatch(shoppingCarts);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderDtoById(Long id) {
        Order order = getById(id);
        if (order == null) {
            return null;
        }
        return buildDetailsAddressDto(order);
    }

    @Override
    public boolean removeOrderById(Long id) {
        Order order = getById(id);
        if (order == null) {
            return false;
        }
        if (order.getStatus() != 5 && order.getStatus() != 6) {
            return false;
        }
        // 仅当订单已完成或已取消才能删除
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getId, id).set(Order::getUserIsDeleted, 1);
        update(wrapper);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getPageInfo(Integer page, Integer pageSize, String number, LocalDateTime beginTime, LocalDateTime endTime) {
        Page<Order> orderPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(number != null, Order::getNumber, number)
                .ge(beginTime != null, Order::getOrderTime, beginTime)
                .le(endTime != null, Order::getOrderTime, endTime)
                .orderByDesc(Order::getOrderTime);
        page(orderPage, wrapper);

        // 获取OrderDto的分页数据（包含订单明细和地址信息）
        List<Order> orderList = orderPage.getRecords();
        List<OrderDto> orderDtoList = orderList.stream().map(this::buildDetailsAddressDto).collect(Collectors.toList());
        Page<OrderDto> orderDtoPage = new Page<>();
        BeanUtils.copyProperties(orderPage, orderDtoPage, "records");
        orderDtoPage.setRecords(orderDtoList);

        return orderDtoPage;
    }

    @Override
    public void updateOrder(Order order) {
        if (order.getStatus() != null) {
            switch (order.getStatus()) {
                case 2:
                    order.setPayTime(LocalDateTime.now());
                    break;
                case 3:
                    order.setReceiveTime(LocalDateTime.now());
                    break;
                case 4:
                    order.setDeliverTime(LocalDateTime.now());
                    break;
                case 5:
                    order.setFinishTime(LocalDateTime.now());
                    break;
                case 6:
                    order.setCancelTime(LocalDateTime.now());
                    break;
            }
        }
        updateById(order);
    }

    /**
     * 查询订单明细并生成对应的 DTO
     *
     * @param order
     * @return OrderDto
     */
    @Transactional(readOnly = true)
    public OrderDto buildDetailsDto(Order order) {
        // 查询订单明细
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, order.getId());
        List<OrderDetail> details = orderDetailService.list(wrapper);

        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        orderDto.setDetails(details);
        return orderDto;
    }

    /**
     * 查询订单明细和地址信息并生成对应的 DTO
     *
     * @param order
     * @return
     */
    @Transactional(readOnly = true)
    public OrderDto buildDetailsAddressDto(Order order) {
        OrderDto orderDto = buildDetailsDto(order);
        AddressBook address = addressBookService.getById(orderDto.getAddressBookId());
        orderDto.setAddress(address);
        return orderDto;
    }
}