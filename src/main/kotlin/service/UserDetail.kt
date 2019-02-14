package service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpHeaders
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import util.EnvConfig
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


private val algorithm: Algorithm = Algorithm.HMAC256(EnvConfig.secret)
private val verifier = JWT.require(algorithm).build()
private const val USERNAME = "username"

private val logger = LoggerFactory.getLogger("user-detail")
data class UserDetail(val username: String) {
  fun toJWT() = JWT.create()
    .withClaim(USERNAME, username)
    .withExpiresAt(Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()))
    .sign(algorithm)
}

fun RoutingContext.getUserDetail(): UserDetail = try {
  val decoded = verifier.verify(this.request().getHeader(HttpHeaders.AUTHORIZATION))
  UserDetail(decoded.getClaim(USERNAME).asString())
} catch (e: Exception) {
  response()
    .setStatusCode(HttpResponseStatus.UNAUTHORIZED.code())
    .end(e.message)
  logger.warn("Failed to verify token")
  throw e
}

