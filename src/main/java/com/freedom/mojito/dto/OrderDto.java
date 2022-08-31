package com.freedom.mojito.dto;

import com.freedom.mojito.pojo.AddressBook;
import com.freedom.mojito.pojo.Order;
import com.freedom.mojito.pojo.OrderDetail;
import lombok.Data;

import java.util.List;

/**
 * Description: Order的数据传输对象
 * <p>CreateTime: 2022-08-18 下午 9:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Data
public class OrderDto extends Order {

    /**
     * 订单明细
     */
    private List<OrderDetail> details;

    /**
     * 下单地址
     */
    private AddressBook address;

}
