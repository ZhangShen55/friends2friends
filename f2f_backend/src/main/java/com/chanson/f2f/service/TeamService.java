package com.chanson.f2f.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chanson.f2f.model.domain.Team;
import com.chanson.f2f.model.domain.User;
import com.chanson.f2f.model.domain.dto.TeamQuery;
import com.chanson.f2f.model.domain.request.TeamJoinRequest;
import com.chanson.f2f.model.domain.request.TeamUpdateRequest;
import com.chanson.f2f.model.domain.vo.TeamUserVO;

import java.util.List;


/**
* @author ZhangShen
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-07-30 20:40:00
*/
public interface TeamService extends IService<Team> {
    long  addTeam(Team team, User loginUser);

    /**
     * 查询队伍
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, Boolean isAdmin);

    /**
     * 更新队伍信息
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    Boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);
}
