package main2_8

import java.awt.Color

class Cube(x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double, color: Color) {
    //座標
    @JvmField
    var x: Double
    @JvmField
    var y: Double
    var z: Double
    @JvmField
    var dx: Double
    @JvmField
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
    var y1 = 0.0
    var y2 = 0.0
    var y3 = 0.0
    var y4 = 0.0
    @JvmField
    var Polys = arrayOfNulls<DPolygon>(6)

    //角度を収納する配列
    lateinit var angle: DoubleArray

    //x,y,z座標
    var a = x + dx
    var b = y + dy
    var c = z + dz

    //座標と色情報からポリゴンを生成
    init {
        Polys[0] = DPolygon(doubleArrayOf(x, a, a, x), doubleArrayOf(y, y, b, b), doubleArrayOf(z, z, z, z), color, false)
        Polys[1] = DPolygon(doubleArrayOf(x, a, a, x), doubleArrayOf(y, y, b, b), doubleArrayOf(c, c, c, c), color, false)
        Polys[2] = DPolygon(doubleArrayOf(x, x, x, x), doubleArrayOf(y, y, b, b), doubleArrayOf(z, c, c, z), color, false)
        Polys[3] = DPolygon(doubleArrayOf(a, a, a, a), doubleArrayOf(y, y, b, b), doubleArrayOf(z, c, c, z), color, false)
        Polys[4] = DPolygon(doubleArrayOf(x, x, a, a), doubleArrayOf(y, y, y, y), doubleArrayOf(z, c, c, z), color, false)
        Polys[5] = DPolygon(doubleArrayOf(x, x, a, a), doubleArrayOf(b, b, b, b), doubleArrayOf(z, c, c, z), color, false)

        //Screen.javaのDPolygons<List>に転送
        Polys[0]?.let { Screen.DPolygons.add(it) }
        Polys[1]?.let { Screen.DPolygons.add(it) }
        Polys[2]?.let { Screen.DPolygons.add(it) }
        Polys[3]?.let { Screen.DPolygons.add(it) }
        Polys[4]?.let { Screen.DPolygons.add(it) }
        Polys[5]?.let { Screen.DPolygons.add(it) }

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
        for (i in 0..5) {
            Polys[i]?.let { Screen.DPolygons.add(it) }
            Screen.DPolygons.remove(Polys[i])
        }
        val radius = Math.sqrt(dx * dx + dy * dy)
        x1 = x + dx * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[0])
        x2 = x + dx * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[1])
        x3 = x + dx * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[2])
        x4 = x + dx * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[3])
        y1 = y + dy * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[0])
        y2 = y + dy * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[1])
        y3 = y + dy * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[2])
        y4 = y + dy * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[3])
        Polys[0]!!.x = doubleArrayOf(x1, x2, x3, x4)
        Polys[0]!!.y = doubleArrayOf(y1, y2, y3, y4)
        Polys[0]!!.z = doubleArrayOf(z, z, z, z)
        Polys[1]!!.x = doubleArrayOf(x4, x3, x2, x1)
        Polys[1]!!.y = doubleArrayOf(y4, y3, y2, y1)
        Polys[1]!!.z = doubleArrayOf(z + dz, z + dz, z + dz, z + dz)
        Polys[2]!!.x = doubleArrayOf(x1, x1, x2, x2)
        Polys[2]!!.y = doubleArrayOf(y1, y1, y2, y2)
        Polys[2]!!.z = doubleArrayOf(z, z + dz, z + dz, z)
        Polys[3]!!.x = doubleArrayOf(x2, x2, x3, x3)
        Polys[3]!!.y = doubleArrayOf(y2, y2, y3, y3)
        Polys[3]!!.z = doubleArrayOf(z, z + dz, z + dz, z)
        Polys[4]!!.x = doubleArrayOf(x3, x3, x4, x4)
        Polys[4]!!.y = doubleArrayOf(y3, y3, y4, y4)
        Polys[4]!!.z = doubleArrayOf(z, z + dz, z + dz, z)
        Polys[5]!!.x = doubleArrayOf(x4, x4, x1, x1)
        Polys[5]!!.y = doubleArrayOf(y4, y4, y1, y1)
        Polys[5]!!.z = doubleArrayOf(z, z + dz, z + dz, z)
    }

    fun removeCube() {
        for (i in 0..5) {
            Screen.DPolygons.remove(Polys[i])
        }
        Screen.Cube.remove(this)
    }

    companion object {
        const val e = 0.0001
    }
}