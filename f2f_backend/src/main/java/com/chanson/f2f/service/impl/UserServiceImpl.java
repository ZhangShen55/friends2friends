package com.chanson.f2f.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanson.f2f.common.ErrorCode;
import com.chanson.f2f.contant.UserConstant;
import com.chanson.f2f.exception.BusinessException;
import com.chanson.f2f.mapper.UserMapper;
import com.chanson.f2f.model.domain.User;
import com.chanson.f2f.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author <a href="https://github.com/lichanson">程序员鱼皮</a>
 * @from <a href="https://chanson.icu">编程导航知识星球</a>
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "chanson";

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @return 新用户 id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号过长");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编号重复");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    // [加入星球](https://www.code-nav.cn/) 从 0 到 1 项目实战，经验拉满！10+ 原创项目手把手教程、7 日项目提升训练营、60+ 编程经验分享直播、1000+ 项目经验笔记

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);
        // 4. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public List<User> searchUsersByTags(List<String> tagNameList)  {

        //通过SQL语句查询
        return searchUsersByTagsUseMemory(tagNameList);

        //通过内存查询
        //return searchUsersByTagsUseMemory(tagNameList,queryWrapper);

        //1.通过Sql语句进行查询
        /**
         QueryWrapper<User> queryWrapper = new QueryWrapper<>();
         //拼接and 查询
         for (String tagName : tagNameList) {
         //不断拼接
         queryWrapper = queryWrapper.like("tags", tagName);
         }

         List<User> userList = userMapper.selectList(queryWrapper);
         */
        /**
         //2.通过内存进行查询
         queryWrapper = new QueryWrapper<>();
         //拿到所有用户
         List<User> userList = userMapper.selectList(queryWrapper);
         Gson gson = new Gson();

         //在下面这个语法塘汇总返回false会被过滤掉 true会被保留
         return userList.stream().filter(user -> {
         String tags = user.getTags();
         //判空
         if (StringUtils.isBlank(tags)) {
         //标签为null则就被过滤掉
         return false;
         }
         //将标签json反序列化set<String>
         Set<String> userTagsSet = gson.fromJson(tags, new TypeToken<Set<String>>() {
         }.getType());
         //判断这个set集合中 是否有传入的tag
         for (String tag : tagNameList) {
         if (!userTagsSet.contains(tag)) {
         return false;
         }
         }
         return true;
         }).map(this::getSafetyUser).collect(Collectors.toList());
         */


    }


    /**
     * 根据tag查询用户 通过内存进行查询
     *
     * @param tagNameList
     * @return
     */
    public List<User> searchUsersByTagsUseMemory(List<String> tagNameList) {
        //首先判断tag是否空
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        Long startTime = System.currentTimeMillis();
        //首先查询到所有用户
        List<User> users = userMapper.selectList(queryWrapper);
        //拿到所有用户的标签 进行序列化
        Gson gson = new Gson();
        // false 为过滤成功 ;true 保留当前user 进行返回
        List<User> usersList = users.stream().filter(user -> {
            String tags = user.getTags();
            //判断当前遍历用户的tag是否为空
            if (StringUtils.isBlank(tags)) {
                return false;
            }
            //序列化
            Set<String> tagsSet = gson.fromJson(tags, new TypeToken<Set<String>>() {
            }.getType());
            //对集合判空
            tagsSet = Optional.ofNullable(tagsSet).orElse(new HashSet<>());
            //遍历查询穿入的tag是否存在于tagsSet当中
            for (String tag : tagNameList) {
                if (!tagsSet.contains(tag)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
//        log.info("USE Memory 耗时:"+(System.currentTimeMillis()-startTime));
        return usersList;
    }

    /**
     * 根据tag查询用户 使用SQL语句进行查询
     *
     * @param tagNameList
     * @return
     */
    @Deprecated
    private List<User> searchUsersByTagsUseSQL(List<String> tagNameList) {
        //首先判断tag是否空
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        Long startTime = System.currentTimeMillis();
        //拼接and 查询
        for (String tagName : tagNameList) {
            //不断拼接
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        List<User> usersList = userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
//        log.info("USE Sql 耗时:"+(System.currentTimeMillis()-startTime));
        return usersList;
    }
}




































