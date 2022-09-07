package com.freedom.mojito.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.pojo.Commodity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.freedom.mojito.dto.CommodityDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 针对表【commodity(商品信息)】的数据库操作Service
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public interface CommodityService extends IService<Commodity> {

    /**
     * 保存商品信息，同时保存对应的商品配置
     *
     * @param commodityDto Commodity的数据传输对象
     */
    void saveWithConfigs(CommodityDto commodityDto) throws IOException;

    /**
     * 分页查询商品信息
     *
     * @param page         页码
     * @param pageSize     数量
     * @param commodityDto 条件参数
     * @return 分页信息
     */
    Page<Commodity> getPageInfo(Integer page, Integer pageSize, CommodityDto commodityDto);

    /**
     * 根据id查询商品信息，同时查询对应的商品配置
     *
     * @param id 商品id
     * @return CommodityDto
     */
    CommodityDto getWithConfigsById(Long id);

    /**
     * 修改商品信息，同时修改对应的商品配置
     *
     * @param commodityDto Commodity的数据传输对象
     */
    void updateWithConfigs(CommodityDto commodityDto) throws IOException;

    /**
     * 根据条件查询商品信息
     *
     * @param commodity 条件信息
     * @return 商品信息
     */
    List<Commodity> getByCondition(Commodity commodity);

    /**
     * 删除商品信息，同时删除对应的商品配置
     *
     * @param ids 商品id
     */
    void removeWithConfigs(List<Long> ids);

    /**
     * 批量修改商品状态
     *
     * @param ids    商品id
     * @param status 商品状态
     */
    void updateStatusBatch(List<Long> ids, Integer status);

    /**
     * 根据条件查询商品信息，同时查询对应的商品配置
     *
     * @param commodity 条件信息
     * @return CommodityDto集合
     */
    List<CommodityDto> getWithConfigsByCondition(Commodity commodity);

    /**
     * 根据id查询商品的价格
     *
     * @param id 商品id
     * @return 商品价格
     */
    BigDecimal getPriceById(Long id);

    /**
     * 根据id增加商品的销量
     *
     * @param id        商品id
     * @param increment 增量
     */
    void incrSalesById(Long id, Integer increment);
}
