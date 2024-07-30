package com.chanson.f2f.service;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {


    @Resource
    private RedisTemplate redisTemplate;



    @Test
    public void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();

//        valueOperations.set("name","zhangshen");
//        valueOperations.set("age",27);
//        valueOperations.set("work","中移");
        redisTemplate.delete("name");
        redisTemplate.delete("age");
        redisTemplate.delete("work");

    }
}
