package ru.itis.trello.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    @Before("@annotation(ru.itis.trello.aspect.Logging)")
    public void beforeAnnotation(JoinPoint joinPoint) {
        System.out.println(
                "Method: " + joinPoint.getSignature().toString() + "; " +
                        "Args: " + Arrays.toString(joinPoint.getArgs())
        );
    }
}
