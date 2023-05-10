package main2_8

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Toolkit
import javax.swing.JPanel

//import  io.KeyHandler;
class Menu : JPanel() {
    var path = "./rsc/BlueFlame.png"
    var font: Font? = null
    var img = Toolkit.getDefaultToolkit().getImage(path)

    init {
        this.isFocusable = true
        this.requestFocus()
        background = Color.BLACK
        isFocusable = true
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        draw(g)
    }

    private fun draw(g: Graphics) {
        g.drawImage(img, 0, 0, this)
        font = Font("Arial", Font.BOLD, 20)
        g.font = font
        g.drawString(System.currentTimeMillis().toString() + "ms", 10, 20)
        font = Font("Arial", Font.BOLD, 50)
        g.font = font
        g.color = Color.GREEN
        val l = 100
        val x = (Math.cos(t) * 2 * l).toInt() + width / 2 - l
        val y = (Math.sin(t) * 2 * l).toInt() + height / 2 - l
        g.fillOval(x, y, 200, 200)
        g.color = Color.RED
        g.drawString("Test", width / 2 - 50, height / 2)
        sleep()
    }

    private fun sleep() {
        try {
            Thread.sleep(10)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        t += 0.02
        repaint()
    }

    companion object {
        var t = 0.0
    }
}