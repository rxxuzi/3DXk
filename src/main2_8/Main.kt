package main2_8

import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.JTextField

/*
 * @author Rxxuzi
 * @version 3.0
 */
object Main {
    @JvmField
	var ScreenSize = Toolkit.getDefaultToolkit().screenSize
    var TF: JTextField? = null
    @JvmField
	val StartUpTime = System.currentTimeMillis()
    private const val Debug = false
    @JvmStatic
    fun main(args: Array<String>) {
        val jf = JFrame()
        val Panel = Screen()
        if (Debug) {
            jf.setSize(1000, 1000)
            jf.add(Menu())
            jf.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        } else {
            jf.isUndecorated = true
            jf.size = ScreenSize
            jf.jMenuBar
            jf.add(Panel)
        }
        jf.isVisible = true
        println("It is working properly.")
    }
}