package com.denkora.image

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by denkoRa on 9/8/2018.
  */
object Operation {
  val add: (Double, Double) => Double = (x, y) => x + y
  val sub: (Double, Double) => Double = (x, y) => x - y
  val invsub: (Double, Double) => Double = (x, y) => y - x
  val mul: (Double, Double) => Double = (x, y) => x * y
  val div: (Double, Double) => Double = (x, y) => x / y
  val invdiv: (Double, Double) => Double = (x, y) => y / x
  val power: (Double, Double) => Double = (x, y) => scala.math.pow(x, y)
  val logarithm: (Double, Double) => Double = (x, y) => scala.math.log(x)
  val max: (Double, Double) => Double = (x, y) => scala.math.max(x, y)
  val min: (Double, Double) => Double = (x, y) => scala.math.min(x, y)
  val abs: (Double, Double) => Double = (x, y) => scala.math.abs(x)
  val identity: (Double, Double) => Double = (x, y) => x
  val inversion: (Double, Double) => Double = base(identity, invsub, 1.0)

  def base(g: (Double, Double) => Double, f: (Double, Double) => Double, c: Double): (Double, Double) => Double = {
    def f(x: Double, y: Double): Double = {
      g(f(x, c), y)
    }
    f
  }

  var ops: mutable.HashMap[String, (Double, Double) => Double] = mutable.HashMap()

  def listOps = {
    ops.keys.foreach {println}
  }

  def limit(v: Double): Double = {
    scala.math.max(scala.math.min(v, 1), 0)
  }

  def dontLimit(v: Double): Double = v

  ops("add") = add
  ops("sub") = sub
  ops("invsub") = invsub
  ops("mul") = mul
  ops("div") = div
  ops("invdiv") = invdiv
  ops("power") = power
  ops("logarithm") = logarithm
  ops("max") = max
  ops("min") = min
  ops("abs") = abs
  ops("inversion") = inversion

  def op(name: String, const: Double): Double => Double = {
    def f(param: Double): Double = {
      ops(name)(param, const)
    }
    f
  }

  def applyOpOnImage(name: String, c: Double, img: Image): Unit = {
    img.applyOp(op(name, c), dontLimit)
  }

  def applyOpOnLayers(name: String, c: Double, layers: ArrayBuffer[Layer]): Unit = {
    for (l <- layers)
      applyOpOnImage(name, c, l.img)
  }

  def composeOp(compositionName: String, names: ArrayBuffer[String], consts: ArrayBuffer[Double]): Unit = {
    //var comp_op: (Double, Double) => Double = (a, b) => a
    if (names.size == 1) {
      var comp_op = base(identity, ops(names(0)), consts(0))
      ops(compositionName) = comp_op
    } else {
      var comp_op = base(ops(names(1)), ops(names(0)), consts(0))
      for (i <- 2 until names.size) {
        comp_op = base(ops(names(i)), comp_op, consts(i - 1))
      }
      comp_op = base(identity, comp_op, consts(names.size - 1))
      ops(compositionName) = comp_op
    }
  }

}



