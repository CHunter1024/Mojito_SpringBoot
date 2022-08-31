package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.mapper.CategoryMapper;
import com.freedom.mojito.pojo.Category;
import com.freedom.mojito.pojo.Combo;
import com.freedom.mojito.pojo.Commodity;
import com.freedom.mojito.service.CategoryService;
import com.freedom.mojito.service.ComboService;
import com.freedom.mojito.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description: 针对表【category(分类信息)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-19 下午 11:50</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CommodityService commodityService;
    @Autowired
    private ComboService comboService;

    @Override
    @Transactional(readOnly = true)
    public Page<Category> getPageInfo(Integer page, Integer pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        return page(pageInfo, wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getByCondition(Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        // 添加条件
        wrapper.eq(category.getType() != null, Category::getType, category.getType());  // 根据分类类型
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);  // 排序条件

        return list(wrapper);
    }

    @Override
    public boolean removeBeforeCheck(Long id) {
        // 检查该分类下是否有商品或套餐
        LambdaQueryWrapper<Commodity> commodityWrapper = new LambdaQueryWrapper<>();
        commodityWrapper.eq(Commodity::getCategoryId, id);
        long commodityCount = commodityService.count(commodityWrapper);
        if (commodityCount > 0) {
            return false;
        }
        LambdaQueryWrapper<Combo> comboWrapper = new LambdaQueryWrapper<>();
        comboWrapper.eq(Combo::getCategoryId, id);
        long comboCount = comboService.count(comboWrapper);
        if (comboCount > 0) {
            return false;
        }

        // 删除分类
        return removeById(id, false);  // 有逻辑删除，删除的时候不使用自动填充
    }
}
