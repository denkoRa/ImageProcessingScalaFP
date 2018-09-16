package com.denkora.managers

import com.denkora.image.Selection
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, HashSet}

/**
  * Created by denkoRa on 9/15/2018.
  */
trait Manager {
  var selected: Iterable[Selection] = ArrayBuffer()

  def setSelection(sel: Iterable[Selection]): Unit = {
    selected = sel
  }

  def getSelection(): Option[Selection] = {
    if (selected.count(s => s.active) > 0) Some(mergeSelections()) else None
  }

  private def mergeSelections(): Selection = {
    var hs: HashSet[(Int, Int)] = mutable.HashSet()
    selected foreach (x => hs = hs ++ x.points)
    Selection(hs)
  }
}
