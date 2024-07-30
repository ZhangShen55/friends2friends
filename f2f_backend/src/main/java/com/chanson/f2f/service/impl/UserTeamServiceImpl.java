package com.chanson.f2f.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanson.f2f.mapper.UserTeamMapper;
import com.chanson.f2f.model.domain.UserTeam;
import com.chanson.f2f.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author ZhangShen
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-07-30 20:40:20
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




