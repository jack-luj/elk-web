package com.github.jackl.elk.shiro;

import com.github.jackl.elk.utils.RedisTool;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

/**
 * 实现session基于redis的session存储
 * Created by jackl on 2017/2/23.
 */
public class SessionRedisImpl  extends EnterpriseCacheSessionDAO {
    protected Logger _logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTool redisTool;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        _logger.info("doCreate sessionId>>>>>>>>" + sessionId);

        //将session存入redis
        if(session != null){
            redisTool.saveValueObject(session.getId().toString(),sessionToByte(session), -1);
        }

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        _logger.debug("doReadSession sessionId>>>>>>>>" + sessionId);

        Session session = super.doReadSession(sessionId);
        //若当前session为空 则去redis查询会话是否存在
        if(session == null){
            Object obj = redisTool.getValueObject(sessionId.toString());
            if(obj != null){
                session = byteToSession((byte[]) obj);
            }
        }

        return session;
    }

    @Override
    protected void doUpdate(Session session) {
        _logger.debug("doUpdate sessionId>>>>>>>>" + session.getId());

        super.doUpdate(session);

        //更新会话状态
        redisTool.saveValueObject(session.getId().toString(), sessionToByte(session), -1);

    }

    @Override
    protected void doDelete(Session session) {
        _logger.info("doDelete sessionId>>>>>>>>" + session.getId());

        super.doDelete(session);
        //清除redis中session信息
        redisTool.delValueString(session.getId().toString());
    }

    // 把session对象转化为byte保存到redis中
    private byte[] sessionToByte(Session session){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(session);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            _logger.info(e.getMessage());
        }
        return bytes;
    }

    // 把byte还原为session
    private Session byteToSession(byte[] bytes){
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream in;
        Session session = null;
        try {
            in = new ObjectInputStream(bi);
            Object obj = in.readObject();
            if(obj != null){
                session = (Session) obj;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            _logger.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            _logger.info(e.getMessage());
        }

        return session;
    }

}
