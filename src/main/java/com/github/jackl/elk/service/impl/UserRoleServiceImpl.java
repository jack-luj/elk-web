package com.github.jackl.elk.service.impl;


import com.github.jackl.elk.dao.UserRoleDao;
import com.github.jackl.elk.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/13.
 */
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        if(roleIdList.size() == 0){
            return ;
        }

        //先删除用户与角色关系
        userRoleDao.delete(userId);

        //保存用户与角色关系
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("roleIdList", roleIdList);
        userRoleDao.save(map);
    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return userRoleDao.queryRoleIdList(userId);
    }

    @Override
    public void delete(Long userId) {
        userRoleDao.delete(userId);
    }
}
