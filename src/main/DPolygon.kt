package main

import java.awt.Color

public class DPolygon(@JvmField var x: DoubleArray, @JvmField var y: DoubleArray, @JvmField var z: DoubleArray, var c: Color, seeThrough: Boolean) {
    @JvmField
    var draw = true
    var seeThrough = false
    lateinit var CalculatorPosition: DoubleArray
    lateinit var nx: DoubleArray
    lateinit var ny: DoubleArray
    @JvmField
    var DrawablePolygon: Object? = null
    @JvmField
    var AverageDistance = 0.0

    init {
        this.seeThrough = seeThrough
        createPolygon()
    }

    //�|���S�����쐬���郁�\�b�h
    fun createPolygon() {
        DrawablePolygon = Object(DoubleArray(x.size), DoubleArray(x.size), c, Screen.DPolygons.size, seeThrough)
    }

    //�|���S�����X�V���郁�\�b�h
    fun updatePolygon() {
        nx = DoubleArray(x.size)
        ny = DoubleArray(x.size)
        draw = true
        for (i in x.indices) {
            CalculatorPosition = Calculator.CalculatePositionP(Screen.ViewFrom, Screen.ViewTo, x[i], y[i], z[i])
            nx[i] = Main.ScreenSize.getWidth() / 2 - Calculator.CalculateFocusPosition[0] + CalculatorPosition[0] * Screen.zoom
            ny[i] = Main.ScreenSize.getHeight() / 2 - Calculator.CalculateFocusPosition[1] + CalculatorPosition[1] * Screen.zoom

            //���_�̌��ɃI�u�W�F�N�g������ꍇ�A�`�悵�Ȃ�
            if (Calculator.t < 0) {
                draw = false
            }
        }
        DrawablePolygon!!.draw = draw
        DrawablePolygon!!.updatePolygon(nx, ny)
        AverageDistance = GetDistance()
    }

    //�������擾
    fun GetDistance(): Double {
        var total = 0.0
        for (i in x.indices) total += GetDistanceToP(i)
        return total / x.size
    }

    fun GetDistanceToP(i: Int): Double {
        val Fx = Screen.ViewFrom[0] - x[i]
        val Fy = Screen.ViewFrom[1] - y[i]
        val Fz = Screen.ViewFrom[2] - z[i]
        return Math.sqrt(Fx * Fx + Fy * Fy + Fz * Fz)
    }
}