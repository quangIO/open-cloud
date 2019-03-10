package service

import com.github.benmanes.caffeine.cache.Caffeine
import event.Events
import event.FunctionReply
import event.FunctionRequest
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Engine
import org.graalvm.polyglot.Source
import util.EnvConfig.uploadDir
import java.io.File
import java.util.concurrent.TimeUnit

private val logger = LoggerFactory.getLogger(ExecutorVerticle::class.java)
private val engine = Engine.create()
private val sourceCache = Caffeine.newBuilder()
  .maximumSize(10000)
  .expireAfterWrite(5, TimeUnit.MINUTES)
  .refreshAfterWrite(1, TimeUnit.MINUTES)
  .build(::getSource)

private fun getSource(sourceId: String): Source? {
  val file = File(uploadDir, sourceId)

  if (!file.isFile)
    return null

  val fileType = SourceType.toIso(sourceId.takeLastWhile { it != '.' })
  return Source.newBuilder(fileType, file).build()
}

class ExecutorVerticle : CoroutineVerticle() {
  override suspend fun start() {
    vertx.eventBus().apply {
      localConsumer<FunctionRequest>(Events.REQUEST_CODE) { message ->
        val request = message.body()
        message.reply(execute(request).getOrElse {
          logger.warn(it.cause)
          FunctionReply("Internal Server Error", hashMapOf(), 500)
        })
      }
      localConsumer<String>(Events.EVICT_CACHE_ITEM) { message ->
        sourceCache.invalidate(message)
        // TODO: use refresh instead
      }
    }
  }

  private fun execute(request: FunctionRequest): Result<FunctionReply> {
    val context = Context.newBuilder().engine(engine).build()
    vertx.setTimer(2000) {
      context.close()
    }
    val source = sourceCache[request.sourceId]
    return try {
      val reply = FunctionReply()
      with(context.polyglotBindings) {
        putMember("request", request)
        putMember("response", reply)
      }
      context.eval(source)
      Result.success(reply)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
