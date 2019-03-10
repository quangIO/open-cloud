package service

import event.Events
import io.vertx.core.AbstractVerticle
import io.vertx.core.logging.LoggerFactory
import util.EnvConfig
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchService

private fun Path.watch(): WatchService = fileSystem.newWatchService().apply {
  register(
    this,
    StandardWatchEventKinds.ENTRY_CREATE,
    StandardWatchEventKinds.ENTRY_MODIFY,
    StandardWatchEventKinds.OVERFLOW,
    StandardWatchEventKinds.ENTRY_DELETE
  )
}

private val logger = LoggerFactory.getLogger(FileWatcherVerticle::class.java)

class FileWatcherVerticle : AbstractVerticle() {
  override fun start() {
    val watcher = Paths.get(EnvConfig.uploadDir).watch()

    vertx.setPeriodic(1000) {
      // Fix me: that may cause some troubles
      watcher.poll()?.let { key ->
        key.pollEvents().forEach {
          logger.info("${it.context()} ${it.kind()}$")
          vertx.eventBus().publish(Events.EVICT_CACHE_ITEM, it.context().toString())
        }
        key.reset()
      }
    }
  }
}
