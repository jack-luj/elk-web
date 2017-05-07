package com.github.jackl.elk.service;


import com.github.jackl.elk.entity.RoleEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/13.
 */
public interface RoleService {

    RoleEntity queryObject(Long roleId);

    List<RoleEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(RoleEntity role);

    void update(RoleEntity role);

    void deleteBatch(Long[] roleIds);
}
