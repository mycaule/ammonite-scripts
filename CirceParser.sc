#!/usr/local/bin/amm

import $ivy.{
  `io.circe::circe-generic:0.9.3`,
  `io.circe::circe-iteratee:0.10.0`,
  `io.iteratee::iteratee-monix:0.18.0`
}

// https://github.com/circe/circe/tree/master/examples/sf-city-lots

import io.circe.Decoder, io.circe.generic.auto._

import io.circe.iteratee._
import io.iteratee.monix.task._
import java.io.File
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class Coord(x: Double, y: Double)

object Coord {
  implicit val decodeCoord: Decoder[Coord] =
    Decoder[(Double, Double)].map(p => Coord(p._1, p._2)).or(
      Decoder[(Double, Double, Double)].map(p => Coord(p._1, p._2))
    )
}

sealed trait Geometry
case class Polygon(coordinates: List[List[Coord]]) extends Geometry
case class MultiPolygon(coordinates: List[List[List[Coord]]]) extends Geometry

// object Geometry {
//   implicit val decodeGeometry: Decoder[Geometry] = Decoder.instance(c =>
//     c.downField("type").as[String].flatMap {
//       case "Polygon" => c.as[Polygon]
//       case "MultiPolygon" => c.as[MultiPolygon]
//     }
//   )
// }

// object Polygon {
//   implicit val decodePolygon: Decoder[Polygon] =
//     Decoder[List[List[Coord]]].prepare(
//       _.downField("coordinates")
//     ).map(Polygon(_))
// }

// object MultiPolygon {
//   implicit val decodeMultiPolygon: Decoder[MultiPolygon] =
//     Decoder[List[List[List[Coord]]]].prepare(
//       _.downField("coordinates")
//     ).map(MultiPolygon(_))
// }

case class Lot(tpe: String, props: Map[String, String], geo: Option[Geometry])

// object Lot {
//   implicit val decodeLot: Decoder[Lot] = Decoder.instance(c =>
//     for {
//       t <- c.downField("type").as[String]
//       p <- c.downField("properties").as[Map[String, Option[String]]]
//       g <- c.downField("geometry").as[Option[Geometry]]
//     } yield Lot(t, p.collect { case (k, Some(v)) => (k, v) }, g)
//   )
// }

// wget https://raw.githubusercontent.com/zemirco/sf-city-lots-json/master/citylots.json
// awk 'BEGIN{print"["} NR>4 {print l} {l=$0}' citylots.json > data.json && rm citylots.json

val lots = readBytes(new File("data.json"))
  .through(byteArrayParser)
  // .through(decoder[Task, Lot])

val task = lots.into(length)
Await.result(task.runAsync, Duration.Inf)
