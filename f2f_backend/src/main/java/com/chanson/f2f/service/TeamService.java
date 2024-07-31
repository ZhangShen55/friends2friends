package com.chanson.f2f.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chanson.f2f.model.domain.Team;
import com.chanson.f2f.model.domain.User;


/**
* @author ZhangShen
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-07-30 20:40:00
*/
public interface TeamService extends IService<Team> {
    long  addTeam(Team team, User loginUser);
}
