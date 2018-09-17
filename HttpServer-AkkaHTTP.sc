#!/usr/local/bin/amm

import $ivy.`com.typesafe.akka::akka-http:10.1.5`
import $ivy.`com.typesafe.akka::akka-stream:2.5.16`
import akka.http.scaladsl._
import akka.http.scaladsl.model.{HttpEntity, ContentTypes}
import akka.http.scaladsl.server.Directives._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

implicit val system = akka.actor.ActorSystem("MySystem")
implicit val materializer = akka.stream.ActorMaterializer()
implicit val executionContext = system.dispatcher

val route =
  path("hello") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
    }
  }

val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

def shutdown = bindingFuture.flatMap(_.unbind()).onComplete{_ => system.terminate() }

Await.result(system.whenTerminated, Duration.Inf)
