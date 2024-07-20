package org.example.fourtreesproject.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class MethodExecutionTimer {
    private static final Logger logger = LoggerFactory.getLogger(MethodExecutionTimer.class);

    @Pointcut("@annotation(org.example.fourtreesproject.common.annotation.Timer)")
    private void timePointCut() {
    }

    @Around("timePointCut()")
    public Object traceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            logger.info("{} 실행 시간: {}s", joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis()/1000.0);
        }
    }
}
