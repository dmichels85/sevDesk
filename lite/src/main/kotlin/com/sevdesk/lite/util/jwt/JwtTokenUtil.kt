package com.sevdesk.lite.util.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenUtil {

    @Value("\${jwt.secret}")
    private val secret: String? = null

    /**
     * Create a JWT Token for a given userId.
     *
     * @param userId is the id to add as a claim to the token
     * @return token
     */
    fun createToken(userId: Long): String {
        return JWT.create()
            .withIssuer("sevDesk")
            .withClaim("userId", userId)
            .withIssuedAt(Date())
            .sign(Algorithm.HMAC512(secret))
    }


    /**
     * Extract the userId from a given JWT Token.
     * If the token is invalid it throws a JWTVerificationException
     *
     * @param token
     * @return the extracted userId
     * @throws JWTVerificationException if the verification fails
     */
    fun getUserIdFromToken(token: String): Long {
        val algorithm = Algorithm.HMAC512(secret)
        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer("sevDesk")
            .build()
        val decodedJWT = verifier.verify(token)
        return decodedJWT.claims["userId"]!!.asLong()
    }

}