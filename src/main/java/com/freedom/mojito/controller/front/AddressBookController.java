package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.AddressBook;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.AddressBookService;
import com.freedom.mojito.util.ValidateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-08-15 下午 7:06</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@RestController
@RequestMapping("/front/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 当前用户添加地址
     *
     * @param address
     * @param validResults
     * @param session
     * @return
     */
    @PostMapping
    public Result<Object> addAddress(@RequestBody @Validated AddressBook address, BindingResult validResults, HttpSession session) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        User currUser = (User) session.getAttribute("user");
        address.setUserId(currUser.getId());
        addressBookService.saveAddress(address);

        return Result.succeed(null);
    }

    /**
     * 获取当前用户下的所有地址
     *
     * @param session
     * @return
     */
    @GetMapping
    public Result<List<AddressBook>> getAddresses(HttpSession session) {
        User currUser = (User) session.getAttribute("user");
        List<AddressBook> addressList = addressBookService.getAddressesByUserId(currUser.getId());
        return Result.succeed(addressList);
    }

    /**
     * 根据id获取地址
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressById(@PathVariable("id") Long id) {
        AddressBook address = addressBookService.getById(id);
        return Result.succeed(address);
    }

    /**
     * 修改当前用户地址
     *
     * @param address
     * @param validResults
     * @return
     */
    @PutMapping
    public Result<Object> updateAddress(@RequestBody @Validated AddressBook address, BindingResult validResults, HttpSession session) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }
        User currUser = (User) session.getAttribute("user");
        address.setUserId(currUser.getId());
        addressBookService.updateAddress(address);
        return Result.succeed(null);
    }

    /**
     * 根据id删除地址
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Object> deleteAddress(@PathVariable("id") Long id) {
        addressBookService.removeById(id, false);
        return Result.succeed(null);
    }

    /**
     * 获取下单地址
     *
     * @param session
     * @return
     */
    @GetMapping("/order")
    public Result<AddressBook> getOrderAddress(HttpSession session) {
        User user = (User) session.getAttribute("user");
        AddressBook address = addressBookService.getOrderAddress(user.getId());

        return address != null ? Result.succeed(address) : Result.fail("未获取到下单地址");
    }
}
