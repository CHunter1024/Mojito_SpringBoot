package com.freedom.mojito.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.pojo.Category;
import com.freedom.mojito.service.CategoryService;
import com.freedom.mojito.util.ValidateData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "分类相关API（管理端）")
@RestController("backend-categoryController")
@RequestMapping("/backend/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @ApiOperation("分页查询分类信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, paramType = "query", dataTypeClass = Integer.class)
    })
    @GetMapping("/page")
    public Result<Page<Category>> getCategoriesPage(Integer page, Integer pageSize) {
        Page<Category> pageInfo = categoryService.getPageInfo(page, pageSize);
        return Result.succeed(pageInfo);
    }


    @ApiOperation("添加分类")
    @PostMapping
    public Result<Object> saveCategory(@RequestBody @Validated Category category, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        boolean result = categoryService.save(category);
        return result ? Result.succeed(null) : Result.fail("参数有误");
    }


    @ApiOperation("修改分类信息")
    @PutMapping
    public Result<Object> updateCategory(@RequestBody @Validated Category category, BindingResult validResults) {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        boolean result = categoryService.updateById(category);
        return result ? Result.succeed(null) : Result.fail("参数有误");
    }


    @ApiOperation(value = "根据条件查询分类信息", notes = "条件：分类类型（type）")
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(Category category) {
        List<Category> categoryList = categoryService.getByCondition(category);
        return Result.succeed(categoryList);
    }


    @ApiOperation("删除分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path", dataTypeClass = Long.class)
    })
    @DeleteMapping("/{id}")
    public Result<Object> deleteCategory(@PathVariable("id") Long id) {
        boolean result = categoryService.removeBeforeCheck(id);
        return result ? Result.succeed(null) : Result.fail("该分类下不为空");
    }
}
