package com.github.jackl.elk.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.jackl.elk.entity.Proxy;
import com.github.jackl.elk.utils.RedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.jackl.elk.dao.ProxyDao;
import com.github.jackl.elk.entity.ProxyEntity;
import com.github.jackl.elk.service.ProxyService;



@Service("proxyService")
public class ProxyServiceImpl implements ProxyService {
	@Autowired
	private ProxyDao proxyDao;
	@Autowired
	private RedisTool redisTool;

	private String redisKey="_proxies";
	@Override
	public ProxyEntity queryObject(Long id){
		return proxyDao.queryObject(id);
	}
	
	@Override
	public List queryList(Map<String, Object> map){
		int offset=(Integer)map.get("offset");
		int limit=(Integer)map.get("limit");
		long total=redisTool.lengthList(redisKey);
		int end=(int)(offset+limit>total?total:offset+limit);
		List proxyStrList = redisTool.rangeList(redisKey,offset,end-1);
		List proxyList=new ArrayList<>();
		for (int i=0;i<proxyStrList.size();i++){
			proxyList.add(JSON.parse((String)proxyStrList.get(i)));
		}
		return proxyList;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return (int)redisTool.lengthList(redisKey).longValue();
	}
	
	@Override
	public void save(ProxyEntity proxy){
		proxyDao.save(proxy);
	}
	
	@Override
	public void update(ProxyEntity proxy){
		proxyDao.update(proxy);
	}
	
	@Override
	public void delete(Long id){
		proxyDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		proxyDao.deleteBatch(ids);
	}
	
}
