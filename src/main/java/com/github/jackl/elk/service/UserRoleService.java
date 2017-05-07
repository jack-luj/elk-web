package com.github.jackl.elk.service;

import java.util.List;

/**
 * Created by jackl on 2017/2/13.
 */
public interface UserRoleService{

        void saveOrUpdate(Long userId, List<Long> roleIdList);

        /**
         * 根据用户ID，获取角色ID列表
         */
        List<Long> queryRoleIdList(Long userId);

        void delete(Long userId);
}
