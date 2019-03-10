import api.APIGatewayVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.CoroutineVerticle
import service.ExecutorVerticle
import service.FileWatcherVerticle

class Launcher : CoroutineVerticle() {
  override suspend fun start() {
    with(vertx) {
      deployVerticle(
        ExecutorVerticle::class.java, DeploymentOptions()
          .setConfig(config)
          .setInstances(5)
          .setWorker(true)
          .setWorkerPoolName("executor-worker")
      )
      deployVerticle(APIGatewayVerticle())

      if (System.getenv("NORELOAD") == null)
        deployVerticle(FileWatcherVerticle())
    }
  }
}

fun main() {
  Vertx.vertx().deployVerticle(Launcher())
}
