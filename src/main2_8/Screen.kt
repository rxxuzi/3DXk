package main2_8

import main2_8.Calculator.VectorInfo
import java.awt.*
import java.awt.event.*
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.JPanel

/*
* Main.javaのFrameにパネルとしてaddするclass
* */
public class Screen : JPanel() {
    private var NumberOfDeleteCube = 0

    //マウスを中心に置いておくために使用するサブクラス
    var r: Robot? = null

    //FPSの測定
    var drawFPS = 0.0
    var MaxFPS = 2000.0

    //	double SleepTime = 1000.0 / MaxFPS;
    var LastFPSCheck = 0.0
    var Checks = 0.0
    var LastRefresh = 0.0
    var VerticalLook = -0.9 //0.99 ~ -0.99まで、正の値の時は上向き。負の値の時は下向き
    var HorizontalLook = 0.0 // 任意の数値をとり、ラジアン単位で一周する
    var VerticalRotationSpeed = 1000.0 //垂直回転の速さ
    var HorizontalRotationSpeed = 500.0 //水平回転の速さ

    //配列DPolygonの描画する順番を保持する配列
    lateinit var NewOrder: IntArray

    //キー入力の情報を格納する配列
    var Control = BooleanArray(15)
    var Press = 10

    /*描画したい3Dポリゴンを定義するclass*/
    init {
        //キー入力
        addKeyListener(KeyTyped())
        isFocusable = true
        //マウス関連
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
			１．赤　R255
			２．橙　R255 G150
			３．黄　R255 G240
			４．緑　G135
			５．青　G145 B255
			６．紺　G100 B190
			７．紫　R145 B130
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

        /*地面の表示*/
        val d = 2 //幅
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

        //テキストからオブジェクトを製造
        if (!debugMode) {
            val t2o = TextToObject("./rsc/summon.txt")
        }
    }

    /*描画に関するメソッド*/
    public override fun paintComponent(g: Graphics) {
        //描画リセット
        g.clearRect(0, 0, Main.ScreenSize.getWidth().toInt(), Main.ScreenSize.getHeight().toInt())

        //カメラを動かす
        KeyControl()

        //フォーカスされたポリゴンを削除する
        deleteCube()

        //このカメラ位置で一般的なものをすべて計算します。
        VectorInfo()

        // ポリゴン情報を各自アップデート
        for (dPolygon in DPolygons) {
            dPolygon.updatePolygon()
        }

//		//ポリゴンを回転する
//		Cube.get(0).rotation+=0.01;
//		Cube.get(0).updatePoly();

//		Pyramid.get(0).rotation += 0.01;
//		Pyramid.get(0).updatePoly();
//
//		Pyramid.get(1).rotation -= 0.05;
//		Pyramid.get(1).updatePoly();

        //全てのポリゴンの距離をソート
        setOrder()

        //マウスが乗っているポリゴンを特定する
        setPolygonOver()

        //setOrder関数で設定された順序でポリゴンを描画
        for (j in NewOrder) {
            DPolygons[j].DrawablePolygon!!.drawPolygon(g)
        }

        //画面の中央にエイムをつける
        drawMouseAim(g)

        //フォントの設定
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
        g.drawString("Vertical angle   	 : " + VAngle.toInt() + "°", 10, 120)
        g.drawString("Number Of Polygons : " + DPolygons.size, 10, 135)
        g.drawString("Number Of Cubes    : " + Cube.size, 10, 150)
        g.drawString("Focus Polys ID : " + FocusPolygon.toString(), 10, 170)
        g.font = Font(Font.SANS_SERIF, Font.BOLD, 20)
        g.drawString("CONDITION: " + condition, 10, 190)
        if (Control[10]) {
            Press++
        }
        g.drawString(Press.toString() + "SIZE", 10, 220)

        //描画更新のインターバル
        SleepAndRefresh()
        if (firstPersonMode) {
            hitJudgment()
        }
    }

    private fun hitJudgment() {
        if (Cube[0].x < ViewFrom[0] && Cube[0].dx > ViewFrom[0] && Cube[0].y < ViewFrom[1] && Cube[0].dy > ViewFrom[1]) {
            condition = "in the BOX"
            //ViewFromに近い方の座標を採用
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
	 * Control7が押された時、フォーカスしているキューブを特定して削除する
	 *　連続で消えるのを防ぐ為、delete intervalを設けている
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

        //全削除
        if (Control[9]) {
            for (i in Cube.indices) {
                Cube[i].removeCube()
                condition = "ALL DELETE"
            }
        }
    }

    //距離情報をソート
    private fun setOrder() {
        //距離情報を格納する配列
        val k = DoubleArray(DPolygons.size)
        //ソートした情報を代入
        NewOrder = IntArray(DPolygons.size)
        for (i in DPolygons.indices) {
            k[i] = DPolygons[i].AverageDistance
            NewOrder[i] = i
        }

        /*平均距離の短い順にソートする
		* (バブルソート)
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

    //マウスを非表示
    private fun invisibleMouse() {
        val toolkit = Toolkit.getDefaultToolkit()
        val cursorImage = BufferedImage(1, 1, BufferedImage.TRANSLUCENT)
        val invisibleCursor = toolkit.createCustomCursor(cursorImage, Point(0, 0), "InvisibleCursor")
        cursor = invisibleCursor
    }

    //マウスエイムを描画
    private fun drawMouseAim(g: Graphics) {
        g.color = Color.black
        g.drawLine((Main.ScreenSize.getWidth() / 2 - aimSight).toInt(), (Main.ScreenSize.getHeight() / 2).toInt(), (Main.ScreenSize.getWidth() / 2 + aimSight).toInt(), (Main.ScreenSize.getHeight() / 2).toInt())
        g.drawLine((Main.ScreenSize.getWidth() / 2).toInt(), (Main.ScreenSize.getHeight() / 2 - aimSight).toInt(), (Main.ScreenSize.getWidth() / 2).toInt(), (Main.ScreenSize.getHeight() / 2 + aimSight).toInt())
    }

    /*描画更新のためのメソッド*/
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

        //描画更新
        repaint()
    }

    //キー入力を制御
    private fun KeyControl() {
        val ViewVector = Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2])
        var xMove = 0.0
        var yMove = 0.0
        var zMove = 0.0

        //(垂直)単位ベクトルを取得
        val VerticalVector = Vector(0.0, 0.0, 1.0)
        //水平に動くベクトル
        val SideViewVector = ViewVector.CrossProduct(VerticalVector)
        //動く際の時間を取得
        val moveTime = System.currentTimeMillis()
        //動くスピードを計算
        if (moveTime - LastMoveTime > moveInterval) {
            //前に移動
            if (Control[0]) {
                xMove += ViewVector.x * cameraSpeed
                yMove += ViewVector.y * cameraSpeed
                zMove += ViewVector.z * cameraSpeed
            }

            //後ろに移動
            if (Control[2]) {
                xMove -= ViewVector.x * cameraSpeed
                yMove -= ViewVector.y * cameraSpeed
                zMove -= ViewVector.z * cameraSpeed
            }
            //左に移動
            if (Control[1]) {
                xMove += SideViewVector.x * cameraSpeed
                yMove += SideViewVector.y * cameraSpeed
                zMove += SideViewVector.z * cameraSpeed
            }
            //右に移動
            if (Control[3]) {
                xMove -= SideViewVector.x * cameraSpeed
                yMove -= SideViewVector.y * cameraSpeed
                zMove -= SideViewVector.z * cameraSpeed
            }
            LastMoveTime = System.currentTimeMillis()
        }


        //移動するベクトル
        val MoveVector = Vector(xMove, yMove, zMove)
        val fx = MoveVector.x * MovementSpeed
        val fy = MoveVector.y * MovementSpeed
        val fz = MoveVector.z * MovementSpeed
        MoveTo(ViewFrom[0] + fx, ViewFrom[1] + fy, ViewFrom[2] + fz)

        //カメラ座標をリセット
        if (Control[4]) {
            for (i in FViewFrom.indices) {
                ViewFrom[i] = FViewFrom[i]
                ViewTo[i] = FViewTo[i]
            }
            zoom = 1000.0
            condition = "View Reset"
        }

        //正の方向にz移動
        if (Control[5]) {
            ViewFrom[2] += 0.4
            ViewTo[2] += 0.4
            condition = "FLY"
        }

        //負の方向にz移動
        if (Control[6]) {
            //z < 0の場合z = 0で止める
            if (ViewFrom[2] > 0.0) {
                ViewFrom[2] -= 0.4
                ViewTo[2] -= 0.4
            } else {
                ViewFrom[2] -= 0.1
                ViewTo[2] -= 0.1
            }
        }

        //ランダムなキューブを生成
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

    //キューブが重複していないかチェック
    private fun CoordinateCheck(xyz: Int): Boolean {
        for (i in 0 until counter1) {
            if (colorBox[i] == xyz) {
                return false
            }
        }
        return true
    }

    //カメラの座標を決めるメソッド
    private fun MoveTo(x: Double, y: Double, z: Double) {
        ViewFrom[0] = x
        ViewFrom[1] = y
        ViewFrom[2] = z
        if (firstPersonMode) {
            ViewFrom[2] = Companion.height
        } else {
            if (ViewFrom[2] < 0.0) ViewFrom[2] = 0.0
        }

        //描画更新
        updateView()
    }

    //マウスが乗っているポリゴンを特定
    private fun setPolygonOver() {
        //一度nullと定義
        PolygonOver = null
        //探索
        for (i in NewOrder.indices.reversed()) {
            if (DPolygons[NewOrder[i]].DrawablePolygon!!.MouseOver() && DPolygons[NewOrder[i]].draw && DPolygons[NewOrder[i]].DrawablePolygon!!.visible) {
                FocusPolygon = DPolygons[NewOrder[i]].DrawablePolygon
                PolygonOver = DPolygons[NewOrder[i]].DrawablePolygon
                break
            }
        }
    }

    private fun MouseMovement(NewX: Double, NewY: Double) {
        //マウスがy軸(スクリーンの中央)からどれだけはなれたか計測
        val difX = NewX - Main.ScreenSize.getWidth() / 2
        //マウスがx軸(スクリーンの中央)からどれだけはなれたか計測
        var difY = NewY - Main.ScreenSize.getHeight() / 2
        difY *= 6 - Math.abs(VerticalLook) * 5
        VerticalLook -= difY / VerticalRotationSpeed
        HorizontalLook += difX / HorizontalRotationSpeed

        //VerticalLookの絶対値が1.0以上にならないようにする
        if (VerticalLook > 0.999) VerticalLook = 0.999
        if (VerticalLook < -0.999) VerticalLook = -0.999
        updateView()
    }

    //視点をアップデート
    private fun updateView() {
        val r = Math.sqrt(1 - VerticalLook * VerticalLook)
        ViewTo[0] = ViewFrom[0] + r * Math.cos(HorizontalLook) // x軸移動
        ViewTo[1] = ViewFrom[1] + r * Math.sin(HorizontalLook) // y軸移動
        ViewTo[2] = ViewFrom[2] + VerticalLook // z軸移動	
    }

    /*キー入力用class*/
    internal inner class KeyTyped : KeyAdapter() {
        //キーを押した時にtrue
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
                    OutLines = !OutLines //ライン削除
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

        //キーを離した時にfalse
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
	 * マウスの情報を扱うクラス
	 * マウスの座標、マウスクリック情報、マウスホイールの情報を扱う
	 */
    internal inner class AboutMouse : MouseListener, MouseMotionListener, MouseWheelListener {
        //マウスを中央に移動させるメソッド
        fun CenterMouse() {
            try {
                r = Robot()
                r!!.mouseMove(Main.ScreenSize.getWidth().toInt() / 2, Main.ScreenSize.getHeight().toInt() / 2)
            } catch (e: AWTException) {
                e.printStackTrace()
            }
        }

        //マウスをドラッグしたときの制御		
        override fun mouseDragged(e: MouseEvent) {
            MouseMovement(e.x.toDouble(), e.y.toDouble())
            MouseX = e.x.toDouble()
            MouseY = e.y.toDouble()
            CenterMouse()
        }

        //マウスの動きを制御
        override fun mouseMoved(e: MouseEvent) {
            MouseMovement(e.x.toDouble(), e.y.toDouble())
            MouseX = e.x.toDouble()
            MouseY = e.y.toDouble()
            CenterMouse()
        }

        override fun mouseClicked(e: MouseEvent) {}
        override fun mouseEntered(e: MouseEvent) {}
        override fun mouseExited(e: MouseEvent) {}

        //マウスのクリックをしたときの制御
        override fun mousePressed(e: MouseEvent) {
            //右クリック
            if (e.button == MouseEvent.BUTTON1) {
                if (PolygonOver != null) PolygonOver!!.seeThrough = false
            }

            //左クリック
            if (e.button == MouseEvent.BUTTON3) {
                if (PolygonOver != null) PolygonOver!!.seeThrough = true
            }
        }

        override fun mouseReleased(e: MouseEvent) {}

        //マウスホイールをつかさどるメソッド
        override fun mouseWheelMoved(e: MouseWheelEvent) {

            //ホイールが正になるとズームアウト
            if (e.unitsToScroll > 0) {
                if (zoom > MinZoom) zoom -= (25 * e.unitsToScroll).toDouble()
                condition = "Zoom out"
            } else {
                //ホイールが負になるとズームイン
                if (zoom < MaxZoom) zoom -= (25 * e.unitsToScroll).toDouble()
                condition = "Zoom in"
            }
        }
    }

    companion object {
        /**
         * serialVersionUID は、直列化されたオブジェクトのバージョン管理に使用されます。
         * serialVersionUID を明示的に宣言しない場合、JVM は Serializable クラスのさまざまな側面に基づいて自動的に計算します。
         * ただし、異なるバージョンのクラスを読み込んでしまうことで発生しうる不正な動作を防ぐために、serialVersionUID をクラスに宣言することが推奨されています。
         */
        private const val serialVersionUID = 1L

        /**
         * fpsModeをTrueにすると以下のことが起こります
         * * 1.重力の追加(浮遊できなくなる)
         * * 2.カメラの最低z座標が2となる
         */
        const val firstPersonMode = true
        private const val height = 4.0
        private const val debugMode = true
        private const val cameraSpeed = 0.002 //default => 0.25
        private const val moveInterval: Long = 10 // default => 0

        //classを格納するArrayList
        @JvmField
        var DPolygons = ArrayList<DPolygon>()
        @JvmField
        var Cube = ArrayList<Cube>()
        @JvmField
        var Pyramid = ArrayList<Pyramid>()

        //カウント用配列
        private val colorBox = IntArray(256 * 256 * 256)
        private var counter1 = 0

        //現在マウスが乗っているポリゴンの情報
        var PolygonOver: Object? = null

        //フォーカスしているポリゴン情報
        private var FocusPolygon: Object? = null

        //オブジェクトを削除するインターバル(Mill Second)
        private const val deleteInterval: Long = 200
        private var LastMoveTime: Long = 0

        //最後にキューブを削除した時間
        private var LastCubeDeleteTime: Long = 0
        private var LastCubeGenerateTime: Long = 0
        private var dCube = "NONE"

        //乱数生成
        var random = Random()

        //初期のカメラとオブジェクトの位置
        val FViewFrom = doubleArrayOf(-5.0, -5.0, 10.0)
        val FViewTo = doubleArrayOf(0.0, 0.0, 0.0)

        //カメラの位置
        var ViewFrom = FViewFrom.clone()

        //オブジェクトの位置
        var ViewTo = FViewTo.clone()

        //ズーム率
        var zoom = 1000.0
        var MinZoom = 100.0
        var MaxZoom = 5000.0

        //マウスの座標
        var MouseX = 0.0
        var MouseY = 0.0

        //視点の動くスピードを制御。大きいほど遅く動く Max 1.0
        var MovementSpeed = 0.5
        const val aimSight = 4.0 // センタークロスの大きさ
        var OutLines = true

        //フォントサイズ
        const val FontSize = 15
        var condition = "NONE"
    }
}