package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.AddressBook;
import com.freedom.mojito.pojo.User;
import com.freedom.mojito.service.AddressBookService;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "用户地址相关API")
@RestController
@RequestMapping("/front/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private HttpSession session;


    @ApiOperation("当前用户添加地址")
    @PostMapping
    public Result<Object> addAddress(@RequestBody @Validated AddressBook address, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        User currUser = (User) session.getAttribute("user");
        address.setUserId(currUser.getId());
        addressBookService.saveAddress(address);

        return Result.succeed(null);
    }


    @ApiOperation("查询当前用户的所有地址信息")
    @GetMapping
    public Result<List<AddressBook>> getAddresses() {
        User currUser = (User) session.getAttribute("user");
        List<AddressBook> addressList = addressBookService.getAddressesByUserId(currUser.getId());
        return Result.succeed(addressList);
    }


    @ApiOperation("根据id获取地址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressById(@PathVariable("id") Long id) {
        AddressBook address = addressBookService.getById(id);
        return Result.succeed(address);
    }


    @ApiOperation("修改当前用户地址信息")
    @PutMapping
    public Result<Object> updateAddress(@RequestBody @Validated AddressBook address, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }
        User currUser = (User) session.getAttribute("user");
        address.setUserId(currUser.getId());
        addressBookService.updateAddress(address);
        return Result.succeed(null);
    }


    @ApiOperation("根据id删除地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @DeleteMapping("/{id}")
    public Result<Object> deleteAddress(@PathVariable("id") Long id) {
        addressBookService.removeById(id, false);
        return Result.succeed(null);
    }


    @ApiOperation("查询下单地址信息")
    @GetMapping("/order")
    public Result<AddressBook> getOrderAddress() {
        User user = (User) session.getAttribute("user");
        AddressBook address = addressBookService.getOrderAddress(user.getId());

        return address != null ? Result.succeed(address) : Result.fail("未获取到下单地址");
    }
}
