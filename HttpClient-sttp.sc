#!/usr/local/bin/amm

import $ivy.`com.softwaremill.sttp::core:1.3.1`
import com.softwaremill.sttp.quick._
val response = sttp.get(uri"http://httpbin.org/ip").send()

println(response.unsafeBody)
