package com.chanson.f2f.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanson.f2f.common.ErrorCode;
import com.chanson.f2f.common.ResultUtils;
import com.chanson.f2f.exception.BusinessException;
import com.chanson.f2f.mapper.TeamMapper;
import com.chanson.f2f.model.domain.Team;
import com.chanson.f2f.model.domain.User;
import com.chanson.f2f.model.domain.UserTeam;
import com.chanson.f2f.model.domain.dto.TeamQuery;
import com.chanson.f2f.model.domain.enums.TeamStatusEnum;
import com.chanson.f2f.model.domain.request.TeamJoinRequest;
import com.chanson.f2f.model.domain.request.TeamUpdateRequest;
import com.chanson.f2f.model.domain.vo.TeamUserVO;
import com.chanson.f2f.model.domain.vo.UserVO;
import com.chanson.f2f.service.TeamService;

import com.chanson.f2f.service.UserService;
import com.chanson.f2f.service.UserTeamService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ZhangShen
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-07-30 20:40:00
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {


    @Resource
    private UserTeamService userTeamService;
    @Resource
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)  //开启事务 原子性
    public long addTeam(Team team, User loginUser) {
        //1. 请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2. 是否登录，未登录不允许创建
        long userId = loginUser.getId();
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //3. 校验信息
        //   1. 队伍人数 > 1 且 <= 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum <= 1 || maxNum >= 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        //   2. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //   3. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isBlank(description) || description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不满足要求");
        }
        //   4. status 是否公开（int）不传默认为 0（公开）
        int teamStatus = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamStatus);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //   5. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码不符合要求");
            }
        }
        //   6. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设置过期时间不符合要求");
        }
        //   7. 校验用户最多创建 5 个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍数量上限(5个)");
        }
        //4. 插入队伍信息到队伍表

        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();

        if (!result || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        //5. 插入用户  => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }

        return teamId;
    }

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, Boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamQuery != null) {
            //id name description maxNum userId status
            //1. 从请求参数中取出队伍名称等查询条件，如果存在则作为查询条件
            //根据队伍id查询
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            //根据队伍名称和队伍描述进行模糊查询
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }
            //根据队伍名模糊查询
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
            //根据描述模糊查询
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            //根据最大人数查询
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }
            //根据创建人id查询
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }
            //根据状态来查询
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                // 队伍状态值不存在 那么设定为公开
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && !statusEnum.equals(TeamStatusEnum.PUBLIC)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            queryWrapper.eq("status", statusEnum.getValue());
        }
        //不展示已过期的队伍
        queryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));

        //查询到 队伍信息
        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            //未查询到信息直接返回一个空集合
            return new ArrayList<>();
        }
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        //关联查询创建人的信息
        for (Team team : teamList) {
            //拿到创建人的userId
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            //查询该用户的信息
            User user = userService.getById(userId);
            TeamUserVO teamUserVO = new TeamUserVO();
            //将team信息复制到teamUserVO中
            BeanUtils.copyProperties(team, teamUserVO);
            if (user != null) {
                //脱敏用户信息(就是去掉不必要返回前端的属性)
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(teamUserVO, userVO);
                //将脱敏后的用户信息插入到set到teamUserVO createUser属性中
                teamUserVO.setCreateUser(userVO);
            }
            //将teamUserVO add 到list中
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"队伍不存在");
        }

        if (!oldTeam.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        //状态判断
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if(statusEnum.equals(TeamStatusEnum.SECRET)){
            if(StringUtils.isBlank(teamUpdateRequest.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"加密队伍密码不能为空");
            }
        }
        Team updateTeam = new Team();
        //将队伍信息 复制给包装类
        BeanUtils.copyProperties(teamUpdateRequest,updateTeam);
        boolean result = this.updateById(updateTeam);
        return result;
    }

    @Override
    public Boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        //其他人、未满、未过期，允许加入多个队伍，但是要有个上限  P0

        //判空
        if(teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = loginUser.getId();
        QueryWrapper<UserTeam> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("userId",userId);
        long userTeamNum = userTeamService.count(queryWrapper1);
        //1. 用户最多加入 5 个队伍
        if(userTeamNum >= 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入失败！最多只能加入5支队伍");
        }

        //2. 队伍必须存在，未过期的队伍 只能加入未满
        Long teamId = teamJoinRequest.getTeamId();
        Team team = this.getById(teamId);
        if(team == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"加入失败！队伍不存在");
        }
        Date expireTime = team.getExpireTime();
        if(expireTime != null && expireTime.before(new Date()) ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入失败！队伍已过期");
        }

        QueryWrapper<UserTeam> queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("teamId",teamId);
        long joinTeamUserNum = userTeamService.count(queryWrapper2);
        if (joinTeamUserNum >= team.getMaxNum()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入失败！队伍已满");
        }

        //3. 不能加入自己的队伍，
        if(team.getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入失败！无法加入自己的队伍");
        }
        //不能重复加入已加入的队伍（幂等性）
        QueryWrapper<UserTeam> queryWrapper3 = new QueryWrapper();
        queryWrapper3.eq("teamId",teamId);
        queryWrapper3.eq("userId",userId);
        long hasUserJoinTeam = userTeamService.count(queryWrapper3);
        if(hasUserJoinTeam > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入失败！您已在该队伍当中");
        }

        //4. 禁止加入私有的队伍  加入的队伍是加密的，必须密码匹配才可以
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(team.getStatus());
        String password = teamJoinRequest.getPassword();
        if(TeamStatusEnum.PRIVATE.equals(statusEnum)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入失败！私有队伍无法加入");
        }
        if(TeamStatusEnum.SECRET.equals(statusEnum)){
            if(StringUtils.isBlank(password) || !team.getPassword().equals(password)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入失败！输入密码错误");
            }
        }
        //6. 新增队伍 - 用户关联信息
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        boolean result = userTeamService.save(userTeam);

        return result;
    }
}




