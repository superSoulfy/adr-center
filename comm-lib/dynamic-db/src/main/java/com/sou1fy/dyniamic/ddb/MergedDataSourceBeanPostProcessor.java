package com.sou1fy.dyniamic.ddb;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.JmxUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * 合并多数据源配置（来自xml和yml）
 */
@Slf4j
public class MergedDataSourceBeanPostProcessor implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {

    private static final String DATA_SOURCE_SET = "targetDataSources";
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        if (bean instanceof DynamicDataSource) {
            log.debug("配置多数据源...");
            MutiDataSourceProperties mutiDataSourceProperties = applicationContext.getBean(MutiDataSourceProperties.class);
            List<MutiDataSourceProperties.NamedDataSourceProperty> namedDataSourceProperties = mutiDataSourceProperties.getDbs();
            if (namedDataSourceProperties.size() > 0) {
                PropertyValue pv = pvs.getPropertyValue(DATA_SOURCE_SET);
                ManagedMap managedMap = null;
                if (pv == null) {
                    managedMap = new ManagedMap();
                    ((MutablePropertyValues) pvs).add(DATA_SOURCE_SET, managedMap);
                } else {
                    managedMap = (ManagedMap) pv.getValue();
                }
                Class<HikariDataSource> defaultClazz = HikariDataSource.class;
                ManagedMap finalManagedMap = managedMap;
                namedDataSourceProperties.forEach(dbConfig -> {
                    Object dbKey = dbConfig.getDbKey();
                    DataSource ds;
                    if (StringUtils.isNotEmpty(dbConfig.getJndiName())) {
                        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
                        dataSourceLookup.setJndiEnvironment(dbConfig.getJndiEnvironment());
                        ds = dataSourceLookup.getDataSource(dbConfig.getJndiName());
                        excludeMBeanIfNecessary(ds,"dataSource");
                    } else {
                        Class<? extends DataSource> clazz = dbConfig.getType();
                        if (null == clazz) {
                            dbConfig.setType(defaultClazz);
                        }
                        ds = dbConfig.initializeDataSourceBuilder().build();
                    }
                    Assert.isTrue(null == finalManagedMap.put(dbKey, ds), "存在重复配置的数据源:" + dbKey);
                });
                log.debug("多数据源配置完成，共{}个数据源。", finalManagedMap.size());
            }
        }
        return pvs;
    }

    private void excludeMBeanIfNecessary(Object candidate,String name) {
        try {
            MBeanExporter mbeanExporter = this.applicationContext.getBean(MBeanExporter.class);
            if (JmxUtils.isMBean(candidate.getClass())) {
                mbeanExporter.addExcludedBean(name);
            }
        } catch (NoSuchBeanDefinitionException ex) {
            // No exporter. Exclusion is unnecessary
        }
    }
}
