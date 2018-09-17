#!/usr/local/bin/amm

import $ivy.`com.lihaoyi::requests:0.1.5`

val r = requests.post("http://httpbin.org/post", data = Map("key" -> "value"))

println(r.statusCode)
println(r.headers("content-type"))
println(r.text)
