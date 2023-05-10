package main

/*DPolygon.class�̌v�Z�pclass*/
object Calculator {
    var t = 0.0
    var RVector1: Vector? = null
    var RVector2: Vector? = null
    var ViewVector: Vector? = null
    var RotationVector: Vector? = null
    var DirectionVector: Vector? = null
    var PlaneVector1: Vector? = null
    var PlaneVector2: Vector? = null
    var P: Plane? = null
    var CalculateFocusPosition = DoubleArray(2)

    /*X , Y ���W�̕`��̌v�Z*/
    fun CalculatePositionP(ViewFrom: DoubleArray, ViewTo: DoubleArray?, x: Double, y: Double, z: Double): DoubleArray {
        val projP = getProject(ViewFrom, ViewTo, x, y, z, P)
        return getDrawP(projP[0], projP[1], projP[2])
    }

    fun getProject(ViewFrom: DoubleArray, ViewTo: DoubleArray?, x: Double, y: Double, z: Double, P: Plane?): DoubleArray {

        //���_����Ƃ���|�C���g�܂ł̃x�N�g��
        var x = x
        var y = y
        var z = z
        val ViewToPoint = Vector(x - ViewFrom[0], y - ViewFrom[1], z - ViewFrom[2])
        val VNP = P!!.NV.x * P.P[0] + P.NV.y * P.P[1] + P.NV.z * P.P[2]
        val VCP = P.NV.x * ViewFrom[0] + P.NV.y * ViewFrom[1] + P.NV.z * ViewFrom[2]
        val VTP = P.NV.x * ViewToPoint.x + P.NV.y * ViewToPoint.y + P.NV.z * ViewToPoint.z

        /*�x�N�g����ŕ��ʂƏՓ˂��鋗��*/t = (VNP - VCP) / VTP

        //���ʓ��̓_
        x = ViewFrom[0] + ViewToPoint.x * t
        y = ViewFrom[1] + ViewToPoint.y * t
        z = ViewFrom[2] + ViewToPoint.z * t
        return doubleArrayOf(x, y, z)
    }

    //2������Ƀ|���S����`�悷�邽�߂̃��\�b�h
    private fun getDrawP(x: Double, y: Double, z: Double): DoubleArray {
        val DrawX = RVector2!!.x * x + RVector2!!.y * y + RVector2!!.z * z
        val DrawY = RVector1!!.x * x + RVector1!!.y * y + RVector1!!.z * z
        return doubleArrayOf(DrawX, DrawY)
    }

    /*��]�x�N�g���̃��\�b�h*/
    private fun getRotationVector(ViewFrom: DoubleArray, ViewTo: DoubleArray): Vector {
        val dx = Math.abs(ViewFrom[0] - ViewTo[0])
        val dy = Math.abs(ViewFrom[1] - ViewTo[1])
        var xRot: Double
        var yRot: Double
        xRot = dy / (dx + dy)
        yRot = dx / (dx + dy)
        if (ViewFrom[1] > ViewTo[1]) xRot = -xRot
        if (ViewFrom[0] < ViewTo[0]) yRot = -yRot
        return Vector(xRot, yRot, 0.0)
    }

    @JvmStatic
	fun VectorInfo() {
        /*�x�N�g���v�Z�@(x�̎n�_ - x�̏I�_) , (y�̎n�_ - y�̏I�_) , (z�̎n�_ - z�̏I�_)*/
        ViewVector = Vector(Screen.ViewTo[0] - Screen.ViewFrom[0], Screen.ViewTo[1] - Screen.ViewFrom[1], Screen.ViewTo[2] - Screen.ViewFrom[2])
        //�����x�N�g�� (�P�ʃx�N�g��)
        DirectionVector = Vector(1.0, 1.0, 1.0)

        /*
		 * 3�����x�N�g����2�����x�N�g���ŕ`�悷��ׂɂ�2�̃x�N�g����p�ӂ���K�v������
		 * �O�����E�V���~�b�g�̐��K�������@��p����
		 */PlaneVector1 = ViewVector!!.CrossProduct(DirectionVector!!)
        PlaneVector2 = ViewVector!!.CrossProduct(PlaneVector1!!)
        P = Plane(PlaneVector1!!, PlaneVector2!!, Screen.ViewTo)

        //��]�x�N�g��
        RotationVector = getRotationVector(Screen.ViewFrom, Screen.ViewTo)
        //��]�x�N�g��1
        RVector1 = ViewVector!!.CrossProduct(RotationVector!!)
        //��]�x�N�g��2
        RVector2 = ViewVector!!.CrossProduct(RVector1!!)
        CalculateFocusPosition = CalculatePositionP(Screen.ViewFrom, Screen.ViewTo, Screen.ViewTo[0], Screen.ViewTo[1], Screen.ViewTo[2])
        CalculateFocusPosition[0] = Screen.zoom * CalculateFocusPosition[0]
        CalculateFocusPosition[1] = Screen.zoom * CalculateFocusPosition[1]
    }
}