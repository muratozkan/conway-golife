package com.farorigins.gameoflife

/**
 * Created by murat.ozkan on 19/02/15.
 */
import java.awt.{Color, Graphics}
import javax.swing.{JComponent, JFrame}

import scala.collection.immutable.List

class Gui {

  def show(height: Int, width: Int) {
    val window = new JFrame()
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    window.setBounds(0, 0, width, height)
    window.getContentPane.add(Paint)
    window.setVisible(true)
  }

  object Paint extends JComponent {
    var gameWorld: List[Cell] = List()

    def setWorld(world: List[Cell]): Unit = {
      gameWorld = world
      repaint()
    }

    override def paint(graphics: Graphics) {
      graphics.setColor(Color.BLACK)
      graphics.fillRect(0, 0, this.getWidth, this.getHeight)

      graphics.setColor(Color.GREEN)
      gameWorld filter(cell => cell.status) foreach { cell =>
        graphics.fillRect(cell.pos.col * Gui.CellSize, cell.pos.row * Gui.CellSize, Gui.CellSize, Gui.CellSize)
      }
    }
  }
}

object Gui {
  val CellSize: Int = 20
}