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

	String findProxiesKey="_find_proxies";
	String availableProxiesKey="_available_proxies";
	@Override
	public ProxyEntity queryObject(Long id){
		return proxyDao.queryObject(id);
	}
	
	@Override
	public List queryList(Map<String, Object> map){
		int offset=(Integer)map.get("offset");
		int limit=(Integer)map.get("limit");
		String type=(String)map.get("type");
		String _key="";
		if("all".equals(type)){
			_key=findProxiesKey;
		}else{
			_key=availableProxiesKey;
		}
		long total=redisTool.lengthList(_key);
		int end=(int)(offset+limit>total?total:offset+limit);
		List proxyStrList = redisTool.rangeList(_key,offset,end-1);
		List proxyList=new ArrayList<>();
		for (int i=0;i<proxyStrList.size();i++){
			proxyList.add(JSON.parseObject((String)proxyStrList.get(i),Proxy.class));
		}
		return proxyList;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		String type=(String)map.get("type");
		String _key="";
		if("all".equals(type)){
			_key=findProxiesKey;
		}else{
			_key=availableProxiesKey;
		}

		return (int)redisTool.lengthList(_key).longValue();
	}
	
	@Override
	public void save(Proxy proxy){
		String _key = "";
		//_key = findProxiesKey;
		_key = availableProxiesKey;
		redisTool.inList(_key, JSON.toJSONString(proxy));
	}
	
	@Override
	public void update(Proxy proxy){
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
