package com.freedom.mojito.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.freedom.mojito.pojo.Employee;

/**
 * Description:  针对表【employee(员工信息)】的数据库操作Service
 * <p>CreateTime: 2022-07-12 下午 2:10</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public interface EmployeeService extends IService<Employee> {

    /**
     * 根据字段和值获取员工数量
     *
     * @param field 字段
     * @param value 值
     * @return 员工数量
     */
    Long countByFieldValue(String field, String value);

    /**
     * 根据帐号或者手机号码获取员工信息
     *
     * @param accountOrPhoneNumber 帐号或者手机号码
     * @return 员工
     */
    Employee getByAccountOrPhoneNumber(String accountOrPhoneNumber);

    /**
     * 分页查询员工信息
     *
     * @param page     页码
     * @param pageSize 数量
     * @param employee 条件参数
     * @return 分页信息
     */
    Page<Employee> getPageInfo(Integer page, Integer pageSize, Employee employee);

    /**
     * 保存员工信息
     *
     * @param employee 员工信息
     */
    void saveEmployee(Employee employee);

    /**
     * 修改员工信息
     *
     * @param employee 员工信息
     */
    void updateEmployee(Employee employee);
}
