package com.denkora.image

import scala.collection.mutable.ArrayBuffer

/**
  * Created by denkoRa on 9/6/2018.
  */
object Main extends App {

  println("Entry point <= Main")
//  val l1 = Layer("testimg.jpg", 0)
//
//  val l2 = Layer("snimi2.jpg", 1)
//  //.deactivateLayer(2)
//  val result = Layer.joinLayers()
//
//  Image.save(result, "result.png")
  val o = Operation

  val img = Image("testimg.jpg")
//  o.applyOpOnImage("add", 1, img)
//  Image.save(img, "result_add.png")
  o.applyOpOnImage("sub", 2, img)
  Image.save(img, "result_sub.png")

  o.composeOp("composition", ArrayBuffer[String]("add"), ArrayBuffer[Double](0.2))
  o.listOps

  val f = o.op("composition", 4)
  println(f(1))
}
