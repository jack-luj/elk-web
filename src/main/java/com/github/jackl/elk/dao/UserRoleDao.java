package com.github.jackl.elk.dao;


import com.github.jackl.elk.entity.UserRoleEntity;

import java.util.List;

/**
 * Created by jackl on 2017/2/13.
 */
public interface UserRoleDao extends BaseDao<UserRoleEntity>{

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

}
