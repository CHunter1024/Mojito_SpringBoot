package com.freedom.mojito.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.Category;
import com.freedom.mojito.service.CategoryService;
import com.freedom.mojito.util.ValidateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 * <p>CreateTime: 2022-07-19 下午 7:37</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@RestController("backend-categoryController")
@RequestMapping("/backend/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询分类信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Category>> getCategoriesPage(Integer page, Integer pageSize) {
        Page<Category> pageInfo = categoryService.getPageInfo(page, pageSize);
        return Result.succeed(pageInfo);
    }

    /**
     * 添加分类
     *
     * @param category
     * @param validResults
     * @return
     */
    @PostMapping
    public Result<Object> saveCategory(@RequestBody @Validated Category category, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        boolean result = categoryService.save(category);
        return result ? Result.succeed(null) : Result.fail("参数有误");
    }

    /**
     * 修改分类信息
     *
     * @param category
     * @param validResults
     * @return
     */
    @PutMapping
    public Result<Object> updateCategory(@RequestBody @Validated Category category, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        boolean result = categoryService.updateById(category);
        return result ? Result.succeed(null) : Result.fail("参数有误");
    }

    /**
     * 根据条件查询分类信息
     *
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(Category category) {
        List<Category> categoryList = categoryService.getByCondition(category);
        return Result.succeed(categoryList);
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Object> deleteCategory(@PathVariable("id") Long id) {
        boolean result = categoryService.removeBeforeCheck(id);
        return result ? Result.succeed(null) : Result.fail("该分类下不为空");
    }
}
