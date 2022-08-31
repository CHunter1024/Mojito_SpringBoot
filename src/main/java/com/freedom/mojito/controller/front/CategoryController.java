package com.freedom.mojito.controller.front;

import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.Category;
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

@RestController("front-categoryController")
@RequestMapping("/front/category")
public class CategoryController {

    /**
     * 调用后台的Controller
     */
    @Autowired
    @Qualifier("backend-categoryController")
    private com.freedom.mojito.controller.backend.CategoryController categoryController;

    /**
     * 根据条件查询分类信息
     *
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(Category category) {
        return categoryController.getCategoryList(category);
    }
}
