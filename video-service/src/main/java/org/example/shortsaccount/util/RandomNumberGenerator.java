package org.example.shortsaccount.util;
import java.security.SecureRandom;

public class RandomNumberGenerator {
    public int randomNumberGenerator(int maxSize){
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(1,maxSize + 1);
    }
}
