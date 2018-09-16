package com.denkora.exceptions

/**
  * Created by denkoRa on 9/16/2018.
  */
class NoActiveException extends Exception {
  override def toString: String = {
    "THERE ARE NO ACTIVE LAYERS"
  }
}
