package com.denkora.image

import java.awt.image.BufferedImage
import java.nio.file.{Path, Paths}

import com.denkora.exceptions.{IndexException, NoActiveException, ValueException}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * Created by denkoRa on 9/10/2018.
  */
case class Layer(path: String, ind: Int, img: Image, var opacity: Double) {
  var active = true

  def activate = active = true
  def deactivate = active = false

  def width = img.getWidth
  def height = img.getHeight

  def setOpacity(o: Double): Unit = {
    opacity = o
  }

  override def toString: String = {
    path + " " + active.toString + " " + opacity.toString
  }
}

object LayerFactory {

  val layers: ArrayBuffer[Layer] = ArrayBuffer[Layer]()

  def apply(path: String, opacity: Double) : Layer = {
    val l = Layer(path, layers.size, Image(path), opacity)
    layers += l
    l
  }

  def n = layers.size

  def setOpacity(ind: Int, opacity: Double): Unit = {
    if (ind > n) throw new IndexException
    if (opacity > 1 || opacity < 0) throw new ValueException
    layers(ind - 1).setOpacity(opacity)
  }

  def deactivateLayer(ind: Int) {
    require(ind <= n)
    layers(ind - 1).deactivate
  }

  def activateLayer(ind: Int) {
    require(ind <= n)
    layers(ind - 1).activate
  }

  def mergeLayers(fname: String): Image = {
    if (layers.find(p => p.active).isEmpty)
      throw new NoActiveException
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
    val img = Image(pixels, w, h)
    Image.save(img, fname)
    img
  }

  def listLayers(): Unit = {
    var c = 1
    for (l <- layers) {
      println(c.toString + ". " + l.toString)
      c += 1
    }
  }

}
