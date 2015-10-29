package com.farorigins.gameoflife.v1

/**
 * Created by murat.ozkan on 19/02/15.
 */
import javax.swing.{WindowConstants, JFrame, JComponent}

import scala.collection.immutable.List
import java.awt.{Color, Graphics}

class Gui {

  private[this] var window: Option[JFrame] = _
  private[this] val painter: CellPainter = new CellPainter

  def show(height: Int, width: Int) {
    window = Some(new JFrame())

    window.foreach(w => {
      w.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
      w.setBounds(0, 0, width, height)
      w.getContentPane.add(painter)
      w.setVisible(true)
    })
  }

  def setWorld(world: List[Cell]) = {
    painter.gameWorld = world
    painter.repaint()
  }

  def dispose() = {
    window.foreach(_.dispose())
  }
}

object Gui {
  val CellSize: Int = 20
}

private class CellPainter extends JComponent {
  var gameWorld: List[Cell] = List()

  override def paint(graphics: Graphics) {
    graphics.setColor(Color.BLACK)
    graphics.fillRect(0, 0, this.getWidth, this.getHeight)

    graphics.setColor(Color.GREEN)
    gameWorld filter(cell => cell.status) foreach { cell =>
      graphics.fillRect(cell.pos.col * Gui.CellSize, cell.pos.row * Gui.CellSize, Gui.CellSize, Gui.CellSize)
    }
  }
}