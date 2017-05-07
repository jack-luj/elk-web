package com.github.jackl.elk.service;

import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/17.
 */
public interface GeneratorService {
    List<Map<String, Object>> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);

    /**
     * 生成代码
     */
    byte[] generatorCode(String[] tableNames);
}
