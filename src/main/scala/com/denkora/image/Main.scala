package com.denkora.image

import scala.collection.mutable.ArrayBuffer

/**
  * Created by denkoRa on 9/6/2018.
  */
object Main extends App {

  println("Entry point <= Main")
  val o = OperationManager

  val i = o.op("inversion", 0)
  println(i(0.3))
  o.composeOp("composition", ArrayBuffer[String]("add", "inversion"), ArrayBuffer[Double](0.2, 1))

//  val f = o.op("composition", 0)
//  println(f(1))
//
//  o.composeOp("comp2", ArrayBuffer[String]("composition", "sub"), ArrayBuffer[Double](0, 5))
//  val g = o.op("comp2", 0)
//  println(g(1))

//  val img = Image("testimg.jpg")
//  img.select((300, 300), (600, 600))
//  img.select((200, 400), (900, 450))
//  img.deactivateSelection
//  val f = o.applyOp("power", img, 10)
//  Image.save(img, "result.png")
//  val g = o.applyOp("composition", img, 123)
//  Image.save(img, "cmp.png")
//
//  val L = LayerFactory
//  val l1 = L("testimg.jpg", 0.5)
//  val l2 = L("testimg.jpg", 0.2)
//  println(L.layers.size)

    val f = FilterManager
    val img = Image("testimg.jpg")
    f.applyFilter("median", img, 5)
    Image.save(img, "result.png")
//    val mat = Array.tabulate[Double](10, 10)((r, c) => r + c)
//    for (i <- 0 until mat.length) {
//      println(mat(i).mkString(" "))
//    }
//    val mm = f.median(mat, 2)
//    println("-----------------------------------------------")
//    for (i <- 0 until mm.length) {
//      println(mm(i).mkString(" "))
//    }


}
