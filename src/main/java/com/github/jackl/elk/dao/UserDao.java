package com.github.jackl.elk.dao;


import com.github.jackl.elk.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/13.
 */
public interface UserDao extends BaseDao<UserEntity>{

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 根据用户名，查询系统用户
     */
    UserEntity queryByUserName(String username);

    /**
     * 修改密码
     */
    int updatePassword(Map<String, Object> map);

}
