package com.denkora.image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.util.matching.Regex
/**
  * Created by denkoRa on 9/6/2018.
  */
case class Image (img: BufferedImage) {
  private val w = img.getWidth
  private val h = img.getHeight
  def getWidth: Int = w
  def getHeight: Int = h
  val pixelMatrix = Array.ofDim[Double](w, h, 4)

  for (i <- 0 until w)
    for (j <- 0 until h) {
      val p = Pixel(img.getRGB(i, j))
      pixelMatrix(i)(j)(0) = p.red / 255.0
      pixelMatrix(i)(j)(1) = p.green / 255.0
      pixelMatrix(i)(j)(2) = p.blue / 255.0
      pixelMatrix(i)(j)(3) = p.alpha / 255.0
    }

  def toBufferedImage: BufferedImage = {
    val res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    for (i <- 0 until w)
      for (j <- 0 until h) {
        res.setRGB(i, j, getPixel(i, j).toInt)
      }
    res
  }

  def insideBounds(point: (Int, Int)): Boolean = {
    point._1 < getWidth && point._1 >= 0 && point._2 < getHeight && point._2 >= 0
  }

  def getPixel(i: Int, j: Int): Pixel = {
    val r = (pixelMatrix(i)(j)(0) * 255).toInt
    val g = (pixelMatrix(i)(j)(1) * 255).toInt
    val b = (pixelMatrix(i)(j)(2) * 255).toInt
    val a = (pixelMatrix(i)(j)(3) * 255).toInt
    Pixel(r, g, b, a)
  }

  def applyOp(f: Double => Double, limit: Double => Double, selected: Option[Selection]): Unit = {
    val sel = selected.getOrElse(SelectionFactory("", (0,0), (w - 1, h - 1)))
    for (t <- sel.points; if insideBounds(t)) {
      for (j <- 0 to 2) {
        pixelMatrix(t._1)(t._2)(j) = limit(f(pixelMatrix(t._1)(t._2)(j)))
      }
    }
  }

  def applyFilter(filter: Array[Array[Array[Double]]] => Array[Array[Array[Double]]], selected: Option[Selection]): Unit = {
    val newPixelMatrix = filter(pixelMatrix)
    val sel = selected.getOrElse(SelectionFactory("", (0,0), (w - 1, h - 1)))
    for (c <- 0 to 2)
      setChannel(c, newPixelMatrix, sel)
  }

  def setChannel(c: Int, m: Array[Array[Array[Double]]], selected: Selection): Unit = {
    for(t <- selected.points; if insideBounds(t))
      pixelMatrix(t._1)(t._2)(c) = m(t._1)(t._2)(c)
  }

  def getChannel(c: Int): Array[Array[Double]] = {
    val res = Array.ofDim[Double](w, h)
    for (i <- 0 until w; j <- 0 until h)
        res(i)(j) = pixelMatrix(i)(j)(c)
    res
  }
}

object Image {
  val pngFormat = BufferedImage.TYPE_INT_ARGB
  val jpgFormat = BufferedImage.TYPE_INT_RGB

  def apply(pixels: Array[Array[Pixel]], w: Int, h: Int): Image = {
    val res = new BufferedImage(w, h, pngFormat)
    for (i <- 0 until w)
      for (j <- 0 until h)
        res.setRGB(i, j, pixels(i)(j).toInt)
    Image(res)
  }

  def apply(path: String):Image = {
    Image(ImageIO.read(new File(path)))
  }

  private def formatToExt(format: Int): String = {
    format match {
      case `pngFormat` => "png"
      case `jpgFormat` => "jpg"
    }
  }

  private def formatFromPath(path: String): Int = {
    val jpg: Regex = "\\.jpg".r
    val it = jpg.findAllIn(path)
    if (it.hasNext) jpgFormat else pngFormat
  }

  def save(img: Image, filePath: String):Unit = {
    saveWithFormat(img, filePath, formatFromPath(filePath))
  }

  private def saveWithFormat(img: Image, filePath: String, format: Int):Unit = {
    val buf = img.toBufferedImage
    ImageIO.write(buf, formatToExt(format), new File(filePath))
  }
}