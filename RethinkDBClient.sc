#!/usr/local/bin/amm

import $ivy.{
  `com.rethinkdb:rethinkdb-driver:2.3.3`,
  `org.slf4j:slf4j-log4j12:1.7.25`
}

import com.rethinkdb.RethinkDB
import com.rethinkdb.model.MapObject
import com.rethinkdb.net.Cursor
import scala.collection.JavaConverters._

// https://www.rethinkdb.com/docs/guide/java/
// https://github.com/rklick-solutions/play-rethinkdb

val r = RethinkDB.r

val conn = r.connection().hostname("localhost").port(32769).connect()

// r.db("test").tableCreate("authors").run(conn)

val res1: java.util.Map[_, _] = r.table("authors").insert(r.array(
    r.hashMap("name", "William Adama")
    .`with`("tv_show", "Battlestar Galactica")
    .`with`("posts", r.array(
      r.hashMap("title", "Decommissioning speech")
       .`with`("content", "The Cylon War is long over..."),
      r.hashMap("title", "We are at war")
       .`with`("content", "Moments ago, this ship received..."),
      r.hashMap("title", "The new Earth")
       .`with`("content", "The discoveries of the past few days...")
      )
    ),
  r.hashMap("name", "Laura Roslin")
    .`with`("tv_show", "Battlestar Galactica")
    .`with`("posts", r.array(
      r.hashMap("title", "The oath of office")
       .`with`("content", "I, Laura Roslin, ..."),
      r.hashMap("title", "They look like us")
       .`with`("content", "The Cylons have the ability...")
      )
    ),
  r.hashMap("name", "Jean-Luc Picard")
    .`with`("tv_show", "Star Trek TNG")
    .`with`("posts", r.array(
      r.hashMap("title", "Civil rights")
       .`with`("content", "There are some words I've known since...")
      )
    )
)).run(conn)

println(res1.asScala.toMap)

val res2 = r.http("https://api.github.com/repos/rethinkdb/rethinkdb/stargazers")
 .pluck("login", "id").orderBy("id")

println(res2)

val cursor: Cursor[_] = r.table("authors").run(conn)

for (doc <- cursor.toList.asScala) {
  println(doc)
}
