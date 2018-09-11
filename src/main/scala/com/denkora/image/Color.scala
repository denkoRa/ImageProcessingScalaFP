package com.denkora.image

/**
  * Created by denkoRa on 9/7/2018.
  */
trait Color {
  def toRGB: RGBColor

  def toHex: String = Integer.toHexString(toRGB.toInt & 0xffffff).toUpperCase.reverse.padTo(6, '0').reverse

  def toAWT: java.awt.Color = {
    val rgb = toRGB
    new java.awt.Color(rgb.red, rgb.green, rgb.blue, rgb.alpha)
  }

  def toPixel: Pixel = {
    val rgb = toRGB
    Pixel(rgb.red, rgb.green, rgb.blue, rgb.alpha)
  }
}

object Color {

  // by setting other colours to 255, then this will default to white if transparency is not available on the image.
  def Transparent: RGBColor = RGBColor(255, 255, 255, 0)

  implicit def int2color(argb: Int): RGBColor = apply(argb)
  implicit def color2rgb(color: Color): RGBColor = color.toRGB
  implicit def color2awt(color: Color): java.awt.Color = new java.awt.Color(color.toRGB.toInt)
  implicit def awt2color(awt: java.awt.Color): RGBColor = RGBColor(awt.getRed, awt.getGreen, awt.getBlue, awt.getAlpha)

  def apply(red: Int, green: Int, blue: Int, alpha: Int = 255): RGBColor = RGBColor(red, green, blue, alpha)
  def apply(argb: Int): RGBColor = {
    val alpha = (argb >> 24) & 0xFF
    val red = (argb >> 16) & 0xFF
    val green = (argb >> 8) & 0xFF
    val blue = argb & 0xFF
    RGBColor(red, green, blue, alpha)
  }

  val White = RGBColor(255, 255, 255)
  val Black = RGBColor(0, 0, 0)
}

/**
  * Red/Green/Blue
  *
  * The red, green, blue, and alpha components should be between [0,255].
  */
case class RGBColor(red: Int, green: Int, blue: Int, alpha: Int = 255) extends Color {
  require(0 <= red && red <= 255, "Red component is invalid")
  require(0 <= green && green <= 255, "Green component is invalid")
  require(0 <= blue && blue <= 255, "Blue component is invalid")
  require(0 <= alpha && alpha <= 255, "Alpha component is invalid")

  /**
    * Returns as an int the value of this color. The RGB and alpha components are packed
    * into the int as byes.
    * @return
    */
  def toInt: Int = toARGBInt
  def toARGBInt: Int = ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | blue & 0xFF

  override def toRGB: RGBColor = this

}
