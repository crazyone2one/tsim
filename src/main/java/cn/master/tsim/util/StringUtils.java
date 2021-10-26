package cn.master.tsim.util;

import java.util.Random;

/**
 * @author Created by 11's papa on 2021/10/18
 * @version 1.0.0
 */
public class StringUtils {
    /**
     * 生成指定长度的code
     *
     * @param codeLength code长度
     * @return : java.lang.String
     */
    public static String randomCode(int codeLength) {
        String str = "0123456789abcdefghijklmnopqrstvuwxyzABCDEFGHIJKLMNOPQRSTVWXYZ";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            char temp = str.charAt(random.nextInt(str.length()));
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}
