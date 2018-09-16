package com.denkora.app
import com.denkora.image.{LayerFactory, SelectionFactory}
import com.denkora.managers.{FilterManager, OperationManager}
/**
  * Created by denkoRa on 9/15/2018.
  */
object Menus {
  val om = OperationManager
  val fm = FilterManager
  val sf = SelectionFactory
  val lf = LayerFactory

  def main(): Unit = {
    println("### Main menu ###")
    println("1. Layers menu")
    println("2. Selections menu")
    println("3. Operations menu")
    println("4. Filters menu")
    println("0. Exit app")

    val opt = scala.io.StdIn.readInt()

    opt match {
      case 0 => System.exit(0)
      case 1 => layerMenu
      case 2 => selectionMenu
      case 3 => operationMenu
      case 4 => filterMenu
    }
  }

  def layerMenu(): Unit = {
    println("### Layers menu ###")
    println("1. Create new layer")
    println("2. List layers")
    println("3. Activate layer")
    println("4. Deactivate layer")
    println("5. Edit opacity of a layer")
    println("6. Create image by merging all active layers")
    println("0. Go to main menu")
    val opt = scala.io.StdIn.readInt()

    try {
      opt match {
        case 1 => {
          println("Enter the path to the image and opacity of the layer in next line")
          val path = scala.io.StdIn.readLine()
          val opacity = scala.io.StdIn.readDouble()
          lf(path, opacity)
        }
        case 2 => {
          lf.listLayers()
        }
        case 3 => {
          println("Enter layer index")
          val idx = scala.io.StdIn.readInt()
          lf.activateLayer(idx)
        }
        case 4 => {
          println("Enter layer index")
          val idx = scala.io.StdIn.readInt()
          lf.deactivateLayer(idx)
        }
        case 5 => {
          println("Enter layer index")
          val idx = scala.io.StdIn.readInt()
          val opacity = scala.io.StdIn.readDouble()
          lf.setOpacity(idx, opacity)
        }
        case 6 => {
          println("Enter the name of the resulting image")
          val fname = scala.io.StdIn.readLine()
          lf.mergeLayers(fname)
        }
        case 0 => {
          main()
        }
      }
    }
    catch {
      case e: Exception => println(e.toString)
    }
    layerMenu()
  }

  def selectionMenu(): Unit = {
    println("### Selections menu ###")
    println("1. Create selection")
    println("2. List selections")
    println("3. Activate selection")
    println("4. Deactivate selection")
    println("5. Delete selection")
    println("0. Go to main menu")

    val opt = scala.io.StdIn.readInt()
    try {
      opt match {
        case 1 => {
          println("Enter name of the selection and then 4 coordinates of top left and bootom right pixel, respectively")
          val name = scala.io.StdIn.readLine()
          val x1 = scala.io.StdIn.readInt()
          val y1 = scala.io.StdIn.readInt()
          val x2 = scala.io.StdIn.readInt()
          val y2 = scala.io.StdIn.readInt()
          sf(name, (x1, y1), (x2, y2))
        }
        case 2 => {
          sf.listSelections()
        }
        case 3 => {
          println("Enter name of the selection to activate")
          val name = scala.io.StdIn.readLine()
          sf.activateSelection(name)
        }
        case 4 => {
          println("Enter name of the selection to deactivate")
          val name = scala.io.StdIn.readLine()
          sf.deactivateSelection(name)
        }
        case 5 => {
          println("Enter name of the selection to delete")
          val name = scala.io.StdIn.readLine()
          sf.deleteSelection(name)
        }
        case 0 => {
          main()
        }
      }
    }
    catch {
      case e: Exception => println(e.toString)
    }
    selectionMenu()
  }

  def operationMenu(): Unit = {
    println("### Operations menu ###")
    println("1. List operations")
    println("2. Create composite operation")
    println("3. Create sequence of operations")
    println("4. Apply operation")
    println("5. Apply sequence of operations")
    println("0. Go to main menu")
  }

  def filterMenu(): Unit = {
    println("### Filters menu ###")
    println("1. List filters")
    println("2. Set neighbour distance")
    println("3. Set weighted matrix")
    println("4. Apply filter")
    println("0. Go to main menu")
  }
}
