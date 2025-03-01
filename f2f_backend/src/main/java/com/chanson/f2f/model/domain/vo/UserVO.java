package com.chanson.f2f.model.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户返回前端包装类（脱敏）
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 3489906571122590874L;
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 电话
     */
    private String phone;

    /**
     * 描述
     */
    private String profile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0 - 正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    /**
     * 星球编号
     */
    private String planetCode;


    /**
     * 用户标签
     */
    private String tags;



}
