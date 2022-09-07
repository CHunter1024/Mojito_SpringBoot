package com.freedom.mojito.controller.backend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.mojito.common.Result;
import com.freedom.mojito.dto.ComboDto;
import com.freedom.mojito.pojo.Combo;
import com.freedom.mojito.service.ComboService;
import com.freedom.mojito.util.ValidateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * <p>CreateTime: 2022-07-26 下午 11:06</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@RestController("backend-comboController")
@RequestMapping("/backend/combo")
public class ComboController {

    @Autowired
    private ComboService comboService;

    /**
     * 保存套餐信息
     *
     * @param comboDto
     * @param validResults
     * @return
     * @throws IOException
     */
    @PostMapping
    public Result<Object> saveCombo(@RequestBody @Validated ComboDto comboDto, BindingResult validResults) throws IOException {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        comboService.saveWithCommoditiesAndConfigs(comboDto);

        return Result.succeed(null);
    }

    /**
     * 分页 + 条件 查询套餐信息
     *
     * @param page
     * @param pageSize
     * @param comboDto
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Combo>> getCommoditiesPage(Integer page, Integer pageSize, ComboDto comboDto) {
        Page<Combo> pageInfo = comboService.getPageInfo(page, pageSize, comboDto);
        return Result.succeed(pageInfo);
    }

    /**
     * 单量或批量删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<Object> deleteCombo(@RequestBody List<Long> ids) {
        comboService.removeWithCommoditiesAndConfigs(ids);
        return Result.succeed(null);
    }

    /**
     * 单量或批量修改套餐状态
     *
     * @param status
     * @param ids
     * @return
     */
    @PutMapping("/{status}")
    public Result<Object> updateComboStatus(@PathVariable("status") Integer status, @RequestBody List<Long> ids) {
        comboService.updateStatusBatch(ids, status);
        return Result.succeed(null);
    }

    /**
     * 根据套餐id查询套餐信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<ComboDto> getComboById(@PathVariable("id") Long id) {
        ComboDto comboDto = comboService.getWithCommoditiesAndConfigsById(id);

        return comboDto != null ? Result.succeed(comboDto) : Result.fail("参数有误");
    }


    /**
     * 修改套餐信息
     *
     * @param comboDto
     * @param validResults
     * @return
     * @throws IOException
     */
    @PutMapping
    public Result<Object> updateCombo(@RequestBody @Validated ComboDto comboDto, BindingResult validResults) throws IOException {
        List<String> errMsg = ValidateData.getErrMsg(validResults);
        if (errMsg != null) {
            return Result.fail(errMsg.toString());
        }

        comboService.updateWithCommoditiesAndConfigs(comboDto);

        return Result.succeed(null);
    }
}
