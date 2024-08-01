package com.chanson.f2f.model.domain.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 加入队伍请求体
 *
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 3006314605835693776L;
    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 加入队伍密码
     */
    private String password;

}
