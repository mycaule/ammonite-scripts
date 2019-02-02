#!/usr/local/bin/amm

import $ivy.`org.msgpack:msgpack:0.6.12`
import $ivy.`org.msgpack:msgpack-core:0.8.16`
import org.msgpack.MessagePack
import upickle.default.writeBinary

val msgpack = new MessagePack()

val intValue = 1005
val v1 = writeBinary(intValue)
val v2 = msgpack.write(intValue)

println(v1.toSeq)
// WrappedArray(-47, 3, -19)
println(v2.toSeq)
// WrappedArray(-51, 3, -19)

val stringValue = "hello"
val w1 = writeBinary(stringValue)
val w2 = msgpack.write(stringValue)

println(w1.toSeq)
// WrappedArray(-91, 104, 101, 108, 108, 111)
println(w2.toSeq)
// WrappedArray(-91, 104, 101, 108, 108, 111)
