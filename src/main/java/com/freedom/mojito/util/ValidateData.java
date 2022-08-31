package com.freedom.mojito.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 校验数据工具类
 * <p>CreateTime: 2022-07-14 下午 9:38</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public class ValidateData {
    /**
     * 获取校验结果错误信息
     *
     * @param result 校验结果
     * @return 错误信息，无错误返回null
     */
    public static List<String> getErrMsg(BindingResult result) {
        if (result == null) {
            return null;
        }
        // 如果 BindingResult 的 hasErrors 方法返回 true，则表示有错误信息
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            List<String> errMsg = new ArrayList<>(allErrors.size());
            // 遍历错误信息
            for (ObjectError error : allErrors) {
                errMsg.add(error.getDefaultMessage());
            }
            return errMsg;
        }
        return null;
    }
}
