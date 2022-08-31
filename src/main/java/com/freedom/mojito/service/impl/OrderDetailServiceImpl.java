package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.pojo.OrderDetail;
import com.freedom.mojito.service.OrderDetailService;
import com.freedom.mojito.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
 * Description: 针对表【order_detail(订单明细)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-12 下午 2:12</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}




