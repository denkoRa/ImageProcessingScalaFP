package com.denkora.image

import com.denkora.exceptions.KeyNotFoundException

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

  override def toString: String = {
    active.toString
  }
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
    if (!selections.contains(name)) throw new KeyNotFoundException
    selections(name).activate()
  }

  def deactivateSelection(name: String): Unit = {
    if (!selections.contains(name)) throw new KeyNotFoundException
    selections(name).deactivate()
  }

  def deleteSelection(name: String): Unit = {
    if (!selections.contains(name)) throw new KeyNotFoundException
    selections -= name
  }

  def listSelections(): Unit = {
    for (x <- selections) {
      println(x)
    }
  }

  def getSelections(): Iterable[Selection] = {
    for (s <- selections; if s._1.size > 0) yield s._2
  }
}