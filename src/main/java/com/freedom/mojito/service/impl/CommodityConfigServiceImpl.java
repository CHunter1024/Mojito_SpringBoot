package com.freedom.mojito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.mojito.pojo.CommodityConfig;
import com.freedom.mojito.service.CommodityConfigService;
import com.freedom.mojito.mapper.CommodityConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 针对表【commodity_config(商品配置)】的数据库操作Service实现
 * <p>CreateTime: 2022-07-19 下午 11:50</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Service
public class CommodityConfigServiceImpl extends ServiceImpl<CommodityConfigMapper, CommodityConfig> implements CommodityConfigService {
}




