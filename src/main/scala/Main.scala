import java.io.File
import java.util.concurrent.atomic.AtomicInteger

import akka.actor.ActorSystem
import akka.http.javadsl.model.headers.ContentDisposition
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.headers.ContentDispositionTypes
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import spray.json._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.StdIn


trait JsonSupport extends DefaultJsonProtocol {
  implicit val urlFormat = jsonFormat1(YoutubeUrl)
}

case class YoutubeUrl(url: String)

object Main extends App with JsonSupport {

  implicit val system = ActorSystem("my-system")
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val route =
    pathSingleSlash {
      getFromResource("htmls/index.html")
    } ~
      pathPrefix("") {
        getFromResourceDirectory("")
      } ~
      path("hello") {
        /*
        get {
          complete {
            <html>
              <h1>Say hello to akka-http</h1>
            </html>
          }
        } ~
        */
        (post & entity(as[YoutubeUrl])) { y =>
          println("request received.")
          val filename = System.currentTimeMillis().toString + counter.addAndGet(1)
          println("fuck", filename)
          println("url = ", y.url)
          val task = {
            import sys.process._
            Future(stringToProcess(s"youtube-dl -o $filename ${y.url}") !)
          }
          //Await.ready(task, Duration.Inf)
          complete {
            task.map { _ =>
              filename
            }
          }
        }
      } ~
      pathPrefix("files" / Segment) { prefix =>
        complete {
          import scala.collection.JavaConverters._
          val filename = getExactFilename(prefix)
          val entity = HttpEntity(ContentTypes.`application/octet-stream`, getFileSource(filename))
          val headers = List(ContentDisposition.create(ContentDispositionTypes.attachment, Map("filename" -> filename).asJava))
          HttpResponse(entity = entity, headers = headers)
        }
      }

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)
  var counter = new AtomicInteger

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done

  def getFileSource(filename: String) = {
    FileIO.fromFile(new File(filename))
  }

  def getExactFilename(prefix: String) : String = {
    new File(".").listFiles.filter { f =>
      s"^$prefix".r.findFirstIn(f.getName).isDefined
    }(0).getName
  }

  def getFileSource(url: YoutubeUrl) = {
    val file = new File("setup-x86.exe")
    FileIO.fromFile(file)
  }

  def youtubeUrlHandler = { y: YoutubeUrl =>
    complete(HttpEntity(ContentTypes.`application/octet-stream`, getFileSource))
  }

  def getFileSource = {
    val file = new File("test.mp4")
    FileIO.fromFile(file)
  }

  def youtubeGetUrlHandler = complete(HttpEntity(ContentTypes.`application/octet-stream`, getFileSource))

}




