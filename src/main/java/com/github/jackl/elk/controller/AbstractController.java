package com.github.jackl.elk.controller;


import com.github.jackl.elk.entity.UserEntity;
import com.github.jackl.elk.utils.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jackl on 2017/2/13.
 */
public abstract class AbstractController {
    protected Logger _logger = LoggerFactory.getLogger(getClass());

    protected UserEntity getUser() {
        return ShiroUtils.getUserEntity();
    }

    protected Long getUserId() {
        return getUser().getUserId();
    }
}
