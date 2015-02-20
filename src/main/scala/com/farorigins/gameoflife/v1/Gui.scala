package com.farorigins.gameoflife.v1

/**
 * Created by murat.ozkan on 19/02/15.
 */
import javax.swing.{JFrame, JComponent}
import scala.collection.immutable.List
import java.awt.{Color, Graphics}

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