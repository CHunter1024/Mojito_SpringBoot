package com.freedom.mojito.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.freedom.mojito.pojo.Category;

import java.util.List;

/**
 * Description: 针对表【category(分类信息)】的数据库操作Service
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public interface CategoryService extends IService<Category> {

    /**
     * 分页查询分类信息
     *
     * @param page     页码
     * @param pageSize 数量
     * @return 分页信息
     */
    Page<Category> getPageInfo(Integer page, Integer pageSize);

    /**
     * 根据条件查询分类信息
     *
     * @param category 条件信息
     * @return 分类信息
     */
    List<Category> getByCondition(Category category);

    /**
     * 根据id删除分类，此前进行检查是否可删除
     *
     * @param id 主键id
     * @return 删除结果
     */
    boolean removeBeforeCheck(Long id);
}
