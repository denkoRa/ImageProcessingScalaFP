package com.denkora.image

import java.awt.image.BufferedImage

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * Created by denkoRa on 9/10/2018.
  */
case class Layer(img: Image, var opacity: Double) {
  var active = true

  def activate = active = true
  def deactivate = active = false

  def width = img.getWidth
  def height = img.getHeight

  def setOpacity(o: Double): Unit = {
    opacity = o
  }
}

class NoActiveLayers extends Exception

object LayerFactory {

  val layers: ArrayBuffer[Layer] = ArrayBuffer[Layer]()

  def apply(path: String, opacity: Double) : Layer = {
    val l = Layer(Image(path), opacity)
    layers += l
    l
  }

  def n = layers.size

  def deactivateLayer(ind: Int) {
    layers(ind - 1).deactivate
  }

  def activateLayer(ind: Int) {
    layers(ind - 1).activate
  }

  def mergeLayers(): Image = {
    if (layers.find(p => p.active).isEmpty)
      throw new NoActiveLayers
    val end = layers.size - 1
    val lastImg = layers(end).img
    val w = lastImg.getWidth
    val h = lastImg.getHeight
    val pixels = Array.fill[Pixel](w, h)(Pixel())

    for (i <- end to 0 by -1) {
      if (layers(i).active) {
        for (j <- 0 until w)
          for (k <- 0 until h)
            pixels(j)(k) = pixels(j)(k) * (1 - layers(i).opacity) + layers(i).img.getPixel(j, k) * layers(i).opacity
      }
    }
    Image(pixels, w, h)
  }

}
