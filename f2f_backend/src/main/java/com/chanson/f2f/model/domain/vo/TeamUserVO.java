package com.chanson.f2f.model.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍用户包装类（脱敏）
 */
@Data
public class TeamUserVO implements Serializable {
    private static final long serialVersionUID = 4306047856510201938L;
    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *更新时间
     */
    private Date updateTime;

    UserVO createUser;

}
