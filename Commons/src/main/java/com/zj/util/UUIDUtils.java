package com.zj.util;

import java.util.UUID;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 14:25
 */
public class UUIDUtils {

    public static String generateUUID() {
        return generateUUID(false);
    }

    public static String generateUUID(boolean toUpperCase) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        if(toUpperCase){
            return uuid.toUpperCase();
        }else{
            return uuid.toLowerCase();
        }
    }

}
