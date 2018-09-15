package com.denkora.image

import scala.collection.mutable.{ArrayBuffer, HashSet}
import scala.collection.mutable

/**
  * Created by denkoRa on 9/8/2018.
  */
case class Selection(points: HashSet[(Int, Int)] = mutable.HashSet()) {
  var active = true

  def activate(): Unit = {
    active = true
  }

  def deactivate(): Unit = {
    active = false
  }

  override def toString: String = points.toString
}

object SelectionFactory {
  val selections: mutable.HashMap[String, Selection] = mutable.HashMap()

  def apply(name: String, topLeft: (Int, Int) = (0, 0), bottomRight: (Int, Int) = (0, 0)): Selection = {
    val hs: HashSet[(Int, Int)] = HashSet()
    for (i <- topLeft._1 to bottomRight._1)
      for (j <- topLeft._2 to bottomRight._2)
        hs.add(i, j)
    selections(name) = Selection(hs)
    selections(name)
  }

  def activateSelection(name: String): Unit = {
    selections(name).activate()
  }

  def deactivateSelection(name: String): Unit = {
    selections(name).deactivate()
  }

  def deleteSelection(name: String): Unit ={
    selections -= name
  }

  def mergeSelections(): Selection = {
    var hs: HashSet[(Int, Int)] = mutable.HashSet()
    selections foreach (x => if (x._2.active) hs = hs ++ x._2.points)
    Selection(hs)
  }
}