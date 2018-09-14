package com.denkora.managers

import com.denkora.image.{Color, Image, RGBColor}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by denkoRa on 9/13/2018.
  */
object FilterManager {
  var filters: mutable.HashMap[String, (Array[Array[Double]]) => Array[Array[Double]]] = mutable.HashMap()
  var weights: Array[Array[Double]] = _

  def setWeights(w: Array[Array[Double]]): Unit ={
    weights = w
  }

  def medianOfArray(seq: Seq[Double]): Double = {
    val sortedSeq = seq.sortWith(_ < _)

    if (seq.size % 2 == 1) sortedSeq(sortedSeq.size / 2)
    else {
      val (up, down) = sortedSeq.splitAt(seq.size / 2)
      (up.last + down.head) / 2
    }
  }

  def median(m: Array[Array[Array[Double]]], dist: Int): Array[Array[Array[Double]]] = {
    val w = m.length
    val h = m(0).length
    val res = Array.ofDim[Double](w, h, 4)
    for (c <- 0 until 4; i <- 0 until w; j <- 0 until h) {
      val buf: ArrayBuffer[Double] = ArrayBuffer()
      for (x <- scala.math.max(0, i - dist) to scala.math.min(w - 1, i + dist);
           y <- scala.math.max(0, j - dist) to scala.math.min(h - 1, j + dist)) {
        buf += m(x)(y)(c)
      }
      res(i)(j)(c) = medianOfArray(buf)
    }
    res
  }

  def wam(m: Array[Array[Array[Double]]], dist: Int, weights: Array[Array[Double]]): Array[Array[Array[Double]]] = {
    val w = m.length
    val h = m(0).length
    val res = Array.ofDim[Double](w, h, 4)
    for (c <- 0 until 4; i <- 0 until w; j <- 0 until h) {
      var neighbourSum = 0.0
      var cnt = 0
      for (x <- scala.math.max(0, i - dist) to scala.math.min(w - 1, i + dist);
           y <- scala.math.max(0, j - dist) to scala.math.min(h - 1, j + dist)) {
        neighbourSum += m(x)(y)(c) * weights(x - i + dist)(y - j + dist)
        cnt += 1
      }
      res(i)(j)(c) = 1.0 * neighbourSum / cnt
    }
    res
  }

  def grayscale(m: Array[Array[Array[Double]]]): Array[Array[Array[Double]]] = {
    val w = m.length
    val h = m(0).length
    val res = Array.ofDim[Double](w, h, 4)
    for (c <- 0 until 3; i <- 0 until w; j <- 0 until h) {
      val v = (m(i)(j)(0) + m(i)(j)(1) + m(i)(j)(2)) / 3
      res(i)(j)(c) = v
    }
    res
  }

  def fill(m: Array[Array[Array[Double]]], color: RGBColor): Array[Array[Array[Double]]] = {
    val w = m.length
    val h = m(0).length
    val res = Array.ofDim[Double](w, h, 4)
    for (i <- 0 until w; j <- 0 until h) {
      res(i)(j)(0) = color.red / 255.0
      res(i)(j)(1) = color.green / 255.0
      res(i)(j)(2) = color.blue / 255.0
    }
    res
  }

  def filter(name: String, d: Int, c: RGBColor): (Array[Array[Array[Double]]]) => Array[Array[Array[Double]]] = {
    def f(m: Array[Array[Array[Double]]]): Array[Array[Array[Double]]] = {
      name match {
        case "fill" => fill(m, c)
        case "grayscale" => grayscale(m)
        case "median" => median(m, d)
        case "wam" => wam(m, d, weights)
      }
    }
    f
  }

  def applyFilter(name: String, img: Image, d: Int = 0, c: Option[RGBColor] = None): Unit = {
    img.applyFilter(filter(name, d, c.getOrElse(Color.Transparent)))
  }

}
