package main2_8

import java.awt.Color
import java.io.File
import java.io.FileNotFoundException
import java.util.*

/*
* rscフォルダ内のテキストを二次元配列に変換し、
* オブジェクトとしてScreen.javaにあるList "Cube"にaddするclass
* */
internal class TextToObject(path: String?) {
    init {
        val f = File(path)
        val sc: Scanner
        try {
            //ファイルの読み込み
            sc = Scanner(f)
            //行数カウント用変数
            var c = 0
            //１行事ファイルから文字列を抜き出す
            while (sc.hasNextLine()) {
                line.add(sc.nextLine())
                println(line[c])
                c++
            }
            //リソース解放
            sc.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        println("copied")
        //行数
        val row = line.size
        //列数
        val col = line[0].length
        //ファイルから2次元配列に代入する為に定義
        val x = Array(row) { IntArray(col) }
        for (i in 0 until row) {
            for (j in 0 until col) {
                //String -> Char -> int に変換
                x[i][j] = Character.getNumericValue(line[i][j])
            }
        }
        if (f != null) {
            for (i in 0 until row) {
                for (j in 0 until col) {
                    //それぞれに対応した色にしてCubeを作成
                    when (x[i][j]) {
                        1 -> Screen.Cube.add(Cube(j.toDouble(), -10.0, (row - i).toDouble(), 1.0, 1.0, 1.0, Color(255, 0, 0)))
                        2 -> Screen.Cube.add(Cube(j.toDouble(), -10.0, (row - i).toDouble(), 1.0, 1.0, 1.0, Color(98, 86, 26)))
                        3 -> Screen.Cube.add(Cube(j.toDouble(), -10.0, (row - i).toDouble(), 1.0, 1.0, 1.0, Color(237, 148, 102)))
                    }
                }
            }
        }
    }

    companion object {
        //文字列を格納するList
        var line = ArrayList<String>()
    }
}