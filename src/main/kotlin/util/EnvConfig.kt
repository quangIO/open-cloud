package util

object EnvConfig {
  val port = System.getenv("PORT")?.toInt() ?: 8080
  val uploadDir = System.getenv("UPLOAD_DIR") ?: "/tmp"
  val secret = System.getenv("SECRET") ?: "secret"
}
