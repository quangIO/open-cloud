package event

data class FunctionReply(
  @JvmField var response: String = "",
  @JvmField var headers: Map<String, String> = mapOf(),
  @JvmField var statusCode: Int = 200
)
