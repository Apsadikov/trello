package ru.itis.trello.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.trello.security.details.UserDetailsImpl;
import ru.itis.trello.service.BoardMemberService;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class AuthorizationAspect {
    private BoardMemberService boardMemberService;

    @Autowired
    public AuthorizationAspect(BoardMemberService boardMemberService) {
        this.boardMemberService = boardMemberService;
    }

    @Around(value = "@annotation(ru.itis.trello.aspect.Authorization)) && args(UserDetailsImpl, Long,..) && returning()")
    public String aroundController(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        try {
            Annotation[][] annotations = proceedingJoinPoint.getTarget().getClass().getMethod(methodName, parameterTypes).getParameterAnnotations();
            Long boardId = getBoardId(annotations, proceedingJoinPoint);
            Long userId = getUserId(annotations, proceedingJoinPoint);
            if (userId != null && boardId != null) {
                if (boardMemberService.isBoardMemberExist(boardId, userId)) {
                    try {
                        return (String) proceedingJoinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "redirect:/error";
    }

    @Around(value = "@annotation(ru.itis.trello.aspect.RestAuthorization)) && args(UserDetailsImpl, Long,..)")
    public ResponseEntity aroundRestController(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        try {
            Annotation[][] annotations = proceedingJoinPoint.getTarget().getClass().getMethod(methodName, parameterTypes).getParameterAnnotations();
            Long boardId = getBoardId(annotations, proceedingJoinPoint);
            Long userId = getUserId(annotations, proceedingJoinPoint);
            if (userId != null && boardId != null) {
                if (boardMemberService.isBoardMemberExist(boardId, userId)) {
                    try {
                        return (ResponseEntity) proceedingJoinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private Long getBoardId(Annotation[][] annotations, ProceedingJoinPoint proceedingJoinPoint) {
        int paramIndex = 0;
        for (Annotation[] annotationArr : annotations) {
            for (Annotation annotation : annotationArr) {
                if (annotation.annotationType().getName().equals("org.springframework.web.bind.annotation.PathVariable")
                        && ((PathVariable) annotation).value().equals("board_id")) {
                    return Long.valueOf(proceedingJoinPoint.getArgs()[paramIndex].toString());
                }
            }
            paramIndex++;
        }
        return null;
    }

    private Long getUserId(Annotation[][] annotations, ProceedingJoinPoint proceedingJoinPoint) {
        int paramIndex = 0;
        for (Annotation[] annotationArr : annotations) {
            for (Annotation annotation : annotationArr) {
                if (annotation.annotationType().getName().equals("org.springframework.security.core.annotation.AuthenticationPrincipal")) {
                    return ((UserDetailsImpl) proceedingJoinPoint.getArgs()[paramIndex]).getUser().getId();
                }
            }
            paramIndex++;
        }
        return null;
    }
}
