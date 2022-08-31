package com.freedom.mojito.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.freedom.mojito.pojo.Employee;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * Description: 自定义 MyBatisPlus 元数据处理器
 * <p>CreateTime: 2022-07-19 上午 11:33</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Component
public class MPMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private HttpSession session;

    /**
     * 插入操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter("createTime")) {
            metaObject.setValue("createTime", LocalDateTime.now());
        }
        if (metaObject.hasSetter("updateTime")) {
            metaObject.setValue("updateTime", LocalDateTime.now());
        }
        if (metaObject.hasSetter("createUser")) {
            Employee employee = (Employee) session.getAttribute("employee");
            metaObject.setValue("createUser", employee.getId());
        }
        if (metaObject.hasSetter("updateUser")) {
            Employee employee = (Employee) session.getAttribute("employee");
            metaObject.setValue("updateUser", employee.getId());
        }
    }

    /**
     * 更新操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updateTime")) {
            metaObject.setValue("updateTime", LocalDateTime.now());
        }
        if (metaObject.hasSetter("updateUser")) {
            Employee employee = (Employee) session.getAttribute("employee");
            metaObject.setValue("updateUser", employee.getId());
        }
    }
}
