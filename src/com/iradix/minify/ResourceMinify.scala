package com.iradix.minify

import net.liftweb.http._
import net.liftweb.util.{LRU, Props, Helpers}
import scala._
import scala.collection.immutable.::
import scala.Predef._
import scala.Unit
import java.io.File._
import java.io.{InputStream, ByteArrayInputStream}
import net.liftweb.http.StreamingResponse
import scala.collection.immutable.::
import java.util.Locale
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._

object ResourceMinify {

  private val cacheSize = Props.get("minify.cache.size").flatMap{ asInt _}.getOrElse(1000)
  private lazy val cache : LRU[String, Array[Byte]] = new LRU(cacheSize)

  val suffixes = List(
    ResourceTypes.Css,
    ResourceTypes.JavaScript)

  /**
   * Matches the run mode and decides whether to compress and cache or not
   * @param in an InputStream to the file to be minified
   * @param resourceType The type of resource being handled
   * @return an InputStream to pass on further to Lift StreamingResponse
   */
  def compress(cacheKey:String, in:InputStream, resourceType:ResourceTypes.ResourceType):InputStream = {
    Props.mode match {
      //These modes are cached for performance
      case Props.RunModes.Production =>
        val cached = cache.get(cacheKey).openOr(resourceType.compress(in, cacheKey).getBytes())
        new ByteArrayInputStream(cached)
      //These modes are not cached, but still minified
      case Props.RunModes.Staging | Props.RunModes.Pilot =>
        val nonCached = resourceType.compress(in, cacheKey).getBytes()
        new ByteArrayInputStream(nonCached)
      //This is not cached or minified
      case _ =>
        in
    }
  }

  def init() : Unit = {
    suffixes.foreach{ s =>
      val dir = s.directory
      LiftRules.statelessDispatch.prepend {
        case Req(`dir` :: file :: Nil, `dir`, _) =>
          () => for (in <- LiftRules.getResource(separator + dir + separator + file + "." + dir).map(_.openStream))
          yield {
            StreamingResponse(
              compress(dir + "/" + file, in, s),
              () => in.close,
              size = -1,
              headers = Nil,
              cookies = Nil,
              code=200)
          }
      }
    }
  }
}
