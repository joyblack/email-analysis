package com.sunrun.emailanalysis.aop;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

// Request and ResponseLogger.
@Component
@Aspect
public class RequestLogAop {
    private static Logger log = LoggerFactory.getLogger(RequestLogAop.class);


    @Around("within(com.sunrun.emailanalysis.controller.*)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        // Get Request URI
        HttpServletRequest request = getRequest(joinPoint);
        log.info("----------------------------- Request start -----------------------------");
        log.info("Request URL: {}", request.getRequestURI());
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("Call Method: {}.{}", method.getDeclaringClass().getName(),method.getName());
        if(args!= null && args.length > 0){
            String parameterString = JSONObject.toJSONString(args[0], SerializerFeature.WriteMapNullValue);
            log.info("Request Parameter: {}", parameterString);
        }else{
            log.info("Request Parameter: null");
        }

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        log.info("Return data: {}", JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue));
        log.info("Used time: {}ms", System.currentTimeMillis() - start);
        log.info("----------------------------- Request end -----------------------------");
        return result;
    }

    // Get Http Request Object
    private HttpServletRequest getRequest(JoinPoint point){
        Object[] args = point.getArgs();
        for (Object object : args) {
            if(object instanceof HttpServletRequest ) {
                return (HttpServletRequest) object;
            }
        }
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        return request;
    }
}
