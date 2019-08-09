package com.sunrun.emailanalysis.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
//@Aspect
public class ApplicationAop {
    private Logger logger = LoggerFactory.getLogger(ApplicationAop.class);

    private static String[] types = {"java.lang.Integer", "java.lang.Double",
            "java.lang.Float", "java.lang.Long", "java.lang.Short",
            "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",
            "java.lang.String", "int", "double", "long", "short", "byte",
            "boolean", "char", "float"};

    /**
     * 定义切入点，切割所有的service方法
     */
    @Pointcut("execution(public * com.sunrun.emailanalysis.service..*.*(..))")
    public void cutPointer(){}

    /**
     * 前置通知
     * @param jp
     * @throws Throwable
     */
    @Before("cutPointer()")
    public void doBefore(JoinPoint jp) throws Throwable{
        logger.info("======= Service Call =======");
        logger.info("Call Class: " + jp.getTarget().getClass().getName());;
        logger.info("Call method: " + jp.getSignature().getName());;
        // 获取所有的参数
        StringBuilder sb = new StringBuilder();
        Object[] args = jp.getArgs();
        for (int k = 0; k < args.length; k++) {
            Object arg = args[k];
            // 获取对象类型
            String typeName = arg.getClass().getTypeName();
            for (String t : types) {
                //1 判断是否是基础类型
                if (t.equals(typeName)) {
                    sb.append(arg + "; ");
                }else{
                    //2 通过反射获取实体类属性
                    sb.append(getFieldsValue(arg));
                }
            }
        }
        logger.info("cut point args: " + sb.toString());
    }

    //解析实体类，获取实体类中的属性
    public static String getFieldsValue(Object obj) {
        //通过反射获取所有的字段，getFileds()获取public的修饰的字段
        //getDeclaredFields获取private protected public修饰的字段
        Field[] fields = obj.getClass().getDeclaredFields();
        String typeName = obj.getClass().getTypeName();
        for (String t : types) {
            if (t.equals(typeName)) {
                return "";
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Field f : fields) {
            //在反射时能访问私有变量
            f.setAccessible(true);
            try {
                for (String str : types) {
                    //这边会有问题，如果实体类里面继续包含实体类，这边就没法获取。
                    //其实，我们可以通递归的方式去处理实体类包含实体类的问题。
                    if (f.getType().getName().equals(str)) {
                        sb.append(f.getName() + " : " + f.get(obj) + ", ");
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.append("}");
        return sb.toString();
    }

}
