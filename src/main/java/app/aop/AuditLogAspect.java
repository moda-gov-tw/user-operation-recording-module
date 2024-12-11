package app.aop;

import app.annotation.AuditLog;
import app.context.RuntimeContext;
import app.context.ThreadLocalData;
import app.dto.AuditLogDTO;
import app.enums.ExecStatus;
import app.formatter.FormatResult;
import app.formatter.ParamFormatter;
import app.service.AuditLogService;
import app.utils.ApplicationContextUtils;
import app.utils.StringCompressUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 稽核紀錄 AOP
 */
@Slf4j
@Aspect
@Component
public class AuditLogAspect {

    private final AuditLogService auditLogService;

    private final ParamFormatter paramFormatter;

    public AuditLogAspect(
        final AuditLogService auditLogService,
        @Qualifier("logParamFormatter") final ParamFormatter paramFormatter
    ) {
        this.auditLogService = auditLogService;
        this.paramFormatter = paramFormatter;
    }

    /**
     * 前置處理
     */
    private ExecInfo preProcess(
        final ProceedingJoinPoint joinPoint, final AuditLog auditLog
    ) {
        try {

            final ExecInfo execInfo = new ExecInfo(auditLogService);
            this.initAuditLog(execInfo, joinPoint, auditLog);
            final AuditLogDTO auditLogDTO = execInfo.auditLogDTO;
            final AuditLogDTO result = auditLogService.save(auditLogDTO);
            execInfo.auditLogDTO = result;

            final ThreadLocalData data = RuntimeContext.get();
            data.setAuditLogDTO(result);
            data.setAuditLog(auditLog);
            RuntimeContext.set(data);
            return execInfo;

        } catch (final Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 擷取資訊
     */
    private void initAuditLog(final ExecInfo execInfo, final ProceedingJoinPoint joinPoint, final AuditLog auditLog) {
        final AuditLogDTO dto = execInfo.auditLogDTO;
        dto.setServiceClass(joinPoint.getTarget().getClass().getSimpleName());
        dto.setServiceMethod(joinPoint.getSignature().getName());
        dto.setMethodType(auditLog.methodType().getCode());
        dto.setStatus(ExecStatus.execute.getCode());
        dto.setExecTime(0);

        final Object[] args = joinPoint.getArgs();
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method targetMethod = methodSignature.getMethod();
        final String formatterName = auditLog.paramFormatter();
        // 參數轉成字串
        ParamFormatter formatter;
        if (StringUtils.isBlank(formatterName)) {
            formatter = this.paramFormatter;
        } else {
            formatter = ApplicationContextUtils.getBean(formatterName, ParamFormatter.class);
        }
        final FormatResult formatResult = formatter.format(targetMethod, args);
        final String content = formatResult.getResult();
        // 參數字串壓縮轉成base64格式
        if (formatResult.isNeedCompress()) {
            dto.setMethodParams(StringCompressUtils.compress(content));
        } else {
            dto.setMethodParams(content);
        }
        dto.setCreateTime(LocalDateTime.now());
    }

    @Around(value = "@annotation(auditLog)", argNames = "joinPoint, auditLog")
    public Object logExecution(final ProceedingJoinPoint joinPoint, final AuditLog auditLog) throws Throwable {
        log.debug("logExecution Aspect");
        Object result;
        // 前置處理
        final ExecInfo execInfo = this.preProcess(joinPoint, auditLog);
        try {
            // 執行實際方法
            result = joinPoint.proceed();
            // 執行成功紀錄資訊
            this.afterSuccess(joinPoint, execInfo, result);
        } catch (final Exception e) {
            // 執行失敗紀錄資訊
            this.afterException(joinPoint, execInfo, e);
            throw e;
        }
        return result;
    }

    /**
     * 執行成功紀錄資訊
     */
    private void afterSuccess(final ProceedingJoinPoint joinPoint, final ExecInfo info, final Object result) {
        if (info != null) {
            final AuditLogDTO dto = info.auditLogDTO;
            dto.setStatus(ExecStatus.success.getCode());
            dto.setExecTime(info.getUsedTime());

            this.processSaveLog(info);
        }
    }

    /**
     * 執行失敗紀錄資訊
     */
    private void afterException(final ProceedingJoinPoint joinPoint, final ExecInfo info, final Exception e) {
        if (info != null) {
            final AuditLogDTO dto = info.auditLogDTO;
            dto.setStatus(ExecStatus.fail.getCode());
            dto.setExecTime(info.getUsedTime());
            dto.setErrorMsg(StringUtils.substring(ExceptionUtils.getMessage(e), 0, 200));
            this.processSaveLog(info);
        }
    }

    /**
     * 儲存紀錄資訊
     */
    private void processSaveLog(final ExecInfo info) {
        try {
            info.auditLogService.saveAuditLog(info.auditLogDTO);
        } catch (final Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    protected static class ExecInfo {
        final AuditLogService auditLogService;
        AuditLogDTO auditLogDTO;
        final long startTime;

        protected ExecInfo(final AuditLogService auditLogService) {
            this.startTime = System.currentTimeMillis();
            this.auditLogService = auditLogService;
            this.auditLogDTO = new AuditLogDTO();

            final ThreadLocalData data = RuntimeContext.get();
            if (data != null) {
                this.auditLogDTO.setApiUrl(StringUtils.defaultIfBlank(data.getApiUrl(), "Unknown"));
                this.auditLogDTO.setUserIp(StringUtils.defaultIfBlank(data.getUserIp(), "Unknown"));
            }
        }

        private int getUsedTime() {
            try {
                return Math.toIntExact(System.currentTimeMillis() - this.startTime);
            } catch (ArithmeticException e) {
                return Integer.MAX_VALUE;
            }
        }
    }
}
