package com.koi151.msproperty.utils;

import com.github.javafaker.Faker;

public class NumberUtil {
    public static int generateRandomInteger(int min, int max) {
        Faker faker = new Faker();
        return faker.number().numberBetween(min, max);
    }
}
