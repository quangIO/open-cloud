package event

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.Json

class NaiveCodec <T>(private val codecName: String): MessageCodec<T, T> {
  override fun decodeFromWire(pos: Int, buffer: Buffer): T {
    TODO()
  }

  override fun systemCodecID(): Byte = -1

  override fun encodeToWire(buffer: Buffer, s: T) {
    val b = Json.encodeToBuffer(s)
    buffer.appendInt(b.length()).appendBuffer(b)
  }

  override fun transform(s: T) = s

  override fun name(): String = codecName
}
