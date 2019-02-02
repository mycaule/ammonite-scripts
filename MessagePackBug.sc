#!/usr/local/bin/amm

import $ivy.`org.msgpack:msgpack:0.6.12`
import $ivy.`ai.x::diff:2.0.1`

import ai.x.diff.DiffShow
import ai.x.diff.conversions._

import upickle.default.{write, writeBinary}
import org.msgpack.MessagePack

def fixValue(x: Array[Byte]) = {
  if (x(0) == -47 || x(0) == -46)
    x(0) = (x(0)-4).toByte
}

def fixArray(x: Array[Byte]) = {
  for (i <- 1 until x.length)
    if (x(i) == -47 || x(i) == -46)
    x(i) = (x(i)-4).toByte
}

def showValues(x1: Array[Byte], x2: Array[Byte]) = {
  println(x1.toSeq)
  println(x2.toSeq)
}

def showDifferences(x1: Array[Byte], x2: Array[Byte]) = {
  println(DiffShow.diff[Seq[Int]](x1.map(_.toInt), x2.map(_.toInt)).string)
}

val msgpack = new MessagePack()

val num: Int = 1005
val v1 = writeBinary(num)
val v2 = msgpack.write(num)

// fixValue(v1)

showValues(v1, v2)
showDifferences(v1, v2)

val str: String = "hello"
val w1 = writeBinary(str)
val w2 = msgpack.write(str)

showValues(w1, w2)
showDifferences(w1, w2)

val numbers: Array[Int] = (1 to 31).map(scala.math.pow(2,_).toInt).toArray
val x1 = writeBinary(numbers)
val x2 = msgpack.write(numbers)

// fixArray(x1)

showValues(x1, x2)
showDifferences(x1, x2)
