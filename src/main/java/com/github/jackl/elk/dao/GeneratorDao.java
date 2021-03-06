package com.github.jackl.elk.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/17.
 */
public interface GeneratorDao {

    List<Map<String, Object>> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);
}
