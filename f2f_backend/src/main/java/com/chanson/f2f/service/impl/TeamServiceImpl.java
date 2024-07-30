package com.chanson.f2f.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanson.f2f.mapper.TeamMapper;
import com.chanson.f2f.model.domain.Team;
import com.chanson.f2f.service.TeamService;

import org.springframework.stereotype.Service;

/**
* @author ZhangShen
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-07-30 20:40:00
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {

}




