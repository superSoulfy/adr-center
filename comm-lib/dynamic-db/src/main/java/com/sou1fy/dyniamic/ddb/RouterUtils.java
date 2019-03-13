package com.sou1fy.dyniamic.ddb;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 编程式动态数据源操作
 */
public class RouterUtils {
    public void excute(Object key, WrapperCaller caller) {
        Assert.isTrue(null != key && StringUtils.isNotEmpty(key.toString()), "数据源标识不能为空");
        DetermineDbKeyContext.routeTo(key);
        try {
            caller.call();
        } finally {
            DetermineDbKeyContext.restoreDbKey();
        }
    }

    @FunctionalInterface
    public interface WrapperCaller {
        void call();
    }
}
