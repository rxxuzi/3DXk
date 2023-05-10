package main2_8

class Vector(x: Double, y: Double, z: Double) {
    var x = 0.0
    var y = 0.0
    var z = 0.0

    init {
        /*�x�N�g���̑傫�����v�Z*/
        val length = Math.sqrt(x * x + y * y + z * z)
        /*���`�Ɨ��̎��Ɏ��s�B���`�]���̎��ɂ�x,y,z�̒l��0��*/if (length > 0) {
            this.x = x / length
            this.y = y / length
            this.z = z / length
        }
    }

    /*�x�N�g���̒��p�Ɍ�������x�N�g���̍쐬*/
    fun CrossProduct(V: Vector): Vector {
        return Vector(
                y * V.z - z * V.y,
                z * V.x - x * V.z,
                x * V.y - y * V.x)
    }
}