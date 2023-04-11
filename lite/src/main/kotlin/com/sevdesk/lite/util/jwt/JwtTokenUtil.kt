package com.sevdesk.lite.util.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenUtil {

    @Value("\${jwt.secret}")
    private val secret: String? = null

    fun doGenerateToken(userId: Long): String {
        return JWT.create().withIssuer("sevDesk").withClaim("userId", userId).withIssuedAt(Date()).sign(Algorithm.HMAC512(secret))
    }

    fun validateToken(token: String): Long? {

            val algorithm = Algorithm.HMAC512(secret)
            val verifier: JWTVerifier = JWT.require(algorithm) // specify an specific claim validations
                .withIssuer("sevDesk") // reusable verifier instance
                .build()
            val decodedJWT = verifier.verify(token)
            return decodedJWT.claims.get("userId")!!.asLong()

    }

}