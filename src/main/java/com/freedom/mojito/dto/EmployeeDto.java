package com.freedom.mojito.dto;

import com.freedom.mojito.pojo.Employee;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: Employee的数据传输对象
 * <p>CreateTime: 2022-08-03 下午 8:39</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Data
@EqualsAndHashCode(callSuper = true)  // 调用父类的equals和hashCode实现作为生成的equals和hashCode算法的一部分
@ApiModel("员工信息DTO")
public class EmployeeDto extends Employee {

    @ApiModelProperty("登录Id")
    private String loginId;

    @ApiModelProperty("原密码")
    private String oldPassword;

}
