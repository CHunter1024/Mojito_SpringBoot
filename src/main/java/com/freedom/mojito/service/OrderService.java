package com.freedom.mojito.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.dto.OrderDto;
import com.freedom.mojito.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * Description: 针对表【order(订单信息)】的数据库操作Service
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public interface OrderService extends IService<Order> {

    /**
     * 添加订单
     *
     * @param order
     * @return 订单主键
     */
    Long saveOrder(Order order);

    /**
     * 根据用户id分页查询订单
     *
     * @param userId
     * @return
     */
    Page<OrderDto> getPageInfoByUserId(Integer page, Integer pageSize, Long userId);

    /**
     * 再来一单
     *
     * @param order
     */
    void addAgain(Order order);

    /**
     * 根据id查询订单Dto信息
     *
     * @param id
     * @return
     */
    OrderDto getOrderDtoById(Long id);

    /**
     * 根据id删除订单
     *
     * @param id
     * @return
     */
    boolean removeOrderById(Long id);

    /**
     * 分页查询订单
     *
     * @param page      页码
     * @param pageSize  数量
     * @param number    订单号
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 分页信息
     */
    Page<OrderDto> getPageInfo(Integer page, Integer pageSize, String number, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 修改订单
     *
     * @param order
     */
    void updateOrder(Order order);
}
