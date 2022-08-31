package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.mapper.EmployeeMapper;
import com.freedom.mojito.pojo.Employee;
import com.freedom.mojito.service.EmployeeService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Description: 针对表【employee(员工信息)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-12 下午 2:12</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    @Transactional(readOnly = true)
    public Long countByFieldValue(String field, String value) {
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq(field, value);
        return count(wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getByAccountOrPhoneNumber(String accountOrPhoneNumber) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        // 确定只会查询到1或0条数据，且查询条件字段不是主键，加上limit 1会大大提高查询效率
        wrapper.eq(Employee::getAccount, accountOrPhoneNumber).or().eq(Employee::getPhoneNumber, accountOrPhoneNumber)
                .last("LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> getPageInfo(Integer page, Integer pageSize, String username) {
        // 设置分页参数
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        // 构造查询条件
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(username), Employee::getUsername, username).orderByDesc(Employee::getUpdateTime);

        // 进行查询
        return page(pageInfo, wrapper);
    }

    @Override
    public void saveEmployee(Employee employee) {
        String enPassword = BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt());  // 将密码进行加盐加密
        employee.setPassword(enPassword);

        save(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        if (employee.getPassword() != null) {
            String enPassword = BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt());  // 将密码进行加盐加密
            employee.setPassword(enPassword);
        }

        updateById(employee);
    }
}
