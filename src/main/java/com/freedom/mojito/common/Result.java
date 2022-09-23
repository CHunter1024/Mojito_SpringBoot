package com.freedom.mojito.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: 服务器响应给客户端数据统一的封装类
 * <p>CreateTime: 2022-07-12 上午 11:58</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Data
@ApiModel("请求结果")
public class Result<T> {

    @ApiModelProperty("响应编码：1为成功，0或其他数字为失败")
    private Integer code;

    @ApiModelProperty("错误信息")
    private String msg;

    @ApiModelProperty("数据")
    private T data;

    /**
     * 服务器处理成功
     *
     * @param data 数据
     * @param <E>  数据的类型
     * @return 成功结果对象
     */
    public static <E> Result<E> succeed(E data) {
        Result<E> result = new Result<>();
        result.code = 1;
        result.data = data;
        return result;
    }

    /**
     * 服务器处理失败
     *
     * @param msg 错误信息
     * @param <E> 数据的类型
     * @return 失败结果对象
     */
    public static <E> Result<E> fail(String msg) {
        Result<E> result = new Result<>();
        result.code = 0;
        result.msg = msg;
        return result;
    }
}
