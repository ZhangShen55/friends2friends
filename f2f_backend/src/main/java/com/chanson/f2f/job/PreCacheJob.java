package com.chanson.f2f.job;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chanson.f2f.model.domain.User;
import com.chanson.f2f.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


/**
 * 缓存预热任务
 */
@Slf4j
@Component
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    //白名单用户
    //todo 需要通过后台拿到热门活跃用户的 这里只是做个示范将其写死
    private List<Long> mainUserList = Arrays.asList(1L,5L,8L);

    //每天中午12点进行一次缓存预热
    @Scheduled(cron = "0 0 12 * * * ")
    private void doCacheRecommendUser(){
        //拿到所有白名单用户的id
        for (Long userId : mainUserList) {
            String redisKey = String.format("sylvan:user:recommed:%s",userId);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            //pageNum：指定页码 pageSize：每页数据量
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            try {
                valueOperations.set(redisKey,userPage);
            } catch (Exception e) {
                log.error("Redis set Key-Value error",e);
            }
        }
    }
}
