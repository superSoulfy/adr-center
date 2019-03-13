package com.sou1fy.dyniamic.ddb;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 多数据源配置属性
 */
@Data
public class MutiDataSourceProperties {

    private List<NamedDataSourceProperty> dbs = new ArrayList<>();


    public static class NamedDataSourceProperty extends DataSourceProperties {
        /**
         * 数据源标识
         */
        private Object dbKey;

        private Properties jndiEnvironment;


        public Object getDbKey() {
            return dbKey;
        }

        public void setDbKey(Object dbKey) {
            this.dbKey = dbKey;
        }

        public Properties getJndiEnvironment() {
            return jndiEnvironment;
        }

        public void setJndiEnvironment(Properties jndiEnvironment) {
            this.jndiEnvironment = jndiEnvironment;
        }
    }
}
