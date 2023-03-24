package com.freedom.mojito.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.EmployeeDto;
import com.freedom.mojito.pojo.Employee;
import com.freedom.mojito.service.EmployeeService;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-07-12 下午 2:15</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Api(tags = "员工相关API")
@RestController
@RequestMapping("/backend/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private HttpSession session;


    @ApiOperation(value = "员工登录", notes = "必要参数：loginId（帐号/手机号码），password（密码）")
    @PostMapping("/login")
    public Result<Object> login(@RequestBody EmployeeDto employeeDto) {
        // 1、根据页面提交的帐号或手机号码查询数据库，如果没有查询到返回用户不存在结果
        Employee employee = employeeService.getByAccountOrPhoneNumber(employeeDto.getLoginId());
        if (employee == null) {
            return Result.fail("帐号不存在");
        }

        // 2、密码比对，如果不一致则返回密码错误结果
        if (!BCrypt.checkpw(employeeDto.getPassword(), employee.getPassword())) {
            return Result.fail("密码错误");
        }

        // 3、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (employee.getStatus() == 0) {
            return Result.fail("该帐号已禁用");
        }

        // 4、登录成功，将员工信息存入session中
        session.setAttribute("employee", employee);
        return Result.succeed(null);
    }


    @ApiOperation("员工登出")
    @PostMapping("/logout")
    public Result<Object> logout() {
        // 移出session中保存的当前登录员工
        session.removeAttribute("employee");
        return Result.succeed(null);
    }

    @ApiOperation("查询当前员工信息")
    @GetMapping("/getCurrEmp")
    public Result<Employee> getCurrentEmployee() {
        Employee employee = (Employee) session.getAttribute("employee");
        return Result.succeed(employee);
    }

    @ApiOperation("修改当前员工信息")
    @PutMapping("/putCurrEmp")
    public Result<Object> updateCurrentEmployee(@RequestBody @Validated EmployeeDto employeeDto, BindingResult validResults) {
        Employee currEmp = (Employee) session.getAttribute("employee");
        // 判断是否修改密码
        if (employeeDto.getOldPassword() != null) {
            // 验证原密码
            if (!BCrypt.checkpw(employeeDto.getOldPassword(), currEmp.getPassword())) {
                return Result.fail("原密码错误");
            }
        } else {
            employeeDto.setPassword(null);
        }
        employeeDto.setId(currEmp.getId());  // 保证修改的是当前员工
        updateEmployee(employeeDto, validResults);

        currEmp = employeeService.getById(currEmp.getId());
        session.setAttribute("employee", currEmp);  // 更新存放在session中的员工信息
        return Result.succeed(null);
    }

    @ApiOperation("查询唯一的属性值是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "field", value = "属性名称（account/phone_number）", required = true, paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "value", value = "属性值", required = true, paramType = "query", dataTypeClass = String.class)
    })
    @GetMapping("/exist")
    public Result<Boolean> onlyExist(String field, String value) {
        if ("account".equals(field) || "phone_number".equals(field)) {
            return employeeService.countByFieldValue(field, value) == 0 ? Result.succeed(false) : Result.succeed(true);
        }
        return Result.fail("非法参数");
    }

    @ApiOperation("添加员工")
    @PostMapping
    public Result<Object> saveEmployee(@RequestBody @Validated Employee employee, BindingResult validResults) {
        // 处理校验数据结果
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        employeeService.saveEmployee(employee);
        return Result.succeed(null);
    }


    @ApiOperation(value = "分页+条件查询员工信息", notes = "条件：员工姓名关键字（username）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, paramType = "query", dataTypeClass = Integer.class)
    })
    @GetMapping("/page")
    public Result<Page<Employee>> getEmployeesPage(Integer page, Integer pageSize, Employee employee) {
        Page<Employee> pageInfo = employeeService.getPageInfo(page, pageSize, employee);
        return Result.succeed(pageInfo);
    }


    @ApiOperation("根据Id查询员工信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @GetMapping("/{id}")
    public Result<Employee> getEmployeeById(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        return employee != null ? Result.succeed(employee) : Result.fail("参数有误");
    }


    @ApiOperation("修改员工信息")
    @PutMapping
    public Result<Object> updateEmployee(@RequestBody @Validated Employee employee, BindingResult validResults) {
        // 处理校验数据结果
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        employeeService.updateEmployee(employee);
        return Result.succeed(null);
    }
}