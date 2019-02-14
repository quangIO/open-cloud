package event

data class FunctionReply(
  val response: String,
  val headers: MutableMap<String, String> = mutableMapOf()
)
