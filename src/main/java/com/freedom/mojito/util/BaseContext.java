package com.freedom.mojito.util;

/**
 * Description: 操作一个ThreadLocal的封装工具类
 * <p>ThreadLocal: 是当前线程的一个副本，可以将变量储存到该线程副本中</p>
 * <p>CreateTime: 2022-07-19 下午 12:03</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

@Deprecated
public class BaseContext {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 将当前用户Id存储到当前线程副本中
     *
     * @param id
     */
    public static void setCurrEmpId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 从当前线程副本中获取当前用户Id
     *
     * @return
     */
    public static Long getCurrEmpId() {
        return threadLocal.get();
    }

    /**
     * 销毁ThreadLocal，防止内存泄漏
     */
    public static void remove() {
        threadLocal.remove();
    }
}
