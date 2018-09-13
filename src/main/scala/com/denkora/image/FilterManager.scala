package com.denkora.image

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

  def median(m: Array[Array[Double]], dist: Int): Array[Array[Double]] = {
    val w = m.length
    val h = m(0).length
    val res = Array.ofDim[Double](w, h)
    for (i <- 0 until w; j <- 0 until h) {
      val buf: ArrayBuffer[Double] = ArrayBuffer()
      for (x <- scala.math.max(0, i - dist) to scala.math.min(w - 1, i + dist);
          y <- scala.math.max(0, j - dist) to scala.math.min(h - 1, j + dist)) {
        buf += m(x)(y)
      }
      res(i)(j) = medianOfArray(buf)
    }
    res
  }

  def wam(m: Array[Array[Double]], dist: Int, weights: Array[Array[Double]]): Array[Array[Double]] = {
    m
  }

  def filter(name: String, d: Int): (Array[Array[Double]]) => Array[Array[Double]] = {
    def f(m: Array[Array[Double]]): Array[Array[Double]] = {
      name match {
        case "median" => median(m, d)
        case "wam" => wam(m, d, weights)
      }
    }
    f
  }

  def applyFilter(name: String, img: Image, d: Int): Unit = {
    img.applyFilter(filter(name, d))
  }
}
