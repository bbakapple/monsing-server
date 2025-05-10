package org.monsing.common.log

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView

/**
 * 컨트롤러 어드바이스보다 먼저 예외를 가로채서 로깅하는 리졸버
 * 예외 처리는 하지 않고 로깅만 수행한 후 원래 예외를 다시 던짐
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 최우선으로 실행되도록 설정
class ExceptionLoggingResolver(
    private val httpLogger: HttpLogger
) : HandlerExceptionResolver {

    override fun resolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception
    ): ModelAndView? {
        // 예외 정보 로깅
        if (request.getAttribute("exception_logged") == null) {
            // 중복 로깅 방지
            request.setAttribute("exception_logged", true)
            
            // 요청 정보가 없을 경우 설정 (필터에서 설정되지 않았을 경우)
            try {
                httpLogger.setException(ex)
                httpLogger.logException(ex)
            } catch (e: Exception) {
                // 로깅 중 오류가 발생해도 원래 예외 처리 흐름은 중단되지 않도록 함
            }
        }
        
        // null을 반환하여 예외 처리를 계속 진행하도록 함
        return null
    }
} 