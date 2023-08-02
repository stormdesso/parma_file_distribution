package ru.parma.filesdistr.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogger {

    private static Logger LOG = LoggerFactory.getLogger(ServiceLogger.class);
    @Pointcut("@annotation(ru.parma.filesdistr.aop.annotations.LoggableMethod)")
    public void loggableMethod() {}

    @Around(value = "loggableMethod()")
    public Object logMethod(ProceedingJoinPoint call) throws Throwable {
        try {
            return call.proceed();
        } catch (Exception e) {
            LOG.error(String.format("Метод %s был выполнен с ошибкой: %s", call.getSignature().getName(), e.getMessage()));
            throw e;
        }
    }
}
