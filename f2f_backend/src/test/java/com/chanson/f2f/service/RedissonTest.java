package com.chanson.f2f.service;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedissonTest {


    @Resource
    private RedisTemplate redisTemplate;



    @Test
    public void test(){


    }
}
