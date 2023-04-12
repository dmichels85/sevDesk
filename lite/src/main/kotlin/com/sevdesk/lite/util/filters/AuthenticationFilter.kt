package com.sevdesk.lite.util.filters

import com.sevdesk.lite.user.UserService
import com.sevdesk.lite.util.jwt.JwtTokenUtil
import org.apache.catalina.connector.RequestFacade
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse


class AuthenticationFilter(private val jwtTokenUtil: JwtTokenUtil, private val userService: UserService): Filter {
    override fun doFilter(req: ServletRequest, resp: ServletResponse, chain: FilterChain) {
        val response: HttpServletResponse = resp as HttpServletResponse
        val authHeader = (req as RequestFacade).getHeader("Authorization")

        if (authHeader == null) {
            response.status = 401
            return
        }

        val userId = jwtTokenUtil.getUserIdFromToken(authHeader.substring(7))

        if (userId == null) {
            response.status = 401
            return
        }

        val user = userService.getUserById(userId)

        RequestContextHolder.currentRequestAttributes().setAttribute("user", user, RequestAttributes.SCOPE_REQUEST)

        chain.doFilter(req, response)
    }
}