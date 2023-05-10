package main2_8

import java.awt.Color
import java.awt.Graphics
import javax.swing.JPanel

class Xcs internal constructor() : JPanel() {
    private var t = 0.0
    private var st = 1.0

    init {
        background = Color.BLACK
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.color = Color.WHITE
        val num = 10
        var dt = Math.PI / num
        for (i in 0 until num) {
            dt += 2 * Math.PI / num
            draw(g, dt)
        }
        g.drawString(System.currentTimeMillis().toString() + "", 10, 20)
        sleep()
    }

    private fun draw(g: Graphics, dt: Double) {
        val x: Int
        val y: Int
        val lRad = 100
        x = (Math.cos(t + dt) * lRad * 2 * st).toInt() + width / 2 - lRad
        y = (Math.sin(t + dt) * lRad * 2 * st).toInt() + height / 2 - lRad
        val rad = 50
        g.fillOval(x, y, rad * 2, rad * 2)
        g.drawLine(x + rad, y + rad, width / 2, height / 2)
    }

    private fun sleep() {
        try {
            Thread.sleep(5)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        t += 0.02
        st += 0.001
        repaint()
    }
}