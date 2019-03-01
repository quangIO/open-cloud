package event

data class FunctionReply(
  val response: String,
  val headers: Map<String, String> = mapOf(),
  val statusCode: Int = 200
)
