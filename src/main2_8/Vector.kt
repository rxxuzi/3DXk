package main2_8

class Vector(x: Double, y: Double, z: Double) {
    var x = 0.0
    var y = 0.0
    var z = 0.0

    init {
        /*ベクトルの大きさを計算*/
        val length = Math.sqrt(x * x + y * y + z * z)
        /*線形独立の時に実行。線形従属の時にはx,y,zの値は0に*/if (length > 0) {
            this.x = x / length
            this.y = y / length
            this.z = z / length
        }
    }

    /*ベクトルの直角に交差するベクトルの作成*/
    fun CrossProduct(V: Vector): Vector {
        return Vector(
                y * V.z - z * V.y,
                z * V.x - x * V.z,
                x * V.y - y * V.x)
    }
}