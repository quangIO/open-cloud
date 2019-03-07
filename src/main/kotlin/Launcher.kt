import api.APIGatewayVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.CoroutineVerticle
import service.ExecutorVerticle

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
    }
  }
}
