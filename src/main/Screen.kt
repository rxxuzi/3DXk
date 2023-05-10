package main

import main.Calculator.VectorInfo
import java.awt.*
import java.awt.event.*
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.JPanel

/*
* Main.java��Frame�Ƀp�l���Ƃ���add����class
* */
public class Screen : JPanel() {
    private var NumberOfDeleteCube = 0

    //�}�E�X�𒆐S�ɒu���Ă������߂Ɏg�p����T�u�N���X
    var r: Robot? = null

    //FPS�̑���
    var drawFPS = 0.0
    var MaxFPS = 2000.0

    //	double SleepTime = 1000.0 / MaxFPS;
    var LastFPSCheck = 0.0
    var Checks = 0.0
    var LastRefresh = 0.0
    var VerticalLook = -0.9 //0.99 ~ -0.99�܂ŁA���̒l�̎��͏�����B���̒l�̎��͉�����
    var HorizontalLook = 0.0 // �C�ӂ̐��l���Ƃ�A���W�A���P�ʂň������
    var VerticalRotationSpeed = 1000.0 //������]�̑���
    var HorizontalRotationSpeed = 500.0 //������]�̑���

    //�z��DPolygon�̕`�悷�鏇�Ԃ�ێ�����z��
    lateinit var NewOrder: IntArray

    //�L�[���͂̏����i�[����z��
    var Control = BooleanArray(15)
    var Press = 10

    /*�`�悵����3D�|���S�����`����class*/
    init {
        //�L�[����
        addKeyListener(KeyTyped())
        isFocusable = true
        //�}�E�X�֘A
        addMouseListener(AboutMouse())
        addMouseMotionListener(AboutMouse())
        addMouseWheelListener(AboutMouse())
        invisibleMouse()
        Cube.add(Cube(0.0, 0.0, 0.0, 4.0, 4.0, 4.0, Color.GRAY))
        Cube.add(Cube(18.0, -4.0, 0.0, 2.0, 2.0, 2.0, Color.red))
        Cube.add(Cube(20.0, -4.0, 0.0, 2.0, 2.0, 2.0, Color.yellow))
        Cube.add(Cube(22.0, -4.0, 0.0, 2.0, 2.0, 2.0, Color.green))
        Cube.add(Cube(24.0, -4.0, 0.0, 2.0, 2.0, 2.0, Color.blue))
        /*
			�P�D�ԁ@R255
			�Q�D��@R255 G150
			�R�D���@R255 G240
			�S�D�΁@G135
			�T�D�@G145 B255
			�U�D���@G100 B190
			�V�D���@R145 B130
		 */

//		Pyramid.add(new Pyramid(2, 2, 3, 4, 4, 4, Color.MAGENTA));
        Pyramid.add(Pyramid(4.0, 17.5, 2.0, 2.0, 2.0, Math.sqrt(3.0), Color.YELLOW))
        if (!debugMode) {
            Cube.add(Cube(10.0, -2.0, 0.0, 2.0, 2.0, 2.0, Color(255, 240, 0)))
            Cube.add(Cube(8.0, -2.0, 0.0, 2.0, 2.0, 2.0, Color(145, 0, 130)))
            Cube.add(Cube(8.0, -2.0, 2.0, 2.0, 2.0, 2.0, Color(10, 90, 130)))
            var i = 0
            while (i < 20) {
                Cube.add(Cube(18.0, i.toDouble(), i.toDouble(), 2.0, 2.0, 2.0, Color.red))
                Cube.add(Cube(20.0, i.toDouble(), i.toDouble(), 2.0, 2.0, 2.0, Color.yellow))
                Cube.add(Cube(22.0, i.toDouble(), i.toDouble(), 2.0, 2.0, 2.0, Color.green))
                Cube.add(Cube(24.0, i.toDouble(), i.toDouble(), 2.0, 2.0, 2.0, Color.blue))
                i += 2
            }
        }

        /*�n�ʂ̕\��*/
        val d = 2 //��
        var i = -10
        while (i < 50) {
            var j = -20
            while (j < 40) {
                if ((i + j) / 2 % 2 == 0) {
                    DPolygons.add(DPolygon(doubleArrayOf(i.toDouble(), i.toDouble(), (i + d).toDouble(), (i + d).toDouble()), doubleArrayOf(j.toDouble(), (j + d).toDouble(), (j + d).toDouble(), j.toDouble()), doubleArrayOf(0.0, 0.0, 0.0, 0.0), Color(255, 179, 219), false))
                } else {
                    DPolygons.add(DPolygon(doubleArrayOf(i.toDouble(), i.toDouble(), (i + d).toDouble(), (i + d).toDouble()), doubleArrayOf(j.toDouble(), (j + d).toDouble(), (j + d).toDouble(), j.toDouble()), doubleArrayOf(0.0, 0.0, 0.0, 0.0), Color(179, 236, 255), false))
                }
                j += d
            }
            i += d
        }

        //�e�L�X�g����I�u�W�F�N�g�𐻑�
        if (!debugMode) {
            val t2o = TextToObject("./rsc/summon.txt")
        }
    }

    /*�`��Ɋւ��郁�\�b�h*/
    public override fun paintComponent(g: Graphics) {
        //�`�惊�Z�b�g
        g.clearRect(0, 0, Main.ScreenSize.getWidth().toInt(), Main.ScreenSize.getHeight().toInt())

        //�J�����𓮂���
        KeyControl()

        //�t�H�[�J�X���ꂽ�|���S�����폜����
        deleteCube()

        //���̃J�����ʒu�ň�ʓI�Ȃ��̂����ׂČv�Z���܂��B
        VectorInfo()

        // �|���S�������e���A�b�v�f�[�g
        for (dPolygon in DPolygons) {
            dPolygon.updatePolygon()
        }

//		//�|���S������]����
//		Cube.get(0).rotation+=0.01;
//		Cube.get(0).updatePoly();

//		Pyramid.get(0).rotation += 0.01;
//		Pyramid.get(0).updatePoly();
//
//		Pyramid.get(1).rotation -= 0.05;
//		Pyramid.get(1).updatePoly();

        //�S�Ẵ|���S���̋������\�[�g
        setOrder()

        //�}�E�X������Ă���|���S������肷��
        setPolygonOver()

        //setOrder�֐��Őݒ肳�ꂽ�����Ń|���S����`��
        for (j in NewOrder) {
            DPolygons[j].DrawablePolygon!!.drawPolygon(g)
        }

        //��ʂ̒����ɃG�C��������
        drawMouseAim(g)

        //�t�H���g�̐ݒ�
        val font = Font(Font.DIALOG, Font.ITALIC, FontSize)
        //
        val VAngle = Math.toDegrees(Math.tan(VerticalLook))
        g.font = font
        g.drawString("FPS : " + drawFPS.toInt(), 10, 15)
        g.drawString("ELAPSED TIME : " + (System.currentTimeMillis() - Main.StartUpTime) + "ms", 10, 30)
        g.drawString("OBJECT : " + Arrays.toString(ViewTo), 10, 45)
        g.drawString("CAMERA : " + Arrays.toString(ViewFrom), 10, 60)
        g.drawString("ZOOM   : " + zoom, 10, 75)
        g.drawString("Vertical   Look : $VerticalLook", 10, 90)
        g.drawString("Horizontal Look(rad) : $HorizontalLook", 10, 105)
        g.drawString("Vertical angle   	 : " + VAngle.toInt() + "��", 10, 120)
        g.drawString("Number Of Polygons : " + DPolygons.size, 10, 135)
        g.drawString("Number Of Cubes    : " + Cube.size, 10, 150)
        g.drawString("Focus Polys ID : " + FocusPolygon.toString(), 10, 170)
        g.font = Font(Font.SANS_SERIF, Font.BOLD, 20)
        g.drawString("CONDITION: " + condition, 10, 190)
        if (Control[10]) {
            Press++
        }
        g.drawString(Press.toString() + "SIZE", 10, 220)

        //�`��X�V�̃C���^�[�o��
        SleepAndRefresh()
        if (firstPersonMode) {
            hitJudgment()
        }
    }

    private fun hitJudgment() {
        if (Cube[0].x < ViewFrom[0] && Cube[0].dx > ViewFrom[0] && Cube[0].y < ViewFrom[1] && Cube[0].dy > ViewFrom[1]) {
            condition = "in the BOX"
            //ViewFrom�ɋ߂����̍��W���̗p
            if (Math.abs(Cube[0].x - ViewFrom[0]) > Math.abs(Cube[0].dx - ViewFrom[0])) {
//				ViewFrom[0] = Cube.get(0).dx + 0.2;
                ViewFrom[0] += 0.1
            } else {
//				ViewFrom[0] = Cube.get(0).x - 0.2;
                ViewFrom[0] -= 0.1
            }
            if (Math.abs(Cube[0].y - ViewFrom[1]) > Math.abs(Cube[0].dy - ViewFrom[1])) {
//				ViewFrom[1] = Cube.get(0).dy + 0.2;
                ViewFrom[1] += 0.1
            } else {
//				ViewFrom[1] = Cube.get(0).y - 0.2;
                ViewFrom[1] -= 0.1
            }
        } else {
            condition = "NONE"
        }
    }

    @Suppress("unused")
    private fun lowerLimit() {
        if (ViewFrom[2] < 3.0) {
            ViewFrom[2] = 3.0
        }
    }

    /*
	 * Control7�������ꂽ���A�t�H�[�J�X���Ă���L���[�u����肵�č폜����
	 *�@�A���ŏ�����̂�h���ׁAdelete interval��݂��Ă���
	 */
    private fun deleteCube() {
        if (Control[7]) {
            if (System.currentTimeMillis() - LastCubeDeleteTime >= deleteInterval) {
                for (i in Cube.indices) {
                    for (j in Cube[i].Polys.indices) {
                        if (Cube[i].Polys[j]!!.DrawablePolygon == FocusPolygon) {
                            dCube = Cube[i].toString()
                            Cube[i].removeCube()
                            LastCubeDeleteTime = System.currentTimeMillis()
                            NumberOfDeleteCube++
                            condition = "CUBE DELETED : " + dCube
                            break
                        }
                    }
                }
            }
        }

        //�S�폜
        if (Control[9]) {
            for (i in Cube.indices) {
                Cube[i].removeCube()
                condition = "ALL DELETE"
            }
        }
    }

    //���������\�[�g
    private fun setOrder() {
        //���������i�[����z��
        val k = DoubleArray(DPolygons.size)
        //�\�[�g����������
        NewOrder = IntArray(DPolygons.size)
        for (i in DPolygons.indices) {
            k[i] = DPolygons[i].AverageDistance
            NewOrder[i] = i
        }

        /*���ϋ����̒Z�����Ƀ\�[�g����
		* (�o�u���\�[�g)
		* */
        var dtmp: Double
        var itmp: Int
        for (i in 0 until k.size - 1) {
            for (j in 0 until k.size - 1) {
                if (k[j] < k[j + 1]) {
                    dtmp = k[j]
                    itmp = NewOrder[j]
                    NewOrder[j] = NewOrder[j + 1]
                    k[j] = k[j + 1]
                    NewOrder[j + 1] = itmp
                    k[j + 1] = dtmp
                }
            }
        }
    }

    //�}�E�X���\��
    private fun invisibleMouse() {
        val toolkit = Toolkit.getDefaultToolkit()
        val cursorImage = BufferedImage(1, 1, BufferedImage.TRANSLUCENT)
        val invisibleCursor = toolkit.createCustomCursor(cursorImage, Point(0, 0), "InvisibleCursor")
        cursor = invisibleCursor
    }

    //�}�E�X�G�C����`��
    private fun drawMouseAim(g: Graphics) {
        g.color = Color.black
        g.drawLine((Main.ScreenSize.getWidth() / 2 - aimSight).toInt(), (Main.ScreenSize.getHeight() / 2).toInt(), (Main.ScreenSize.getWidth() / 2 + aimSight).toInt(), (Main.ScreenSize.getHeight() / 2).toInt())
        g.drawLine((Main.ScreenSize.getWidth() / 2).toInt(), (Main.ScreenSize.getHeight() / 2 - aimSight).toInt(), (Main.ScreenSize.getWidth() / 2).toInt(), (Main.ScreenSize.getHeight() / 2 + aimSight).toInt())
    }

    /*�`��X�V�̂��߂̃��\�b�h*/
    private fun SleepAndRefresh() {
        val timeSLU = (System.currentTimeMillis() - LastRefresh).toLong()
        Checks++
        if (Checks >= 15) {
            drawFPS = Checks / ((System.currentTimeMillis() - LastFPSCheck) / 1000.0)
            LastFPSCheck = System.currentTimeMillis().toDouble()
            Checks = 0.0
        }
        if (timeSLU < 1000.0 / MaxFPS) {
            try {
                Thread.sleep((1000.0 / MaxFPS - timeSLU).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        LastRefresh = System.currentTimeMillis().toDouble()

        //�`��X�V
        repaint()
    }

    //�L�[���͂𐧌�
    private fun KeyControl() {
        val ViewVector = Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2])
        var xMove = 0.0
        var yMove = 0.0
        var zMove = 0.0

        //(����)�P�ʃx�N�g�����擾
        val VerticalVector = Vector(0.0, 0.0, 1.0)
        //�����ɓ����x�N�g��
        val SideViewVector = ViewVector.CrossProduct(VerticalVector)
        //�����ۂ̎��Ԃ��擾
        val moveTime = System.currentTimeMillis()
        //�����X�s�[�h���v�Z
        if (moveTime - LastMoveTime > moveInterval) {
            //�O�Ɉړ�
            if (Control[0]) {
                xMove += ViewVector.x * cameraSpeed
                yMove += ViewVector.y * cameraSpeed
                zMove += ViewVector.z * cameraSpeed
            }

            //���Ɉړ�
            if (Control[2]) {
                xMove -= ViewVector.x * cameraSpeed
                yMove -= ViewVector.y * cameraSpeed
                zMove -= ViewVector.z * cameraSpeed
            }
            //���Ɉړ�
            if (Control[1]) {
                xMove += SideViewVector.x * cameraSpeed
                yMove += SideViewVector.y * cameraSpeed
                zMove += SideViewVector.z * cameraSpeed
            }
            //�E�Ɉړ�
            if (Control[3]) {
                xMove -= SideViewVector.x * cameraSpeed
                yMove -= SideViewVector.y * cameraSpeed
                zMove -= SideViewVector.z * cameraSpeed
            }
            LastMoveTime = System.currentTimeMillis()
        }


        //�ړ�����x�N�g��
        val MoveVector = Vector(xMove, yMove, zMove)
        val fx = MoveVector.x * MovementSpeed
        val fy = MoveVector.y * MovementSpeed
        val fz = MoveVector.z * MovementSpeed
        MoveTo(ViewFrom[0] + fx, ViewFrom[1] + fy, ViewFrom[2] + fz)

        //�J�������W�����Z�b�g
        if (Control[4]) {
            for (i in FViewFrom.indices) {
                ViewFrom[i] = FViewFrom[i]
                ViewTo[i] = FViewTo[i]
            }
            zoom = 1000.0
            condition = "View Reset"
        }

        //���̕�����z�ړ�
        if (Control[5]) {
            ViewFrom[2] += 0.4
            ViewTo[2] += 0.4
            condition = "FLY"
        }

        //���̕�����z�ړ�
        if (Control[6]) {
            //z < 0�̏ꍇz = 0�Ŏ~�߂�
            if (ViewFrom[2] > 0.0) {
                ViewFrom[2] -= 0.4
                ViewTo[2] -= 0.4
            } else {
                ViewFrom[2] -= 0.1
                ViewTo[2] -= 0.1
            }
        }

        //�����_���ȃL���[�u�𐶐�
        if (Control[8]) {
            val generate = System.currentTimeMillis()
            if (Math.abs(LastCubeGenerateTime - generate) > 200) {
                val rx = random.nextInt(255)
                val ry = random.nextInt(255)
                val rz = random.nextInt(255)
                val xyz = rx * 1000000 + ry * 1000 + rz
                colorBox[counter1] = xyz
                if (CoordinateCheck(xyz)) {
                    Cube.add(Cube(-rx / 25.0, ry / 25.0, rz / 25.0 + 2, 1.0, 1.0, 1.0, Color(rx, ry, rz)))
                    condition = "CUBE GENERATED : (x,y,z) = " + rx / 25 + "," + ry / 25 + "," + rz / 25
                }
                counter1++
                LastCubeGenerateTime = System.currentTimeMillis()
            }
        }
    }

    //�L���[�u���d�����Ă��Ȃ����`�F�b�N
    private fun CoordinateCheck(xyz: Int): Boolean {
        for (i in 0 until counter1) {
            if (colorBox[i] == xyz) {
                return false
            }
        }
        return true
    }

    //�J�����̍��W�����߂郁�\�b�h
    private fun MoveTo(x: Double, y: Double, z: Double) {
        ViewFrom[0] = x
        ViewFrom[1] = y
        ViewFrom[2] = z
        if (firstPersonMode) {
            ViewFrom[2] = Companion.height
        } else {
            if (ViewFrom[2] < 0.0) ViewFrom[2] = 0.0
        }

        //�`��X�V
        updateView()
    }

    //�}�E�X������Ă���|���S�������
    private fun setPolygonOver() {
        //��xnull�ƒ�`
        PolygonOver = null
        //�T��
        for (i in NewOrder.indices.reversed()) {
            if (DPolygons[NewOrder[i]].DrawablePolygon!!.MouseOver() && DPolygons[NewOrder[i]].draw && DPolygons[NewOrder[i]].DrawablePolygon!!.visible) {
                FocusPolygon = DPolygons[NewOrder[i]].DrawablePolygon
                PolygonOver = DPolygons[NewOrder[i]].DrawablePolygon
                break
            }
        }
    }

    private fun MouseMovement(NewX: Double, NewY: Double) {
        //�}�E�X��y��(�X�N���[���̒���)����ǂꂾ���͂Ȃꂽ���v��
        val difX = NewX - Main.ScreenSize.getWidth() / 2
        //�}�E�X��x��(�X�N���[���̒���)����ǂꂾ���͂Ȃꂽ���v��
        var difY = NewY - Main.ScreenSize.getHeight() / 2
        difY *= 6 - Math.abs(VerticalLook) * 5
        VerticalLook -= difY / VerticalRotationSpeed
        HorizontalLook += difX / HorizontalRotationSpeed

        //VerticalLook�̐�Βl��1.0�ȏ�ɂȂ�Ȃ��悤�ɂ���
        if (VerticalLook > 0.999) VerticalLook = 0.999
        if (VerticalLook < -0.999) VerticalLook = -0.999
        updateView()
    }

    //���_���A�b�v�f�[�g
    private fun updateView() {
        val r = Math.sqrt(1 - VerticalLook * VerticalLook)
        ViewTo[0] = ViewFrom[0] + r * Math.cos(HorizontalLook) // x���ړ�
        ViewTo[1] = ViewFrom[1] + r * Math.sin(HorizontalLook) // y���ړ�
        ViewTo[2] = ViewFrom[2] + VerticalLook // z���ړ�	
    }

    /*�L�[���͗pclass*/
    internal inner class KeyTyped : KeyAdapter() {
        //�L�[������������true
        override fun keyPressed(e: KeyEvent) {
            when (e.keyCode) {
                KeyEvent.VK_W -> Control[0] = true
                KeyEvent.VK_A -> Control[1] = true
                KeyEvent.VK_S -> Control[2] = true
                KeyEvent.VK_D -> Control[3] = true
                KeyEvent.VK_X -> Control[4] = true
                KeyEvent.VK_SPACE -> Control[5] = true
                KeyEvent.VK_SHIFT -> Control[6] = true
                KeyEvent.VK_BACK_SPACE -> Control[7] = true
                KeyEvent.VK_R -> Control[8] = true
                KeyEvent.VK_DELETE -> Control[9] = true
                KeyEvent.VK_O -> {
                    OutLines = !OutLines //���C���폜
                    if (OutLines) {
                        condition = "Show outer frame"
                    } else {
                        condition = "Hide outer frame"
                    }
                }

                KeyEvent.VK_ENTER -> Control[10] = true
                KeyEvent.VK_ESCAPE -> System.exit(0)
            }
        }

        //�L�[�𗣂�������false
        override fun keyReleased(e: KeyEvent) {
            when (e.keyCode) {
                KeyEvent.VK_W -> Control[0] = false
                KeyEvent.VK_A -> Control[1] = false
                KeyEvent.VK_S -> Control[2] = false
                KeyEvent.VK_D -> Control[3] = false
                KeyEvent.VK_X -> Control[4] = false
                KeyEvent.VK_SPACE -> {
                    Control[5] = false
                    condition = "NONE"
                }

                KeyEvent.VK_SHIFT -> Control[6] = false
                KeyEvent.VK_BACK_SPACE -> Control[7] = false
                KeyEvent.VK_R -> Control[8] = false
                KeyEvent.VK_DELETE -> Control[9] = false
                KeyEvent.VK_ENTER -> Control[10] = false
            }
        }
    }

    /*
	 * �}�E�X�̏��������N���X
	 * �}�E�X�̍��W�A�}�E�X�N���b�N���A�}�E�X�z�C�[���̏�������
	 */
    internal inner class AboutMouse : MouseListener, MouseMotionListener, MouseWheelListener {
        //�}�E�X�𒆉��Ɉړ������郁�\�b�h
        fun CenterMouse() {
            try {
                r = Robot()
                r!!.mouseMove(Main.ScreenSize.getWidth().toInt() / 2, Main.ScreenSize.getHeight().toInt() / 2)
            } catch (e: AWTException) {
                e.printStackTrace()
            }
        }

        //�}�E�X���h���b�O�����Ƃ��̐���		
        override fun mouseDragged(e: MouseEvent) {
            MouseMovement(e.x.toDouble(), e.y.toDouble())
            MouseX = e.x.toDouble()
            MouseY = e.y.toDouble()
            CenterMouse()
        }

        //�}�E�X�̓����𐧌�
        override fun mouseMoved(e: MouseEvent) {
            MouseMovement(e.x.toDouble(), e.y.toDouble())
            MouseX = e.x.toDouble()
            MouseY = e.y.toDouble()
            CenterMouse()
        }

        override fun mouseClicked(e: MouseEvent) {}
        override fun mouseEntered(e: MouseEvent) {}
        override fun mouseExited(e: MouseEvent) {}

        //�}�E�X�̃N���b�N�������Ƃ��̐���
        override fun mousePressed(e: MouseEvent) {
            //�E�N���b�N
            if (e.button == MouseEvent.BUTTON1) {
                if (PolygonOver != null) PolygonOver!!.seeThrough = false
            }

            //���N���b�N
            if (e.button == MouseEvent.BUTTON3) {
                if (PolygonOver != null) PolygonOver!!.seeThrough = true
            }
        }

        override fun mouseReleased(e: MouseEvent) {}

        //�}�E�X�z�C�[���������ǂ郁�\�b�h
        override fun mouseWheelMoved(e: MouseWheelEvent) {

            //�z�C�[�������ɂȂ�ƃY�[���A�E�g
            if (e.unitsToScroll > 0) {
                if (zoom > MinZoom) zoom -= (25 * e.unitsToScroll).toDouble()
                condition = "Zoom out"
            } else {
                //�z�C�[�������ɂȂ�ƃY�[���C��
                if (zoom < MaxZoom) zoom -= (25 * e.unitsToScroll).toDouble()
                condition = "Zoom in"
            }
        }
    }

    companion object {
        /**
         * serialVersionUID �́A���񉻂��ꂽ�I�u�W�F�N�g�̃o�[�W�����Ǘ��Ɏg�p����܂��B
         * serialVersionUID �𖾎��I�ɐ錾���Ȃ��ꍇ�AJVM �� Serializable �N���X�̂��܂��܂ȑ��ʂɊ�Â��Ď����I�Ɍv�Z���܂��B
         * �������A�قȂ�o�[�W�����̃N���X��ǂݍ���ł��܂����ƂŔ���������s���ȓ����h�����߂ɁAserialVersionUID ���N���X�ɐ錾���邱�Ƃ���������Ă��܂��B
         */
        private const val serialVersionUID = 1L

        /**
         * fpsMode��True�ɂ���ƈȉ��̂��Ƃ��N����܂�
         * * 1.�d�͂̒ǉ�(���V�ł��Ȃ��Ȃ�)
         * * 2.�J�����̍Œ�z���W��2�ƂȂ�
         */
        const val firstPersonMode = true
        private const val height = 4.0
        private const val debugMode = true
        private const val cameraSpeed = 0.002 //default => 0.25
        private const val moveInterval: Long = 10 // default => 0

        //class���i�[����ArrayList
        @JvmField
        var DPolygons = ArrayList<DPolygon>()
        @JvmField
        var Cube = ArrayList<Cube>()
        @JvmField
        var Pyramid = ArrayList<Pyramid>()

        //�J�E���g�p�z��
        private val colorBox = IntArray(256 * 256 * 256)
        private var counter1 = 0

        //���݃}�E�X������Ă���|���S���̏��
        var PolygonOver: Object? = null

        //�t�H�[�J�X���Ă���|���S�����
        private var FocusPolygon: Object? = null

        //�I�u�W�F�N�g���폜����C���^�[�o��(Mill Second)
        private const val deleteInterval: Long = 200
        private var LastMoveTime: Long = 0

        //�Ō�ɃL���[�u���폜��������
        private var LastCubeDeleteTime: Long = 0
        private var LastCubeGenerateTime: Long = 0
        private var dCube = "NONE"

        //��������
        var random = Random()

        //�����̃J�����ƃI�u�W�F�N�g�̈ʒu
        val FViewFrom = doubleArrayOf(-5.0, -5.0, 10.0)
        val FViewTo = doubleArrayOf(0.0, 0.0, 0.0)

        //�J�����̈ʒu
        var ViewFrom = FViewFrom.clone()

        //�I�u�W�F�N�g�̈ʒu
        var ViewTo = FViewTo.clone()

        //�Y�[����
        var zoom = 1000.0
        var MinZoom = 100.0
        var MaxZoom = 5000.0

        //�}�E�X�̍��W
        var MouseX = 0.0
        var MouseY = 0.0

        //���_�̓����X�s�[�h�𐧌�B�傫���قǒx������ Max 1.0
        var MovementSpeed = 0.5
        const val aimSight = 4.0 // �Z���^�[�N���X�̑傫��
        var OutLines = true

        //�t�H���g�T�C�Y
        const val FontSize = 15
        var condition = "NONE"
    }
}