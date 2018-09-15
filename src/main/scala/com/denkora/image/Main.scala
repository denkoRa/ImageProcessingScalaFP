package com.denkora.image

import com.denkora.managers.{FilterManager, OperationManager}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by denkoRa on 9/6/2018.
  */
object Main extends App {

  println("Entry point <= Main")
  val o = OperationManager
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
  val sm = SelectionFactory
  val s1 = sm("prva", (0, 0), (100, 100))
  val s2 = sm("druga", (5, 5), (15, 15))

  val f = FilterManager
  val ss = sm.mergeSelections()
  f.setSelection(ss)
  ss.deactivate()
  val l = LayerFactory("testimg.jpg", 1)
  var w = Array.ofDim[Double](3, 3)
  w(0) = Array(-1, -1, -1)
  w(1) = Array(2, 2, 2)
  w(2) = Array(-1, -1, -1)
  f.setWeights(w)
  //println(w(1).mkString(" "))
  f.setDist(1)
  f.applyFilter("grayscale", l)
  val i = LayerFactory.mergeLayers()
  Image.save(i, "result.png")
  f.applyFilter("wam", l)
//  //f.applyFilter("fill", img, c = Some(RGBColor(200, 200, 0)))
////  img.deactivateSelection
////  f.applyFilter("grayscale", img)
////  o.applyOp("inversion", img, 0)
////  Image.save(img, "result.png")
////  o.sequenceOp("sekvenca", names = ArrayBuffer("inversion", "inversion", "add"), consts = ArrayBuffer(0, 0, 0.2))
////  o.applySequence("sekvenca", img)
//  val img = LayerFactory.mergeLayers()
//  Image.save(img, "result1.png")

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
