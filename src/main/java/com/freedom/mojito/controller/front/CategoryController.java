package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-08-06 下午 12:44</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Api(tags = "分类相关API（客户端）")
@RestController("front-categoryController")
@RequestMapping("/front/category")
public class CategoryController {

    /**
     * 调用后台的Controller
     */
    @Autowired
    @Qualifier("backend-categoryController")
    private com.freedom.mojito.controller.backend.CategoryController categoryController;


    @ApiOperation(value = "根据条件查询分类信息", notes = "条件：分类类型（type）")
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(Category category) {
        return categoryController.getCategoryList(category);
    }
}
