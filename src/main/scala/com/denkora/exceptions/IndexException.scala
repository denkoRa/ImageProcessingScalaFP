package com.denkora.exceptions

/**
  * Created by denkoRa on 9/16/2018.
  */
class IndexException extends Exception {
  override def toString: String = {
    "CHOSEN INDEX IS GREATER THAN NUMBER OF LAYERS"
  }
}

