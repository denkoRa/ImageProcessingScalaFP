package com.denkora.image

import javax.management.remote.JMXServerErrorException

import scala.collection.mutable.HashSet
import scala.Tuple2
import scala.collection.mutable

/**
  * Created by denkoRa on 9/8/2018.
  */
case class Selection(points: HashSet[(Int, Int)]) {
  def + (sel: Selection) = new Selection(points ++ sel.points)

  override def toString: String = points.toString
}

object Selection {
  def apply(topLeft: (Int, Int) = (0, 0), bottomRight: (Int, Int) = (0, 0)): Selection = {
    val hs: HashSet[(Int, Int)] = HashSet()
    for (i <- topLeft._1 to bottomRight._1)
      for (j <- topLeft._2 to bottomRight._2)
        hs.add(i, j)
    Selection(hs)
  }
}