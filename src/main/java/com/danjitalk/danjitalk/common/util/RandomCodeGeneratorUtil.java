package com.danjitalk.danjitalk.common.util;

import java.security.SecureRandom;

public class RandomCodeGeneratorUtil {

    // 랜덤으로 (length)자리 숫자 생성
    public static int generateRandomNumber(int length) {
        SecureRandom secureRandom = new SecureRandom();
        int lowerLimit = (int) Math.pow(10, length - 1);
        int upperLimit = (int) Math.pow(10, length);
        return secureRandom.nextInt(upperLimit - lowerLimit) + lowerLimit;
    }
}
