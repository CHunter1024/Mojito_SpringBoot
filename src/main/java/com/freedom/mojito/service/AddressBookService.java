package com.freedom.mojito.service;

import com.freedom.mojito.pojo.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Description: 针对表【address_book(地址信息)】的数据库操作Service
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public interface AddressBookService extends IService<AddressBook> {

    /**
     * 添加地址
     *
     * @param address
     */
    void saveAddress(AddressBook address);

    /**
     * 根据用户id查询地址信息
     *
     * @param userId
     * @return
     */
    List<AddressBook> getAddressesByUserId(Long userId);

    /**
     * 修改地址
     *
     * @param address
     */
    void updateAddress(AddressBook address);

    /**
     * 根据用户id查询下单地址
     *
     * @param userId
     * @return
     */
    AddressBook getOrderAddress(Long userId);

    /**
     * 根据id删除地址
     *
     * @param id
     */
    void removeAddressById(Long id);
}
