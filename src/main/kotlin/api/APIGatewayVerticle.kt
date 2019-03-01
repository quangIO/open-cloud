package api

import event.Events
import event.FunctionReply
import event.FunctionRequest
import event.NaiveCodec
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import service.getUserDetail
import util.EnvConfig.port
import util.EnvConfig.uploadDir

private val logger = LoggerFactory.getLogger("api-gateway-server")

class APIGatewayVerticle : CoroutineVerticle() {
  override suspend fun start() {
    val router = createRouter()

    vertx.createHttpServer()
      .requestHandler(router)
      .listenAwait(port)
    logger.info("Server started on port $port")
  }

  private fun createRouter() = Router.router(vertx).apply {
    val eb = vertx.eventBus()
      .registerDefaultCodec(FunctionRequest::class.java, NaiveCodec<FunctionRequest>("func-req"))
      .registerDefaultCodec(FunctionReply::class.java, NaiveCodec<FunctionReply>("func-rep"))
    route()
      .handler(BodyHandler.create(uploadDir))
      .handler(CorsHandler.create("*"))

    get("/f/:name").handler { context ->
      val fileName = context.pathParam("name")
      val args = context.queryParams()
      val publisher = eb.publisher<FunctionRequest>(Events.REQUEST_CODE)
      publisher.send<FunctionReply>(FunctionRequest(fileName, args)) {
        val reply = it.result().body()
        with(context.response()) {
          reply.headers.forEach { (v, k) -> putHeader(v, k) }
          setStatusCode(reply.statusCode).end(reply.response)
        }
      }
    }

    post("/upload").handler { context ->
      val user = context.getUserDetail()
      val fileSystem = context.vertx().fileSystem()
      context.fileUploads().forEach { file ->
        fileSystem.move(
          file.uploadedFileName(), "$user|${System.currentTimeMillis()}" +
            ".${file.fileName().split(".").last()}"
        ) {
          context.response().setChunked(true).end(if (it.failed()) "FAILED" else "OK")
        }
      }
      context.response().end()
    }
  }
}
