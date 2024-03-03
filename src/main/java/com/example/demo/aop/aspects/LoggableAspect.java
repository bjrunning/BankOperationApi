package com.example.demo.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class LoggableAspect {

    @Pointcut("within(@com.example.demo.aop.annotation.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature methodName = proceedingJoinPoint.getSignature();

        String message = "Вызов метода " + methodName + " " + LocalDateTime.now();
        log.info(message);

        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        message = "Выполнение метода " + methodName +
                " закончено. Время выполнения " + (endTime - startTime) + " мс. " + LocalDateTime.now();
        log.info(message);

        return result;
    }
}
