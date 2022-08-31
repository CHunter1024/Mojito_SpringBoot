package com.freedom.mojito.util;

import java.util.Random;

/**
 * Description: 随机生成工具类
 * <p>CreateTime: 2022-07-14 下午 9:38</p>
 * <p>Email: 2396598264@qq.com</p>
 *
 * @author Chb
 */

public class RandomUtils {
    /**
     * 随机生成数字型字符串
     *
     * @param length 长度为4位或者6位
     * @return numStr
     */
    public static String getNumStr(int length) {
        int value;
        if (length == 4) {
            value = new Random().nextInt(10000);  // 生成随机数，[0, 10000)
            if (value < 1000) {
                value = value + 1000;  // 保证随机数为4位数字
            }
        } else if (length == 6) {
            value = new Random().nextInt(1000000);  // 生成随机数，[0, 1000000)
            if (value < 100000) {
                value = value + 100000;  // 保证随机数为6位数字
            }
        } else {
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return Integer.toString(value);
    }

    /**
     * 随机生成指定长度字符串（数字和字母组成）
     *
     * @param length 长度
     * @return str
     */
    public static String getStr(int length) {
        StringBuilder value = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int num = random.nextInt(2);
            // 0:数字, 1:字母
            if (num == 1) {
                // 0:大写字母, 1:小写字母
                int temp = random.nextInt(2) == 0 ? 65 : 97;
                value.append((char) (random.nextInt(26) + temp));
            } else {
                value.append((random.nextInt(10)));
            }
        }
        return value.toString();
    }
}
