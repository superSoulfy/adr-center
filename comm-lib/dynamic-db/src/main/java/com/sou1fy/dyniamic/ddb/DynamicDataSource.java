package com.sou1fy.dyniamic.ddb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 动态数据源
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.debug("----current dataSource key is [{}]------", DetermineDbKeyContext.currentDbKey());
        return DetermineDbKeyContext.currentDbKey();
    }
}
