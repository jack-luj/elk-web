package com.github.jackl.elk.controller;


import com.github.jackl.elk.entity.UserEntity;
import com.github.jackl.elk.service.UserRoleService;
import com.github.jackl.elk.service.UserService;
import com.github.jackl.elk.utils.JsonResult;
import com.github.jackl.elk.utils.PageUtils;
import com.github.jackl.elk.utils.ShiroUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/13.
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends AbstractController{
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public JsonResult login(@RequestParam(value = "username",required = false) String username,
                            @RequestParam(value = "password",required = false) String password,
                            @RequestParam(value = "captcha",required = false) String captcha){

      /*  String kaptcha = ShiroUtils.getKaptcha("");
        if(!captcha.equalsIgnoreCase(kaptcha)){
            return JsonResult.error("验证码不正确");
        }*/

        try{
            Subject subject = ShiroUtils.getSubject();
            //sha256加密
            password = new Sha256Hash(password).toHex();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        }catch (UnknownAccountException e) {
            return JsonResult.error(e.getMessage());
        }catch (IncorrectCredentialsException e) {
            return JsonResult.error(e.getMessage());
        }catch (LockedAccountException e) {
            return JsonResult.error(e.getMessage());
        }catch (AuthenticationException e) {
            return JsonResult.error("账户验证失败");
        }
        ShiroUtils.getSubject().getSession().setTimeout(-1000);//超时时间

        return JsonResult.ok();
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public JsonResult logout(){
        UserEntity currentUser = ShiroUtils.getUserEntity();
        if(currentUser!=null) {
            _logger.info("用户" + ShiroUtils.getUserEntity().getUsername() + "退出登陆");
        }
        ShiroUtils.logout();
        return JsonResult.ok();
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public JsonResult info(){
        return JsonResult.ok().put("user", getUser());
    }

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public JsonResult list(String username,Integer page, Integer limit){
        Map<String, Object> map = new HashMap<>();
        map.put("username",username);
        map.put("offset", (page - 1) * limit);
        map.put("limit", limit);

        //查询列表数据
        List<UserEntity> userList = userService.queryList(map);
        int total = userService.queryTotal(map);

        PageUtils pageUtil = new PageUtils(userList, total, limit, page);
        return JsonResult.ok().put("page", pageUtil);
    }


    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public JsonResult info(@PathVariable("userId") Long userId){
        UserEntity user = userService.queryObject(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = userRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return JsonResult.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public JsonResult save(@RequestBody UserEntity user){
        if(StringUtils.isBlank(user.getUsername())){
            return JsonResult.error("用户名不能为空");
        }
        if(StringUtils.isBlank(user.getPassword())){
            return JsonResult.error("密码不能为空");
        }

        userService.save(user);

        return JsonResult.ok();
    }

    /**
     * 修改用户
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public JsonResult update(@RequestBody UserEntity user){
        if(StringUtils.isBlank(user.getUsername())){
            return JsonResult.error("用户名不能为空");
        }

        userService.update(user);

        return JsonResult.ok();
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public JsonResult delete(@RequestBody Long[] userIds){
        if(ArrayUtils.contains(userIds, 1L)){
            return JsonResult.error("系统管理员不能删除");
        }

        if(ArrayUtils.contains(userIds, getUserId())){
            return JsonResult.error("当前用户不能删除");
        }

        userService.deleteBatch(userIds);

        return JsonResult.ok();
    }


}
