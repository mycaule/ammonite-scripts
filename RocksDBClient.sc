#!/usr/local/bin/amm

import $ivy.`org.rocksdb:rocksdbjni:5.14.2`

import org.rocksdb.{RocksDB, Options}
import java.nio.charset.StandardCharsets

// https://github.com/facebook/rocksdb/blob/master/java/samples/src/main/java/RocksDBSample.java

RocksDB.loadLibrary

val options = new Options()
  .setCreateIfMissing(true)

val db = RocksDB.open(options, "test.db")

db.put("hello".getBytes, "world".getBytes)

val value = new String(db.get("hello".getBytes), StandardCharsets.UTF_8)
println(value)

println(db.getProperty("rocksdb.stats"))

val iterator = db.newIterator

iterator.seekToFirst

// List of all keys
while(iterator.isValid) {
  println(new String(iterator.key))
  iterator.next
}
