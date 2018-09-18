package com.denkora.app
import com.denkora.exceptions.{KeyNotFoundException, ValueException}
import com.denkora.image.{LayerFactory, RGBColor, SelectionFactory}
import com.denkora.managers.{FilterManager, OperationManager}

import scala.collection.mutable.ArrayBuffer
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
    try {
      val opt = scala.io.StdIn.readInt()
      opt match {
        case 0 => System.exit(0)
        case 1 => layerMenu
        case 2 => selectionMenu
        case 3 => operationMenu
        case 4 => filterMenu
      }
    }
    catch {
      case e: Exception => println(e.toString)
    }
    main()
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
    try {
      val opt = scala.io.StdIn.readInt()
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
          println("Enter new opacity value")
          val opacity = scala.io.StdIn.readDouble()
          if (opacity < 0 || opacity > 1) throw new ValueException
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

    try {
      val opt = scala.io.StdIn.readInt()
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

    def readManyOps(f: String): (String, ArrayBuffer[String], ArrayBuffer[Double]) = {
      val ops = om.ops.keySet
      println("Enter names of existing operations (separated by newline)")
      println("(if you want to stop enter anything that is not a name of existing operation)")
      var opName = scala.io.StdIn.readLine()
      val opNames: ArrayBuffer[String] = ArrayBuffer()
      while (ops.contains(opName)) {
        opNames += opName
        opName = scala.io.StdIn.readLine()
      }
      if (opNames.isEmpty) {
        println("Empty list => cancelling!")
        operationMenu()
      }
      val constants: ArrayBuffer[Double] = ArrayBuffer()
      for (name <- opNames) {
        var d = 0.0
        if (om.basicOps.contains(name)) {
          println("Enter constant (Double) for operation " + name + " (if not necessary just type anything)")
          d = try {
            scala.io.StdIn.readDouble()
          } catch {
            case _: Throwable => 0
          }
        }
        constants += d
      }
      println(s"Enter the name of ${f}")
      val name = scala.io.StdIn.readLine()
      (name, opNames, constants)
    }

    try {
      val opt = scala.io.StdIn.readInt()
      opt match {
        case 1 => om.listOps()
        case 2 => {
          val par = readManyOps("composite operation")
          om.composeOp(par._1, par._2, par._3)
        }
        case 3 => {
          val par = readManyOps("sequence of operations")
          om.sequenceOp(par._1, par._2, par._3)
        }
        case 4 => {
          println("Enter the name of operation to apply")
          val name = scala.io.StdIn.readLine()
          if (!om.ops.keySet.contains(name)) throw new KeyNotFoundException
          var c = 0.0
          if (om.basicOps.contains(name)) {
            println("Enter constant (Double) to be used as second argument for the operation")
            c = scala.io.StdIn.readDouble()
          }
          println("Apply to:")
          println("1. All active layers")
          println("2. Chosen layer")
          val subOpt = scala.io.StdIn.readInt()

          subOpt match {
            case 1 => om.applyOp(name, for (l <- lf.layers; if l.active) yield l, c)
            case 2 => {
              println("Enter index of chosen layer")
              val idx = scala.io.StdIn.readInt()
              om.applyOp(name, lf.layers(idx - 1), c)
            }
          }
        }
        case 5 => {
          println("Enter name of sequence of operations")
          val name = scala.io.StdIn.readLine()
          if (!om.sequenceOps.keySet.contains(name)) throw new KeyNotFoundException
          println("Apply to:")
          println("1. All active layers")
          println("2. Chosen layer")
          val subOpt = scala.io.StdIn.readInt()

          subOpt match {
            case 1 => om.applySequence(name, for (l <- lf.layers; if l.active) yield l)
            case 2 => {
              println("Enter index of chosen layer")
              val idx = scala.io.StdIn.readInt()
              om.applySequence(name, lf.layers(idx - 1))
            }
          }
        }
        case 0 => main()
      }
    }
    catch {
      case e: Exception => println(e.toString)
    }
    operationMenu()
  }

  def filterMenu(): Unit = {
    println("### Filters menu ###")
    println("1. List filters")
    println("2. Set neighbour distance")
    println("3. Get current distance")
    println("4. Set weighted matrix")
    println("5. Get current weighted matrix")
    println("6. Apply filter")
    println("0. Go to main menu")

    try {
      val opt = scala.io.StdIn.readInt()

      opt match {
        case 1 => fm.listFilters()
        case 2 => {
          println("Enter distance")
          val d = scala.io.StdIn.readInt()
          fm.setDist(d)
        }
        case 3 => println(fm.getDist())
        case 4 => {
          val dim = fm.getDist() * 2 + 1
          println(s"Enter ${dim * dim} numbers (Double)")
          val w = Array.ofDim[Double](dim, dim)
          for (i <- 0 until dim; j <- 0 until dim)
            w(i)(j) = scala.io.StdIn.readDouble()
          fm.setWeights(w)
        }
        case 5 => {
          val dim = fm.getDist() * 2 + 1
          val w = fm.getWeights()
          for (i <- 0 until dim) {
            println(w(i).mkString(" "))
          }
        }
        case 6 => {
          println("Enter filter name")
          val name = scala.io.StdIn.readLine()
          if (!fm.filters.contains(name)) throw new KeyNotFoundException
          var (r, g, b) = (255, 255, 255)
          if (name == "fill") {
            println("Enter RGB values for the color")
            r = scala.io.StdIn.readInt()
            g = scala.io.StdIn.readInt()
            b = scala.io.StdIn.readInt()
          }
          println("Apply to:")
          println("1. All active layers")
          println("2. Chosen layer")
          val subOpt = scala.io.StdIn.readInt()

          subOpt match {
            case 1 => {
              val ls = for (l <- lf.layers; if l.active) yield l
              if (name == "fill") fm.fillColor(ls, RGBColor(r, g, b))
              else fm.applyFilter(name, ls)
            }
            case 2 => {
              println("Enter index of chosen layer")
              val idx = scala.io.StdIn.readInt()
              if (name == "fill") fm.fillColor(lf.layers(idx - 1), RGBColor(r, g, b))
              else fm.applyFilter(name, lf.layers(idx - 1))
            }
          }
        }
        case 0 => main()
      }
    }
    catch {
      case e: Exception => println(e.toString)
    }
    filterMenu()
  }
}
