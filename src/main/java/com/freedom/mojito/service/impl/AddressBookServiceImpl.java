package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.pojo.AddressBook;
import com.freedom.mojito.pojo.Order;
import com.freedom.mojito.service.AddressBookService;
import com.freedom.mojito.mapper.AddressBookMapper;
import com.freedom.mojito.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 针对表【address_book(地址信息)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-12 下午 2:12</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Autowired
    private OrderService orderService;

    @Override
    public void saveAddress(AddressBook address) {
        save(address);
        updateOtherAddressDefault(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressBook> getAddressesByUserId(Long userId) {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId)
                .orderByDesc(AddressBook::getIsDefault).orderByDesc(AddressBook::getUpdateTime);
        return list(wrapper);
    }

    @Override
    public void updateAddress(AddressBook address) {
        updateById(address);
        updateOtherAddressDefault(address);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressBook getOrderAddress(Long userId) {
        AddressBook address;
        // 获取用户最新订单的下单地址
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getUserId, userId).orderByDesc(Order::getOrderTime).last("LIMIT 1");
        Order order = orderService.getOne(orderWrapper);
        if (order == null) {
            // 如果没有订单，则查询默认地址
            LambdaQueryWrapper<AddressBook> addressBookWrapper = new LambdaQueryWrapper<>();
            addressBookWrapper.eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1).last("LIMIT 1");
            address = getOne(addressBookWrapper);  // 默认地址可有可无
        } else {
            // 如果有订单，则根据订单的地址id查询地址
            address = getById(order.getAddressBookId());  // 该地址可能被删除
        }
        return address;
    }

    /**
     * 修改其他地址的默认状态
     *
     * @param address 默认地址
     */
    private void updateOtherAddressDefault(AddressBook address) {
        // 如果该地址是默认地址，需要将该用户下的其他地址改为非默认地址
        if (address.getIsDefault() == 1) {
            LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(AddressBook::getIsDefault, 0)
                    .eq(AddressBook::getUserId, address.getUserId()).eq(AddressBook::getIsDefault, 1)
                    .notIn(AddressBook::getId, address.getId());
            update(wrapper);
        }
    }
}




