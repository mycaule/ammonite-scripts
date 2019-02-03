#!/usr/local/bin/amm

import $ivy.`org.msgpack:msgpack-core:0.8.16`
import $ivy.`org.msgpack:jackson-dataformat-msgpack:0.8.16`
import $ivy.`ai.x::diff:2.0.1`

import ai.x.diff.DiffShow
import ai.x.diff.conversions._

import upickle.default.{write, writeBinary, readBinary}

import org.msgpack.core.MessagePack
import org.msgpack.jackson.dataformat.MessagePackFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.msgpack.jackson.dataformat.Tuple

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

def writeMsgpack(x: Int) = {
  val packer = MessagePack.newDefaultBufferPacker
  packer.packInt(x)
  packer.close
  packer.toByteArray
}

def writeMsgpack(x: String) = {
  val packer = MessagePack.newDefaultBufferPacker
  packer.packString(x)
  packer.close
  packer.toByteArray
}

def writeMsgpack(x: Array[Int]) = {
  val packer = MessagePack.newDefaultBufferPacker
  packer.packArrayHeader(x.length)
  x.foreach(packer.packInt)
  packer.close
  packer.toByteArray
}

def writeMsgpack(x: Map[Int, Byte]) = {
  val packer = MessagePack.newDefaultBufferPacker
  packer.packMapHeader(x.keys.toSeq.length)
  x.foreach { case (k,v) =>
    packer.packInt(k)
    packer.packByte(v)
  }
  packer.close
  packer.toByteArray
}

val num: Int = 1005
val v1 = writeBinary(num)
val v2 = writeMsgpack(num)

// fixValue(v1)

showValues(v1, v2)
showDifferences(v1, v2)

val str: String = "hello"
val w1 = writeBinary(str)
val w2 = writeMsgpack(str)

showValues(w1, w2)
showDifferences(w1, w2)

val numbers: Array[Int] = (1 to 31).map(scala.math.pow(2,_).toInt).toArray
val x1 = writeBinary(numbers)
val x2 = writeMsgpack(numbers)

// fixArray(x1)

showValues(x1, x2)
showDifferences(x1, x2)

val nums: Map[Int, Byte] = (1 to 10).map(x => (scala.math.pow(2,x).toInt, 45.toByte)).toMap
val y1 = writeBinary(nums)
val y2 = writeMsgpack(nums)

println(readBinary[Map[Int, Byte]](y1).toSeq)

showValues(y1, y2)
showDifferences(y1, y2)
