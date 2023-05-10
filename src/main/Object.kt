package main

import java.awt.Color
import java.awt.Graphics
import java.awt.Polygon

class Object(x: DoubleArray, y: DoubleArray, c: Color, n: Int, seeThrough: Boolean) {
    var P: Polygon
    var c: Color
    @JvmField
    var draw = true
    @JvmField
    var visible = true
    @JvmField
    var seeThrough: Boolean
    var lighting = 1.0

    init {
        //ポリゴンを定義
        P = Polygon()
        for (i in x.indices) {
            P.addPoint(x[i].toInt(), y[i].toInt())
        }
        this.c = c
        this.seeThrough = seeThrough
    }

    fun updatePolygon(x: DoubleArray, y: DoubleArray) {
        //ポリゴンをリセット
        P.reset()
        for (i in x.indices) {
            P.xpoints[i] = x[i].toInt()
            P.ypoints[i] = y[i].toInt()
            P.npoints = x.size
        }
    }

    fun drawPolygon(g: Graphics) {
        if (draw && visible) {
            g.color = Color((c.red * lighting).toInt(), (c.green * lighting).toInt(), (c.blue * lighting).toInt())
            if (seeThrough) {
                g.drawPolygon(P)
            } else {
                g.fillPolygon(P)
            }
            if (Screen.OutLines) {
                g.color = Color(0, 0, 0)
                g.drawPolygon(P)
            }
            if (Screen.PolygonOver === this) {
                g.color = Color(255, 255, 255, 100)
                g.fillPolygon(P)
            }
        }
    }

    fun MouseOver(): Boolean {
        return P.contains(Main.ScreenSize.getWidth() / 2, Main.ScreenSize.getHeight() / 2)
    }
}