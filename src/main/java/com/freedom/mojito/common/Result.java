package com.freedom.mojito.common;

import com.freedom.mojito.constant.ResultCode;
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

    @ApiModelProperty("响应编码：20000(成功),50000(失败),40001(未登录),40003(无权限)")
    private Integer code;

    @ApiModelProperty("信息")
    private String message;

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
        result.code = ResultCode.SUCCESS;
        result.data = data;
        return result;
    }

    /**
     * 服务器处理失败
     *
     * @param message 信息
     * @param <E>     数据的类型
     * @return 失败结果对象
     */
    public static <E> Result<E> fail(String message) {
        Result<E> result = new Result<>();
        result.code = ResultCode.FAILURE;
        result.message = message;
        return result;
    }

    /**
     * 未登录
     *
     * @param message 信息
     * @return 未登录结果对象
     */
    public static Result<Object> notLogin(String message) {
        Result<Object> result = new Result<>();
        result.code = ResultCode.NOT_LOGIN;
        result.message = message;
        return result;
    }

    /**
     * 没有权限禁止访问
     *
     * @param message 信息
     * @return 禁止访问结果对象
     */
    public static Result<Object> forbid(String message) {
        Result<Object> result = new Result<>();
        result.code = ResultCode.FORBIDDEN;
        result.message = message;
        return result;
    }
}
