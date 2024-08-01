package com.chanson.f2f.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chanson.f2f.common.BaseResponse;
import com.chanson.f2f.common.ErrorCode;
import com.chanson.f2f.common.ResultUtils;
import com.chanson.f2f.exception.BusinessException;
import com.chanson.f2f.model.domain.Team;
import com.chanson.f2f.model.domain.User;
import com.chanson.f2f.model.domain.dto.TeamQuery;
import com.chanson.f2f.model.domain.request.TeamAddRequest;
import com.chanson.f2f.model.domain.request.TeamJoinRequest;
import com.chanson.f2f.model.domain.request.TeamUpdateRequest;
import com.chanson.f2f.model.domain.vo.TeamUserVO;
import com.chanson.f2f.service.TeamService;
import com.chanson.f2f.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/team")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class TeamrController {
    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //获取当前loginUser
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest,team);

        Long teamId = teamService.addTeam(team,loginUser);
        return ResultUtils.success(teamId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam( @RequestBody long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        //这里返回值一定是true
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //获得登录用户
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest,loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        //这里返回值一定是true
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById( Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //这里返回值一定是true
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery,HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //判断是否为管理员
        Boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> resultList = teamService.listTeams(teamQuery,isAdmin);
        if (resultList == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "查询失败");
        }
        return ResultUtils.success(resultList);
    }

//    @GetMapping("/list")
//    public BaseResponse<List<Team>> listTeams(TeamQuery teamQuery) {

//        List<Team> resultList = teamService.listTeams(queryWrapper);
//        if (resultList == null) {
//            throw new BusinessException(ErrorCode.NULL_ERROR, "查询失败");
//        }
//        return ResultUtils.success(resultList);
//    }


    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery,team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        if (resultPage == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "查询失败");
        }
        return ResultUtils.success(resultPage);
    }


    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest,HttpServletRequest request){
        if(teamJoinRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        //获得登录用户
        User loginUser = userService.getLoginUser(request);
        Boolean result =teamService.joinTeam(teamJoinRequest,loginUser);
        return ResultUtils.success(result);

    }
}
