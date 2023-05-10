package main2_8

import java.awt.Color

public class Pyramid(x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double, color: Color) {
    //座標
    var x: Double
    var y: Double
    var z: Double
    var dx: Double
    var dy: Double
    var dz: Double
    var rotation = Math.PI * 0.75
    var RotAdd = DoubleArray(4)

    //色情報
    var color: Color

    //回転後の座標
    var x1 = 0.0
    var x2 = 0.0
    var x3 = 0.0
    var x4 = 0.0
    var x5 = 0.0
    var y1 = 0.0
    var y2 = 0.0
    var y3 = 0.0
    var y4 = 0.0
    var y5 = 0.0

    //ポリゴン格納クラス
    var Polys = arrayOfNulls<DPolygon>(5)

    //角度を収納する配列
    lateinit var angle: DoubleArray

    //x,y,z座標
    var a = x + dx
    var b = y + dy
    var c = z + dz

    //座標と色情報からポリゴンを生成
    init {

        //double h = Math.sqrt(3) * c / 2 ;
        Polys[0] = DPolygon(doubleArrayOf(x, x + dx, x + dx, x), doubleArrayOf(y, y, y + dy, y + dy), doubleArrayOf(z, z, z, z), color, false)
        Polys[1] = DPolygon(doubleArrayOf(x, x, x + dx), doubleArrayOf(y, y, y, y), doubleArrayOf(z, z + dz, z + dz), color, false)
        Polys[2] = DPolygon(doubleArrayOf(x + dx, x + dx, x + dx), doubleArrayOf(y, y, y + dy), doubleArrayOf(z, z + dz, z + dz), color, false)
        Polys[3] = DPolygon(doubleArrayOf(x, x, x + dx), doubleArrayOf(y + dy, y + dy, y + dy), doubleArrayOf(z, z + dz, z + dz), color, false)
        Polys[4] = DPolygon(doubleArrayOf(x, x, x), doubleArrayOf(y, y, y + dy), doubleArrayOf(z, z + dz, z + dz), color, false)
        Screen.DPolygons.add(Polys[0]!!)
        Screen.DPolygons.add(Polys[1]!!)
        Screen.DPolygons.add(Polys[2]!!)
        Screen.DPolygons.add(Polys[3]!!)
        Screen.DPolygons.add(Polys[4]!!)

        //インスタンス変数に代入
        this.color = color
        this.x = x
        this.y = y
        this.z = z
        this.dx = dx
        this.dy = dy
        this.dz = dz

        //角度情報を取得
        setRotAdd()
        updatePoly()
    }

    fun setRotAdd() {
        angle = DoubleArray(4)
        var xdif = -dx / 2 + e
        var ydif = -dy / 2 + e
        for (i in angle.indices) {
            angle[i] = Math.atan(ydif / xdif)
            if (xdif < 0) {
                angle[i] += Math.PI
            }
            when (i) {
                0 -> {
                    xdif = dx / 2 + e
                    ydif = -dy / 2 + e
                }

                1 -> {
                    xdif = dx / 2 + e
                    ydif = dy / 2 + e
                }

                2 -> {
                    xdif = -dx / 2 + e
                    ydif = dy / 2 + e
                }
            }
            RotAdd[i] = angle[i] + 0.25 * Math.PI
        }
    }

    @Suppress("unused")
    private fun UpdateDirection(toX: Double, toY: Double) {
        val xdif = toX - (x + dx / 2) + e
        val ydif = toY - (y + dy / 2) + e
        var anglet = Math.atan(ydif / xdif) + 0.75 * Math.PI
        if (xdif < 0) anglet += Math.PI
        rotation = anglet
        updatePoly()
    }

    fun updatePoly() {
        for (poly in Polys) {
            Screen.DPolygons.add(poly!!)
            Screen.DPolygons.remove(poly)
        }
        val radius = Math.sqrt(dx * dx + dy * dy)
        x1 = x + dx * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[0])
        x2 = x + dx * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[1])
        x3 = x + dx * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[2])
        x4 = x + dx * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[3])
        x5 = x + dx * 0.5
        y1 = y + dy * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[0])
        y2 = y + dy * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[1])
        y3 = y + dy * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[2])
        y4 = y + dy * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[3])
        y5 = y + dy * 0.5
        Polys[0]!!.x = doubleArrayOf(x1, x2, x3, x4)
        Polys[0]!!.y = doubleArrayOf(y1, y2, y3, y4)
        Polys[0]!!.z = doubleArrayOf(z, z, z, z)
        Polys[1]!!.x = doubleArrayOf(x1, x5, x2)
        Polys[1]!!.y = doubleArrayOf(y1, y5, y2)
        Polys[1]!!.z = doubleArrayOf(z, z + dz, z)
        Polys[2]!!.x = doubleArrayOf(x3, x2, x5)
        Polys[2]!!.y = doubleArrayOf(y3, y2, y5)
        Polys[2]!!.z = doubleArrayOf(z, z, z + dz)
        Polys[3]!!.x = doubleArrayOf(x3, x5, x4)
        Polys[3]!!.y = doubleArrayOf(y3, y5, y4)
        Polys[3]!!.z = doubleArrayOf(z, z + dz, z)
        Polys[4]!!.x = doubleArrayOf(x1, x4, x5)
        Polys[4]!!.y = doubleArrayOf(y1, y4, y5)
        Polys[4]!!.z = doubleArrayOf(z, z, z + dz)
    }

    @Suppress("unused")
    fun removePyramid() {
        for (i in Polys.indices) {
            Screen.DPolygons.remove(Polys[i])
        }
        Screen.Pyramid.remove(this)
    }

    companion object {
        const val e = 0.0001
    }
}