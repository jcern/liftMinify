package com.iradix.minify
import com.yahoo.platform.yui.compressor.{CssCompressor, JavaScriptCompressor}
import java.io._
import org.mozilla.javascript.{EvaluatorException, ErrorReporter}
import com.iradix.minify.MyError

object ResourceTypes {
  sealed trait ResourceType {
    def extension:String
    def directory:String = extension
    def compress(in:InputStream, pageName:String):String = compress(new InputStreamReader(in), pageName)
    def compress(in:String, pageName:String):String  = compress(new StringReader(in), pageName)
    def compress(in:Reader, pageName:String):String
  }

  object JavaScript extends ResourceType {
    val extension = "js"

    def compress(in:Reader, pageName:String) = {
      val w = new StringWriter()
      new JavaScriptCompressor(in, MyError(pageName)).compress(w, 80, true, true, true, true)
      w.toString
    }
  }

  object Css extends ResourceType {
    val extension = "css"

    def compress(in:Reader, pageName:String) = {
      val w = new StringWriter()
      new CssCompressor(in).compress(w, 80)
      w.toString
    }
  }
}

case class MyError(localFilename:String) extends ErrorReporter {

  def warning( message:String, sourceName:String,
    line:Int, lineSource:String,  lineOffset:Int) {
    System.err.println("\n[WARNING] in " + localFilename)
    if (line < 0) {
      System.err.println("  " + message)
    } else {
      System.err.println("  " + line + ':' + lineOffset + ':' + message);
    }
  }

  def error(message:String, sourceName:String, line:Int, lineSource:String,  lineOffset:Int) {
    if (line < 0) {
      System.err.println("  " + message);
    } else {
      System.err.println("  " + line + ':' + lineOffset + ':' + message);
    }
  }

  def runtimeError(message:String, sourceName:String,
    line:Int, lineSource:String,  lineOffset:Int) {
    error(message, sourceName, line, lineSource, lineOffset);
    return new EvaluatorException(message);
  }
}