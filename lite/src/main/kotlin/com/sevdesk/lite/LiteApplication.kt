package com.sevdesk.lite

import com.sevdesk.lite.user.UserService
import com.sevdesk.lite.util.filters.AuthenticationFilter
import com.sevdesk.lite.util.jwt.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class LiteApplication {

    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173")
                    .allowedMethods(
                        HttpMethod.HEAD.name,
                        HttpMethod.GET.name,
                        HttpMethod.POST.name,
                        HttpMethod.PUT.name,
                        HttpMethod.DELETE.name,
                        HttpMethod.PATCH.name
                    )
            }
        }
    }

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    lateinit var userService: UserService

    @Bean
    fun authenticationFilter(): FilterRegistrationBean<AuthenticationFilter> {
        val frb = FilterRegistrationBean<AuthenticationFilter>()

        frb.setFilter(AuthenticationFilter(jwtTokenUtil, userService))
        frb.addUrlPatterns("/api/*")
        frb.order = 1

        return frb
    }
}

fun main(args: Array<String>) {
    runApplication<LiteApplication>(*args)
}
