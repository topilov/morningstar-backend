package me.topilov.morningstar.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*


private const val JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000

@Service
class JwtService {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    fun extractUsername(token: String): String {
        return extractClaim(token) { claims -> claims.subject }
    }

    fun extractRole(token: String): String {
        return extractClaim(token) { claims -> claims["role"].toString()}
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims = hashMapOf<String, Any>(
            "username" to userDetails.username,
            "role" to userDetails.authorities.first()
        )
        return generateToken(claims, userDetails)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val userName = extractUsername(token)
        return (userName == userDetails.username) && !isTokenExpired(token)
    }

    private fun <T> extractClaim(token: String, claimsResolvers: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolvers(claims)
    }

    private fun generateToken(extraClaims: Map<String, Any?>, userDetails: UserDetails): String {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact()
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { claims -> claims.expiration }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).body
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(jwtSecret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}