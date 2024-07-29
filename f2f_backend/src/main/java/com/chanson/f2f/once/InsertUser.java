package com.chanson.f2f.once;

import com.chanson.f2f.mapper.UserMapper;
import com.chanson.f2f.model.domain.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import javax.annotation.Resource;

@Component
public class InsertUser {

    @Resource
    private UserMapper userMapper;

    //initialDelay = 5000 SpringBoot启动5秒后进行 ；fixedRate = Long.MAX_VALUE Long.MAX_VALUE时间后再执行该方法
//    @Scheduled(initialDelay = 5000,fixedRate = Long.MAX_VALUE)
    private void insertUser(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("zhangshen");
            user.setUserAccount("fakezhangs");
            user.setAvatarUrl("https://himg.bdimg.com/sys/portraitn/item/public.1.e137c1ac.yS1WqOXfSWEasOYJ2-0pvQ");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("17777777777");
            user.setProfile("低头赶路,敬事如仪");
            user.setEmail("17777777777@88.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("55");
            user.setTags("[\"Java\",\"Python\",\"男\",\"应届生\",\"书法生\"]");
            userMapper.insert(user);
        }
        stopWatch.stop();
        stopWatch.getLastTaskTimeMillis();
    }
}
