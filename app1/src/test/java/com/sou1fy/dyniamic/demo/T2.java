package com.sou1fy.dyniamic.demo;

import java.util.HashMap;

public class T2 extends HashMap {
    public static void main(String[] args) {
        System.out.println(new T2().t());
        System.out.println("keep running");
    }

    int t() {
        int x = 1;


        throw new RuntimeException("aaa");
    }
}
//        static class MyConditional extends SpringBootCondition {
//
//            @Override
//            public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
//                if (context.getEnvironment().containsProperty("dynamic.datasource.dbs")) {
//                    //如果environment中的值与指定的value一致，则返回true
//                    return new ConditionOutcome(true, "ok");
//                }
//                return new ConditionOutcome(false, "error");
//
//            }
//        }