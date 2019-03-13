package com.sou1fy.dyniamic.ddb;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Aspect
public class DynamicDataSourceProcessAop {

    private static final List<Class> BASE_TYPE = Arrays.asList(Integer.class, String.class, Long.class);
    private static final List<Class> SKIP_TYPE = Arrays.asList(java.util.Date.class, java.sql.Date.class,
            ArrayList.class,HashMap.class,BigDecimal.class);

    @Pointcut("@annotation(com.sou1fy.dyniamic.ddb.DBRouter)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        routeToKey(joinPoint);
        Object obj;
        try {
            obj = joinPoint.proceed();
        } finally {
            DetermineDbKeyContext.restoreDbKey();
        }
        return obj;
    }

    public void routeToKey(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DBRouter dbRouter = method.getAnnotation(DBRouter.class);
        Object explicitDbKey = dbRouter.explicitDbKey();
        if (StringUtils.isNotEmpty(explicitDbKey.toString())) {
            DetermineDbKeyContext.routeTo(explicitDbKey);
        }
        Object[] args = joinPoint.getArgs();
        int argIndex = dbRouter.argIndex();
        if (argIndex > -1 && argIndex < args.length - 1) {
            String searchField = dbRouter.searchField();
            Object param = args[argIndex];
            if (acceptType(param)) {
                DetermineDbKeyContext.routeTo(param.toString());
            } else {
                //复杂pojo
                String key = doSearchValue(param, searchField);
                if (key == null) {

                }
            }
        } else {
            throw new RuntimeException("非法的参数下标指定值");
        }
    }

    public String doSearchValue(Object arg, String searchField) {
        if (null == arg) {
            return null;
        }
        Field[] fields = arg.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object val = null;
            try {
                val = field.get(arg);
            } catch (IllegalAccessException e) {
                log.error("访问字段获取路由值异常", e);
            }
            if (field.getType().isPrimitive() && BASE_TYPE.contains(field.getType())) {
                if (!searchField.equals(field.getName())) {
                    continue;
                }
                return null != val ? val.toString() : null;
            } else if (!SKIP_TYPE.contains(field.getType())) {
//                if (Collection.class.isAssignableFrom(field.getType())) {
//
//                } else if (Map.class.isAssignableFrom(field.getType())) {
//
//                }
                String keyVal = doSearchValue(val, searchField);
                if (StringUtils.isNotEmpty(keyVal)) {
                    return keyVal;
                }
            }
        }
        return null;
    }

    private boolean acceptType(Object param) {
        return null != param && (param.getClass().isPrimitive() || BASE_TYPE.contains(param.getClass()));
    }
}
