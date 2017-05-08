package com.github.jackl.elk.service;

import com.github.jackl.elk.entity.Proxy;
import com.github.jackl.elk.entity.ProxyEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author jackl
 * @date 2017-05-08 17:03:11
 */
public interface ProxyService {
	
	ProxyEntity queryObject(Long id);
	
	List<Proxy> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);
	
	void save(ProxyEntity proxy);
	
	void update(ProxyEntity proxy);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
