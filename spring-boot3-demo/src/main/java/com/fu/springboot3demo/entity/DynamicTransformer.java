package com.fu.springboot3demo.entity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DynamicTransformer {

    // 层级配置定义
    private final List<LevelConfig> levelConfigs;
    private final Map<String, Function<Object, Object>> fieldFormatters; // 字段格式化器

    // 私有构造函数
    private DynamicTransformer(List<LevelConfig> levelConfigs, Map<String, Function<Object, Object>> fieldFormatters) {
        this.levelConfigs = levelConfigs;
        this.fieldFormatters = fieldFormatters != null ? fieldFormatters : Collections.emptyMap();
    }

    // 转换入口方法
    public List<Map<String, Object>> transform(List<Map<String, Object>> sourceData) {
        return processLevel(sourceData, 0);
    }

    // 迭代处理层级（替代递归）
    private List<Map<String, Object>> processLevel(List<Map<String, Object>> items, int currentLevel) {
        if (currentLevel >= levelConfigs.size()) {
            return Collections.emptyList(); // 终止条件
        }

        LevelConfig config = levelConfigs.get(currentLevel);

        // 按复合分组键分组（保留数据库顺序）
        LinkedHashMap<List<Object>, List<Map<String, Object>>> grouped = items.stream()
                .collect(Collectors.groupingBy(
                        item -> Arrays.stream(config.groupKeyFields)
                                .map(field -> formatFieldValue(field, item.get(field)))
                                .collect(Collectors.toList()),
                        LinkedHashMap::new, // 保留插入顺序
                        Collectors.toList()
                ));

        // 处理当前层级
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<List<Object>, List<Map<String, Object>>> entry : grouped.entrySet()) {
            Map<String, Object> levelMap = new LinkedHashMap<>();

            // 添加分组键字段（按照代码顺序插入）
            List<Object> keys = entry.getKey();
            for (int i = 0; i < keys.size(); i++) {
                String fieldName = config.groupKeyFields[i];
                String mappedFieldName = config.fieldMappings.getOrDefault(fieldName, fieldName);
                Object value = keys.get(i);
                levelMap.put(mappedFieldName, value); // 保留原始类型
            }

            // 处理子层级
            if (config.childFieldName != null) {
                List<Map<String, Object>> childLevel = processLevel(entry.getValue(), currentLevel + 1);
                if (!childLevel.isEmpty()) {
                    levelMap.put(config.childFieldName, childLevel);
                }
            }

            result.add(levelMap);
        }

        return result;
    }

    // 格式化字段值（保留原始类型）
    private Object formatFieldValue(String fieldName, Object value) {
        if (value == null) {
            return null; // 空值处理
        }
        // 应用字段格式化器
        Function<Object, Object> formatter = fieldFormatters.get(fieldName);
        if (formatter != null) {
            value = formatter.apply(value);
        }
        return value; // 保留原始类型
    }

    // 层级配置类
    private static class LevelConfig {
        private final String childFieldName; // 子级字段名（null表示无下一级）
        private final Map<String, String> fieldMappings; // 字段名映射
        private final String[] groupKeyFields; // 分组键字段名

        public LevelConfig(String childFieldName, Map<String, String> fieldMappings, String... groupKeyFields) {
            this.childFieldName = childFieldName;
            this.fieldMappings = fieldMappings != null ? fieldMappings : Collections.emptyMap();
            this.groupKeyFields = groupKeyFields;
        }
    }

    // 构建器类（支持链式调用）
    public static class Builder {
        private final List<LevelConfig> configs = new ArrayList<>();
        private final Map<String, Function<Object, Object>> fieldFormatters = new HashMap<>();

        /**
         * 层级构建
         * @param childFieldName 集合名称
         * @param fieldMappings 需要替换的变量名Map<String, String>
         * @param groupKeyFields 数据库查询结果别名
         */
        public Builder addLevel(String childFieldName, Map<String, String> fieldMappings, String... groupKeyFields) {
            configs.add(new LevelConfig(childFieldName, fieldMappings, groupKeyFields));
            return this;
        }

        /**
         * 字段值转换
         * 如：日期格式化、枚举值转文本
         * @param fieldName 数据库查询结果别名
         * @param formatter 转换后的值
         */
        public Builder addFieldFormatter(String fieldName, Function<Object, Object> formatter) {
            fieldFormatters.put(fieldName, formatter);
            return this;
        }

        /**
         * 结束链
         */
        public DynamicTransformer build() {
            return new DynamicTransformer(configs, fieldFormatters);
        }
    }

}