package viewController

import javafx.animation.*
import javafx.beans.property.StringProperty
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.util.Duration
import models.Client
import models.Session
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Monta on 19/06/2017.
 */
object Scenes{

    val STYLE:String = Scenes::class.java.getResource("material-fx.css").toExternalForm()
    var notificationPane:VBox = VBox()
    lateinit var dialogResult:StringProperty
    lateinit var STAGE:Stage
    lateinit var ROOT:AnchorPane
    lateinit var DialogData:DialogBuilder
    lateinit var _CLIENT:Client
    lateinit var _SESSION:Session
    var _INFOCARD:ArrayList<Pair<String,String>> = ArrayList()
    var NOTIFICATION_DATA:Pair<String,NotificationType> = Pair("hello",NotificationType.INFO)
    enum class NotificationType{
        INFO,WARNING,DANGER,VALIDATION
    }
    var index:Int = 0

    val translateTransition:TranslateTransition = TranslateTransition()
    fun initNotificationPane(vBox: VBox){
        val tmp = Scenes.notificationPane.children
        Scenes.notificationPane = vBox
        vBox.children.addAll(tmp)
    }
    init {
        Font.loadFont(Scenes::class.java.getResource("maticon.ttf").toExternalForm(),10.0)
        with(translateTransition){
            duration = Duration.seconds(0.6)
            interpolator = Interpolator.EASE_OUT
            fromY = 600.0
            toY = 0.0
        }
    }
    fun load(url:URL):Pane{
        val tmp:Pane = FXMLLoader.load(url, RESOURCES_BUNDLE)
        tmp.stylesheets.add(STYLE)
        return tmp
    }
    fun loadNotification(message:String="",type: NotificationType =NotificationType.INFO){
        val ntf = NOTIFICATION
        NOTIFICATION_DATA = Pair(message,type)
        val translation = TranslateTransition()
        notificationPane.children.forEach {
            val translate = TranslateTransition()
            with(translate){
                fromY = -120.0
                toY = 0.0
                duration = Duration.seconds(1.0)
                node = it
                play()
            }
        }

        translation.fromX = 300.0
        translation.toX = 0.0
        translation.node = ntf
        translation.delay = Duration.seconds(0.8)
        ntf.translateX = 300.0
        notificationPane.children.add(0, ntf)
        translation.play()

    }
    fun unloadNotification(notification:AnchorPane?=null) {
        with(notificationPane.children){
            if(isNotEmpty())
                if(notification ==null) {
                    removeAt(size-1)
                } else {
                    remove(notification)
                }
        }
    }
    fun loadRootScene(login:Boolean=true){
        STAGE.scene.root = if(login) LOGIN else MAIN
        if (!login) {
            loadScene(LANDING)
        }
    }

    fun loadScene(parent:Pane) {
        with(ROOT){
            children.clear()
            children.add(parent)
            setAnchor(parent)
        }
    }

    fun loadSecondaryScene(parent:Pane){
        parent.id=(++index).toString()
        with(parent){
            ROOT.children.add(this)
            setAnchor(this)
            translateTransition.node = this
        }
        translateTransition.play()
    }
    fun unloadSecondaryScene(){
        var v = -1
        (0..ROOT.children.size-1).reversed().forEach {
            if(ROOT.children[it].id == index.toString()){
                v=it
                return@forEach
            }
        }
        if (v==-1) {
            return
        }
        with(translateTransition){
            node = ROOT.children[v]
            isAutoReverse = true
            cycleCount=2
            playFrom(Duration.seconds(0.6))
            onFinished = EventHandler {
                cycleCount = 1
                ROOT.children.removeAt(v)
                isAutoReverse = false
                onFinished = null
                index--
            }
        }
    }

    fun loadDialog(){
        with(DIALOG){
            (STAGE.scene.root as AnchorPane).children.add(this)
            setAnchor(this)
        }
    }

    fun unloadDialog(){
        with(STAGE.scene.root as AnchorPane){
            (0..children.size-1).reversed().forEach {
                if(children[it] is FlowPane){
                    children.removeAt(it)
                    return@forEach
                }
            }
        }
    }
    fun setAnchor(node: Node,left:Double=0.0,top:Double=0.0,right:Double=0.0,bottom:Double=0.0){
        with(node){
            if(left >= 0) AnchorPane.setLeftAnchor(this,left)
            if(top >= 0) AnchorPane.setTopAnchor(this,top)
            if(right >= 0) AnchorPane.setRightAnchor(this,right)
            if(bottom >= 0) AnchorPane.setBottomAnchor(this,bottom)
        }
    }

    val RESOURCES_BUNDLE = ResourceBundle.getBundle("strings")!!
    val LOGIN get() = load(viewController.login.Controller::class.java.getResource("view.fxml"))
    val MAIN get() = load(viewController.root.Controller::class.java.getResource("view.fxml"))
    val LANDING get() = load(viewController.landing.Controller::class.java.getResource("view.fxml"))
    val CLIENTS get() = load(viewController.client.Controller::class.java.getResource("view.fxml"))
    val CLIENTADDEDIT get() = load(viewController.clientAddEdit.Controller::class.java.getResource("view.fxml"))
    val REGION get() = load(viewController.region.Controller::class.java.getResource("view.fxml"))
    val CONSUMPTION get() = load(viewController.consumption.Controller::class.java.getResource("view.fxml"))
    val SESSION get() = load(viewController.session.SessionController::class.java.getResource("view.fxml"))
    val SESSIONADDEDIT get() = load(viewController.sessionAddEdit.Controller::class.java.getResource("view.fxml"))
    val DIALOG get() = load(viewController.dialog.Controller::class.java.getResource("view.fxml"))
    val NOTIFICATION get() = load(viewController.notification.Controller::class.java.getResource("view.fxml"))
}