package com.denkora.managers

import com.denkora.image.Selection

/**
  * Created by denkoRa on 9/15/2018.
  */
trait Manager {
  var selected: Selection = Selection()

  def setSelection(sel: Selection): Unit = {
    selected = sel
  }

  def getSelection(): Option[Selection] = {
    selected.active match {
      case true => Some(selected)
      case false => None
    }
  }
}
