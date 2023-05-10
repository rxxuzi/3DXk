package main2_8

import java.awt.Color
import java.io.File
import java.io.FileNotFoundException
import java.util.*

/*
* rsc�t�H���_���̃e�L�X�g��񎟌��z��ɕϊ����A
* �I�u�W�F�N�g�Ƃ���Screen.java�ɂ���List "Cube"��add����class
* */
internal class TextToObject(path: String?) {
    init {
        val f = File(path)
        val sc: Scanner
        try {
            //�t�@�C���̓ǂݍ���
            sc = Scanner(f)
            //�s���J�E���g�p�ϐ�
            var c = 0
            //�P�s���t�@�C�����當����𔲂��o��
            while (sc.hasNextLine()) {
                line.add(sc.nextLine())
                println(line[c])
                c++
            }
            //���\�[�X���
            sc.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        println("copied")
        //�s��
        val row = line.size
        //��
        val col = line[0].length
        //�t�@�C������2�����z��ɑ������ׂɒ�`
        val x = Array(row) { IntArray(col) }
        for (i in 0 until row) {
            for (j in 0 until col) {
                //String -> Char -> int �ɕϊ�
                x[i][j] = Character.getNumericValue(line[i][j])
            }
        }
        if (f != null) {
            for (i in 0 until row) {
                for (j in 0 until col) {
                    //���ꂼ��ɑΉ������F�ɂ���Cube���쐬
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
        //��������i�[����List
        var line = ArrayList<String>()
    }
}