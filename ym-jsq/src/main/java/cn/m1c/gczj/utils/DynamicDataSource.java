package cn.m1c.gczj.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * @date 2016年8月15日
 * @description 自定义数据源，继承自 org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource 主要是重载  determineCurrentLookupKey
 * @author  phil --> E-mail: s@m1c.cn
 * @corp m1c soft Co.,ltd
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDataSouce();
    }

}
