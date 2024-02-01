package me.topilov.morningstar.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import me.topilov.morningstar.dto.auth.AuthToken
import me.topilov.morningstar.utils.AUTH_HEADER_NAME
import me.topilov.morningstar.utils.BEARER_PREFIX
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

@Service
class AuthTokenService {

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    private val ACCESS_TOKEN_VALIDITY = 15.minutes.inWholeMilliseconds
    private val REFRESH_TOKEN_VALIDITY = 30.days.inWholeMilliseconds

    fun getAccessToken(request: HttpServletRequest): String? {
        return request.getHeader(AUTH_HEADER_NAME).substring(BEARER_PREFIX.length)
    }

    fun extractUsername(token: String): String? {
        return extractClaim(token) { claims -> claims.subject }
    }

    fun extractRole(token: String): String? {
        return extractClaim(token) { claims -> claims["role"].toString()}
    }

    fun extractExpiration(token: String): Date? {
        return extractClaim(token) { claims -> claims.expiration }
    }

    fun isTokenValid(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return extractedUsername == username && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token)?.before(Date()) ?: true
    }

    fun generateAuthToken(username: String, role: String): AuthToken {
        return AuthToken(
            access = generateAccessToken(username, role),
            refresh = generateRefreshToken(username, role)
        )
    }

    fun generateAccessToken(username: String, role: String): String {
        return generateToken(username, role, ACCESS_TOKEN_VALIDITY)
    }

    fun generateRefreshToken(username: String, role: String): String {
        return generateToken(username, role, REFRESH_TOKEN_VALIDITY)
    }

    fun getRefreshTokenCookie(refreshToken: String): Cookie {
        return Cookie("refresh_token", refreshToken).apply {
            isHttpOnly = true
            secure = true
            maxAge = REFRESH_TOKEN_VALIDITY.toInt()
        }
    }

    private fun generateToken(username: String, role: String, validity: Long): String {
        val claims = hashMapOf<String, Any>(
            "username" to username,
            "role" to role,
        )
        return generateToken(claims, username, validity)
    }

    private fun generateToken(extraClaims: Map<String, Any?>, username: String, validity: Long): String {
        val currentTimeMillis = System.currentTimeMillis()

        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(username)
            .setIssuedAt(Date(currentTimeMillis))
            .setExpiration(Date(currentTimeMillis + validity))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact()
    }

    private fun <T> extractClaim(token: String, claimsResolvers: (Claims) -> T): T? {
        val claims = extractAllClaims(token)
        return claims?.let(claimsResolvers)
    }

    private fun extractAllClaims(token: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .body
        } catch (exception: ExpiredJwtException) {
            null
        }
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}