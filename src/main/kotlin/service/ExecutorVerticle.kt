package service

import com.github.benmanes.caffeine.cache.Caffeine
import event.Events
import event.FunctionReply
import event.FunctionRequest
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Engine
import org.graalvm.polyglot.Source
import util.EnvConfig.uploadDir
import java.io.File
import java.util.concurrent.TimeUnit

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
    vertx.eventBus().localConsumer<FunctionRequest>(Events.REQUEST_CODE) {
      val request = it.body()
      it.reply(FunctionReply(execute(request)))
    }
  }


  private fun execute(request: FunctionRequest): String {
    val source = sourceCache[request.sourceId]
    Context.newBuilder().engine(engine).build().use { context ->
      return context.eval(source).toString()
    }
  }
}
