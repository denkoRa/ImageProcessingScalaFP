package com.denkora.exceptions

/**
  * Created by denkoRa on 9/16/2018.
  */
class ValueException extends Exception {
  override def toString: String = {
    "ENTERED VALUE MUST BE BETWEEN 0 AND 1 INCLUSIVELY"
  }
}
