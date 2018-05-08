package com.smbms.aop;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * 配置环绕增强
 * 
 * @author Sparks
 *
 */
@Aspect
public class EnhancedLogger {
	private static final Logger logger = Logger.getLogger(EnhancedLogger.class);

	@Around("execution(* com.smbms.service.*.*(..))")
	public Object aroundLogger(ProceedingJoinPoint pjp) throws Throwable {
		logger.info("调用" + pjp.getTarget() + "的" + pjp.getSignature().getName()
				+ "方法，方法入参：" + Arrays.toString(pjp.getArgs()));
		try {
			Object result = pjp.proceed(); // 执行目标方法并获得返回值
			logger.info("调用" + pjp.getTarget() + "的"
					+ pjp.getSignature().getName() + "方法，方法返回值：" + result);
			return result;
		} catch (Throwable e) {
			logger.error(pjp.getSignature().getName() + "方法发生异常：" + e);
			throw e;
		} finally {
			logger.info(pjp.getSignature().getName() + "方法结束执行。");
		}
	}
}
