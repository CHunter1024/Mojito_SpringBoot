package com.freedom.mojito.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.dto.ComboDto;
import com.freedom.mojito.dto.ComboDto;
import com.freedom.mojito.pojo.Combo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.freedom.mojito.pojo.Combo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 针对表【combo(套餐信息)】的数据库操作Service
 * <p>CreateTime: 2022-07-19 下午 7:32</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public interface ComboService extends IService<Combo> {

    /**
     * 保存套餐信息，同时保存对应的套餐商品和套餐配置
     *
     * @param comboDto Combo的数据传输对象
     */
    void saveWithCommoditiesAndConfigs(ComboDto comboDto) throws IOException;

    /**
     * 分页查询套餐信息
     *
     * @param page     页码
     * @param pageSize 数量
     * @param comboDto 条件参数
     * @return 分页信息
     */
    Page<Combo> getPageInfo(Integer page, Integer pageSize, ComboDto comboDto);

    /**
     * 根据id查询套餐信息，同时查询对应的套餐商品和套餐配置
     *
     * @param id 套餐id
     * @return ComboDto
     */
    ComboDto getWithCommoditiesAndConfigsById(Long id);

    /**
     * 修改套餐信息，同时修改对应的套餐商品和套餐配置
     *
     * @param comboDto Combo的数据传输对象
     */
    void updateWithCommoditiesAndConfigs(ComboDto comboDto) throws IOException;

    /**
     * 删除套餐信息，同时删除对应的套餐商品和套餐配置
     *
     * @param ids 套餐id
     */
    void removeWithCommoditiesAndConfigs(List<Long> ids);

    /**
     * 批量修改套餐状态
     *
     * @param ids    套餐id
     * @param status 套餐状态
     */
    void updateStatusBatch(List<Long> ids, Integer status);

    /**
     * 根据条件查询套餐信息，同时查询对应的套餐商品和套餐配置
     *
     * @param combo 条件信息
     * @return Combo集合
     */
    List<ComboDto> getWithConfigsAndCommoditiesByCondition(Combo combo);

    /**
     * 根据id查询套餐价格
     *
     * @param id 套餐id
     * @return 套餐价格
     */
    BigDecimal getPriceById(Long id);

    /**
     * 根据id增加套餐的销量
     *
     * @param id        套餐id
     * @param increment 增量
     */
    void incrSalesById(Long id, Integer increment);
}
