package com.farorigins.gameoflife.v1

/**
 * Created by murat.ozkan on 19/02/15.
 */
import javax.swing.{JFrame, JComponent}
import scala.collection.mutable.ArrayBuffer
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
    var coordinates = new ArrayBuffer[(Int, Int, Int, Int)]

    def updateCoordinates(newCoordinates: ArrayBuffer[(Int, Int, Int, Int)]): Unit = {
      coordinates = newCoordinates
      repaint()
    }

    override def paint(graphics: Graphics) {
      graphics.setColor(Color.BLACK)
      graphics.fillRect(0, 0, this.getWidth, this.getHeight)

      graphics.setColor(Color.GREEN)
      coordinates.foreach { cell =>
        graphics.fillRect(
          cell._1,
          cell._2,
          cell._3,
          cell._4
        )
      }
    }
  }
}