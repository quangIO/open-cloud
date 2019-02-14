package service

import java.lang.Exception


enum class SourceType {
  JAVA, JAVASCRIPT;
  companion object {
    fun getFromExtension(str: String): SourceType = when (str) {
      "java" -> JAVA
      "javascript" -> JAVASCRIPT
      else -> TODO("source not supported")
    }
    fun toIso(str: String) = when(str) {
      "java" -> "java"
      "js" -> "js"
      else -> TODO("unknown $str")
    }
  }
}
