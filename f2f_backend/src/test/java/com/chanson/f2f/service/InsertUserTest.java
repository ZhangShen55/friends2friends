package com.chanson.f2f.service;


import com.chanson.f2f.mapper.UserMapper;
import com.chanson.f2f.model.domain.User;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class InsertUserTest {


    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;


    /**
     * 单次插入数据
     */
    @Test
    public void insertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("张太师");
            user.setUserAccount("fakezhangsshen");
            user.setAvatarUrl("https://himg.bdimg.com/sys/portraitn/item/public.1.e137c1ac.yS1WqOXfSWEasOYJ2-0pvQ");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("15555555555");
            user.setProfile("永和九年岁在葵丑");
            user.setEmail("15555555555@88.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("77");
            user.setTags("[\"Java\",\"Python\",\"男\",\"应届生\",\"书法生\"],\"单身\"]");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());

    }


    /**
     * 分批次一次插入1000条数据
     */
    @Test
    public void insertUserByBatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        ArrayList<User> userList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("张雑尢");
            user.setUserAccount("cheungshen");
            user.setAvatarUrl("https://himg.bdimg.com/sys/portraitn/item/public.1.e137c1ac.yS1WqOXfSWEasOYJ2-0pvQ");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("12222222222");
            user.setProfile("年年欲惜春今年又苦雨");
            user.setEmail("12222222222@88.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("22");
            user.setTags("[\"Java\",\"Go\",\"男\",\"应届生\",\"书法生\",\"单身\",\"手动挡\"]");
            userList.add(user);
        }
        //分批次一次插入1000填数据
        userService.saveBatch(userList, 100000);
        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());
    }

    /**
     * 多线程插入数据
     */
    @Test
    public void insertUserByConcurrency() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 10000;

        List<User> userList = new ArrayList<>();
        List<CompletableFuture> futureList = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 10; i++) {
            int j = 0;
            while (true) {
                j++;
                User user = new User();
                user.setUsername("张雑尢");
                user.setUserAccount("zhangshen");
                user.setAvatarUrl("https://himg.bdimg.com/sys/portraitn/item/public.1.e137c1ac.yS1WqOXfSWEasOYJ2-0pvQ");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setPhone("12222222222");
                user.setProfile("年年欲惜春今年又苦雨");
                user.setEmail("12222222222@88.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("22");
                user.setTags("[\"Java\",\"Go\",\"男\",\"应届生\",\"书法生\",\"单身\",\"手动挡\"]");
                userList.add(user);
                if (j % INSERT_NUM == 0) {
                    break;
                }
            }
        //异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                //打印当前线程的名称
                System.out.println(Thread.currentThread().getName());
                userService.saveBatch(userList, 1000);
            });
            // 乘10次
            futureList.add(future);
        }
        //执行
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());
    }



    /**
     * 自定义线程池 插入数据
     */
    @Test
    public void insertUserByCreatThreadPool() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 10000;
        //定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(40, 60, 10000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10000));
        List<User> userList = new ArrayList<>();
        List<CompletableFuture> futureList = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 10; i++) {
            int j = 0;
            while (true) {
                j++;
                User user = new User();
                user.setUsername("假张s");
                user.setUserAccount("zhangshen");
                user.setAvatarUrl("https://himg.bdimg.com/sys/portraitn/item/public.1.e137c1ac.yS1WqOXfSWEasOYJ2-0pvQ");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setPhone("12222222222");
                user.setProfile("年年欲惜春今年又苦雨");
                user.setEmail("12222222222@88.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("22");
                user.setTags("[\"Java\",\"Go\",\"男\",\"应届生\",\"书法生\",\"单身\",\"手动挡\"]");
                userList.add(user);
                if (j % INSERT_NUM == 0) {
                    break;
                }
            }
            //异步执行 使用自定义线程池
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                //打印当前线程的名称
                System.out.println(Thread.currentThread().getName());
                userService.saveBatch(userList, 100);
            },threadPoolExecutor);
            // 乘10次
            futureList.add(future);
        }
        //执行
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();

        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());
    }


}
