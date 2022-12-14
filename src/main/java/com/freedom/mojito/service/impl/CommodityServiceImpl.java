package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.dto.CommodityDto;
import com.freedom.mojito.mapper.CommodityConfigMapper;
import com.freedom.mojito.mapper.CommodityMapper;
import com.freedom.mojito.pojo.Commodity;
import com.freedom.mojito.pojo.CommodityConfig;
import com.freedom.mojito.service.CommodityConfigService;
import com.freedom.mojito.service.CommodityService;
import com.freedom.mojito.util.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 针对表【commodity(商品信息)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-19 下午 11:50</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @Autowired
    private CommodityConfigService commodityConfigService;
    @Autowired
    private CommodityConfigMapper commodityConfigMapper;
    @Autowired
    private FileUtils fileUtils;

    @Override
    @CacheEvict(value = "commodities", key = "'categoryId:' + #commodityDto.getCategoryId() + 'status:1'")
    public void saveWithConfigs(CommodityDto commodityDto) throws IOException {
        // 保存商品信息
        save(commodityDto);

        // 保存商品配置
        List<CommodityConfig> configs = commodityDto.getConfigs();
        for (CommodityConfig config : configs) {
            config.setCommodityId(commodityDto.getId());
        }
        commodityConfigService.saveBatch(configs);

        // 将图片文件从临时目录移动到上传目录
        fileUtils.saveNewFile(commodityDto.getImage(), null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Commodity> getPageInfo(Integer page, Integer pageSize, CommodityDto commodityDto) {
        Page<Commodity> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
        // 商品名称关键字查询
        wrapper.like(StringUtils.hasText(commodityDto.getName()), Commodity::getName, commodityDto.getName())
                // 商品分类ids查询
                .in(!CollectionUtils.isEmpty(commodityDto.getCategoryIds()), Commodity::getCategoryId, commodityDto.getCategoryIds())
                .orderByDesc(Commodity::getSales).orderByDesc(Commodity::getUpdateTime);

        return page(pageInfo, wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public CommodityDto getWithConfigsById(Long id) {
        Commodity commodity = getById(id);
        if (commodity == null) {
            return null;
        }
        return buildDto(commodity);
    }

    @Override
    @CacheEvict(value = "commodities", key = "'categoryId:' + #commodityDto.getCategoryId() + 'status:1'")
    public void updateWithConfigs(CommodityDto commodityDto) throws IOException {
        String oldImageName = getById(commodityDto.getId()).getImage();
        String newImageName = commodityDto.getImage();

        // 修改商品信息
        updateById(commodityDto);

        // 修改商品配置
        List<CommodityConfig> configs = commodityDto.getConfigs();
        for (CommodityConfig config : configs) {
            if (config.getCommodityId() == null) {
                config.setCommodityId(commodityDto.getId());
            }
        }
        // saveOrUpdate：如果有主键id，会向数据库查询记录，如果有记录则更新，没有则插入；如果没有主键id，则插入数据
        commodityConfigService.saveOrUpdateBatch(configs);
        // 真删除数据库中除了最新配置外的其他配置记录，目的是保证数据最新，且不含假数据
        List<Long> ids = configs.stream().map(CommodityConfig::getId).collect(Collectors.toList());
        commodityConfigMapper.trueDeleteByIdsAndCommodityId(ids, commodityDto.getId());

        // 新旧图片处理
        fileUtils.handleOldNewFile(oldImageName, newImageName, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commodity> getByCondition(Commodity commodity) {
        LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
        // 根据商品分类id查询
        wrapper.eq(commodity.getCategoryId() != null, Commodity::getCategoryId, commodity.getCategoryId())
                // 根据商品名称关键字查询
                .like(commodity.getName() != null, Commodity::getName, commodity.getName())
                // 根据商品状态查询
                .eq(commodity.getStatus() != null, Commodity::getStatus, commodity.getStatus())
                .orderByDesc(Commodity::getSales).orderByAsc(Commodity::getCreateTime);

        return list(wrapper);
    }

    @Override
    @CacheEvict(value = "commodities", allEntries = true)  // 删除所有商品的缓存数据
    public void removeWithConfigs(List<Long> ids) {
        List<Long> categoryIds = ids.stream().map(id -> getById(id).getCategoryId()).collect(Collectors.toList());
        // 删除商品
        removeByIds(ids, false);
        // 删除商品配置
        LambdaUpdateWrapper<CommodityConfig> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(CommodityConfig::getCommodityId, ids);
        commodityConfigService.remove(wrapper);
    }

    @Override
    @CacheEvict(value = "commodities", allEntries = true)
    public void updateStatusBatch(List<Long> ids, Integer status) {
        List<Commodity> commodityList = ids.stream().map(id -> {
            Commodity commodity = new Commodity();
            commodity.setId(id);
            commodity.setStatus(status);
            return commodity;
        }).collect(Collectors.toList());
        updateBatchById(commodityList);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "commodities", key = "'categoryId:' + #commodity.getCategoryId() + 'status:' + #commodity.getStatus()")
    public List<CommodityDto> getWithConfigsByCondition(Commodity commodity) {
        List<Commodity> commodityList = getByCondition(commodity);
        return commodityList.stream().map(this::buildDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPriceById(Long id) {
        return getById(id).getPrice();
    }

    @Override
    public void incrSalesById(Long id, Integer increment) {
        Integer sales = getById(id).getSales();
        LambdaUpdateWrapper<Commodity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Commodity::getId, id).set(Commodity::getSales, sales + increment);
        update(wrapper);  // 使用LambdaUpdateWrapper更新不会自动填充更新时间字段
    }

    /**
     * 查询商品配置并生成对应的 DTO
     *
     * @param commodity
     * @return CommodityDto
     */
    @Transactional(readOnly = true)
    public CommodityDto buildDto(Commodity commodity) {
        // 查询商品配置
        LambdaQueryWrapper<CommodityConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommodityConfig::getCommodityId, commodity.getId());
        List<CommodityConfig> configs = commodityConfigService.list(wrapper);

        CommodityDto commodityDto = new CommodityDto();
        BeanUtils.copyProperties(commodity, commodityDto);  // 将 commodity 属性内容赋值给 commodityDto（浅拷贝）
        commodityDto.setConfigs(configs);
        return commodityDto;
    }
}