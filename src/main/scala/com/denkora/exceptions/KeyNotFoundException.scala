package com.denkora.exceptions

/**
  * Created by denkoRa on 9/16/2018.
  */
class KeyNotFoundException extends Exception {
  override def toString: String = {
    "NAME NOT FOUND IN THE COLLECTION"
  }
}
