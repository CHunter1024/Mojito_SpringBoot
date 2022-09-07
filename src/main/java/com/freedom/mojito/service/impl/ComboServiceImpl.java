package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.dto.ComboDto;
import com.freedom.mojito.mapper.ComboCommodityMapper;
import com.freedom.mojito.mapper.ComboMapper;
import com.freedom.mojito.mapper.CommodityConfigMapper;
import com.freedom.mojito.pojo.Combo;
import com.freedom.mojito.pojo.ComboCommodity;
import com.freedom.mojito.pojo.CommodityConfig;
import com.freedom.mojito.service.ComboCommodityService;
import com.freedom.mojito.service.ComboService;
import com.freedom.mojito.service.CommodityConfigService;
import com.freedom.mojito.util.ImageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 针对表【combo(套餐信息)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-19 下午 11:50</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class ComboServiceImpl extends ServiceImpl<ComboMapper, Combo> implements ComboService {

    @Autowired
    private CommodityConfigService commodityConfigService;
    @Autowired
    private CommodityConfigMapper commodityConfigMapper;
    @Autowired
    private ComboCommodityService comboCommodityService;
    @Autowired
    private ComboCommodityMapper comboCommodityMapper;
    @Autowired
    private ImageUtils imageUtils;

    @Override
    @CacheEvict(value = "combos", key = "'categoryId:' + #comboDto.getCategoryId() + 'status:1'")
    public void saveWithCommoditiesAndConfigs(ComboDto comboDto) throws IOException {

        // 保存套餐信息
        save(comboDto);
        Long comboId = comboDto.getId();  // 根据雪花算法生成的id值已经赋值到对象属性中

        // 保存套餐商品
        List<ComboCommodity> commodities = comboDto.getCommodities();
        for (ComboCommodity commodity : commodities) {
            commodity.setComboId(comboId);
        }
        comboCommodityService.saveBatch(commodities);

        // 保存套餐配置
        List<CommodityConfig> configs = comboDto.getConfigs();
        for (CommodityConfig config : configs) {
            config.setCommodityId(comboId);
        }
        commodityConfigService.saveBatch(configs);

        // 将图片文件从临时目录移动到上传目录
        imageUtils.saveNewImage(comboDto.getImage(), null);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Combo> getPageInfo(Integer page, Integer pageSize, ComboDto comboDto) {
        Page<Combo> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Combo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(comboDto.getName()), Combo::getName, comboDto.getName())
                .in(comboDto.getCategoryIds() != null, Combo::getCategoryId, comboDto.getCategoryIds())
                .orderByDesc(Combo::getSales).orderByDesc(Combo::getUpdateTime);

        return page(pageInfo, wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public ComboDto getWithCommoditiesAndConfigsById(Long id) {
        Combo combo = getById(id);
        if (combo == null) {
            return null;
        }
        return buildDto(combo);
    }

    @Override
    @CacheEvict(value = "combos", key = "'categoryId:' + #comboDto.getCategoryId() + 'status:1'")
    public void updateWithCommoditiesAndConfigs(ComboDto comboDto) throws IOException {
        String oldImageName = getById(comboDto.getId()).getImage();
        String newImageName = comboDto.getImage();

        // 修改套餐信息
        updateById(comboDto);

        // 修改套餐商品
        List<ComboCommodity> commodities = comboDto.getCommodities();
        for (ComboCommodity commodity : commodities) {
            if (commodity.getComboId() == null) {
                commodity.setComboId(comboDto.getId());
            }
        }
        comboCommodityService.saveOrUpdateBatch(commodities);
        List<Long> commodityIds = commodities.stream().map(ComboCommodity::getId).collect(Collectors.toList());
        comboCommodityMapper.trueDeleteByIdsAndComboId(commodityIds, comboDto.getId());

        // 修改套餐配置
        List<CommodityConfig> configs = comboDto.getConfigs();
        for (CommodityConfig config : configs) {
            if (config.getCommodityId() == null) {
                config.setCommodityId(comboDto.getId());
            }
        }
        commodityConfigService.saveOrUpdateBatch(configs);
        List<Long> configIds = configs.stream().map(CommodityConfig::getId).collect(Collectors.toList());
        commodityConfigMapper.trueDeleteByIdsAndCommodityId(configIds, comboDto.getId());

        // 新旧图片处理
        imageUtils.handleOldNewImage(oldImageName, newImageName, null);
    }

    @Override
    @CacheEvict(value = "combos", allEntries = true)  // 删除所有套餐的缓存数据
    public void removeWithCommoditiesAndConfigs(List<Long> ids) {
        // 删除套餐
        removeByIds(ids, false);
        // 删除套餐商品
        LambdaUpdateWrapper<ComboCommodity> commodityWrapper = new LambdaUpdateWrapper<>();
        commodityWrapper.in(ComboCommodity::getComboId, ids);
        comboCommodityService.remove(commodityWrapper);
        // 删除商品配置
        LambdaUpdateWrapper<CommodityConfig> configWrapper = new LambdaUpdateWrapper<>();
        configWrapper.in(CommodityConfig::getCommodityId, ids);
        commodityConfigService.remove(configWrapper);
    }

    @Override
    @CacheEvict(value = "combos", allEntries = true)
    public void updateStatusBatch(List<Long> ids, Integer status) {
        List<Combo> comboList = ids.stream().map(id -> {
            Combo combo = new Combo();
            combo.setId(id);
            combo.setStatus(status);
            return combo;
        }).collect(Collectors.toList());
        updateBatchById(comboList);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "combos", key = "'categoryId:' + #combo.getCategoryId() + 'status:' + #combo.getStatus()")
    public List<ComboDto> getWithConfigsAndCommoditiesByCondition(Combo combo) {
        LambdaQueryWrapper<Combo> wrapper = new LambdaQueryWrapper<>();
        // 根据套餐分类id查询
        wrapper.eq(combo.getCategoryId() != null, Combo::getCategoryId, combo.getCategoryId())
                // 根据套餐状态查询
                .eq(combo.getStatus() != null, Combo::getStatus, combo.getStatus())
                .orderByDesc(Combo::getSales).orderByAsc(Combo::getCreateTime);

        List<Combo> comboList = list(wrapper);
        return comboList.stream().map(this::buildDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPriceById(Long id) {
        Combo combo = getById(id);
        return combo != null ? combo.getPrice() : null;
    }

    @Override
    public void incrSalesById(Long id, Integer increment) {
        Integer sales = getById(id).getSales();
        LambdaUpdateWrapper<Combo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Combo::getId, id).set(Combo::getSales, sales + increment);
        update(wrapper);  // 使用LambdaUpdateWrapper更新不会自动填充更新时间字段
    }

    /**
     * 查询套餐商品和套餐配置并生成对应的 DTO
     *
     * @param combo
     * @return ComboDto
     */
    @Transactional(readOnly = true)
    public ComboDto buildDto(Combo combo) {
        // 查询套餐商品
        LambdaQueryWrapper<ComboCommodity> commodityWrapper = new LambdaQueryWrapper<>();
        commodityWrapper.eq(ComboCommodity::getComboId, combo.getId());
        List<ComboCommodity> commodities = comboCommodityService.list(commodityWrapper);

        // 查询套餐配置
        LambdaQueryWrapper<CommodityConfig> configWrapper = new LambdaQueryWrapper<>();
        configWrapper.eq(CommodityConfig::getCommodityId, combo.getId());
        List<CommodityConfig> configs = commodityConfigService.list(configWrapper);

        ComboDto comboDto = new ComboDto();
        BeanUtils.copyProperties(combo, comboDto);  // 将 combo 属性内容赋值给 comboDto（浅拷贝）
        comboDto.setCommodities(commodities);
        comboDto.setConfigs(configs);
        return comboDto;
    }
}




