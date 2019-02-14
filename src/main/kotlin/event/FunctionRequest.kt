package event

import io.vertx.core.MultiMap
import io.vertx.core.http.HttpMethod

data class FunctionRequest(
  val sourceId: String,
  val args: MultiMap,
  val httpMethod: HttpMethod = HttpMethod.GET
)
