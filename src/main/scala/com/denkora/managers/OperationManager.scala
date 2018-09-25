package com.denkora.managers

import com.denkora.image.{Image, Layer}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by denkoRa on 9/8/2018.
  */
object OperationManager extends Manager {
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
  val inversion: (Double, Double) => Double = baseComposition(identity, invsub, 1.0)

  val basicOps: Set[String] = Set("add", "sub", "invsub", "mul", "div", "invdiv", "power", "max", "min")

  def baseComposition(g: (Double, Double) => Double, f: (Double, Double) => Double, c: Double): (Double, Double) => Double = {
    def func(x: Double, y: Double): Double = {
      g(f(x, c), y)
    }
    func
  }

  val ops: mutable.HashMap[String, (Double, Double) => Double] = mutable.HashMap()
  val sequenceOps: mutable.HashMap[String, ArrayBuffer[Double => Double]] = mutable.HashMap()

  def listOps(): Unit = {
    ops.keys.foreach {println}
  }

  def listSeqOps(): Unit = {
    sequenceOps.keys.foreach {println}
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
  ops("identity") = identity

  def op(name: String, const: Double): Double => Double = {
    def f(param: Double): Double = {
      ops(name)(param, const)
    }
    f
  }

  private def applyOp(name: String, img: Image, c: Double): Unit = {
    img.applyOp(op(name, c), limit, getSelection())
  }

  def applyOp(name: String, layers: ArrayBuffer[Layer], c: Double = 0): Unit = {
    for (l <- layers)
      applyOp(name, l.img, c)
  }

  def applyOp(name: String, layer: Layer, c: Double): Unit ={
    applyOp(name, layer.img, c)
  }

  def composeOp(compositionName: String, names: ArrayBuffer[String], consts: ArrayBuffer[Double]): Unit = {
    if (names.size == 1) {
      val comp_op = baseComposition(identity, ops(names(0)), consts(0))
      ops(compositionName) = comp_op
    } else {
      var comp_op = baseComposition(ops(names(1)), ops(names(0)), consts(0))
      for (i <- 2 until names.size) {
        comp_op = baseComposition(ops(names(i)), comp_op, consts(i - 1))
      }
      comp_op = baseComposition(identity, comp_op, consts(names.size - 1))
      ops(compositionName) = comp_op
    }
  }

  def sequenceOp(sequenceName: String, names: ArrayBuffer[String], consts: ArrayBuffer[Double]): Unit = {
    sequenceOps(sequenceName) = for ((n, c) <- names zip consts) yield op(n, c)
  }

  private def applySequence(sequenceName: String, img: Image): Unit = {
    val sel = getSelection()
    for (op <- sequenceOps(sequenceName)) {
      img.applyOp(op, dontLimit, sel)
    }
    img.applyOp(op("identity", 0), limit, sel)
  }

  def applySequence(sequenceName: String, layers: ArrayBuffer[Layer]): Unit = {
    for (l <- layers)
      applySequence(sequenceName, l.img)
  }

  def applySequence(sequenceName: String, layer: Layer): Unit ={
    applySequence(sequenceName, layer.img)
  }
}



