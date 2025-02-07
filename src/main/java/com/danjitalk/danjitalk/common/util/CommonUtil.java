package com.danjitalk.danjitalk.common.util;

import java.util.UUID;

public class CommonUtil {

    public static String generatedUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
