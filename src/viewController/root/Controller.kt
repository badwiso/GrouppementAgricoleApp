package viewController.root

import assets.Assets
import javafx.animation.*
import javafx.beans.binding.When
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ListChangeListener
import javafx.fxml.*
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.util.Duration
import viewController.Scenes
import viewController.Scenes.CLIENTS
import viewController.Scenes.LANDING
import viewController.Scenes.CONSUMPTION
import viewController.Scenes.REGION
import viewController.Scenes.SESSION
import viewController.Scenes.STAGE
import viewController.Scenes.initNotificationPane
import java.net.URL
import java.util.*

/**
 * Created by Monta on 19/06/2017.
 */
class Controller :Initializable{
    private enum class Menus{
        LANDING,CONSUMPTION,PAYMENT, CLIENT,REGION,SESSION
    }
    private var activeMenu:Menus = Menus.LANDING

    @FXML lateinit var root:AnchorPane
    @FXML lateinit var bgMenu:Label
    @FXML lateinit var mainMenu:VBox
    @FXML lateinit var notificationPane:VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        // TODO need further testing
        notificationPane.visibleProperty().bind(When(SimpleBooleanProperty(notificationPane.children.isEmpty())).then(false).otherwise(true))
        initNotificationPane(notificationPane)
        Scenes.ROOT = root
        bgMenu.style = "-fx-background-image:url(\"${Assets::class.java.getResource("sidebar-1.jpg").toURI()}\")"
    }
    private fun setActiveMenu(node:Node,menu:Menus){
        with(node.styleClass){
            add("activemenuitem")
            remove("menuitem")
        }
        val res = mainMenu.children[activeMenu.ordinal]
        with(res.styleClass)
        {
            add("menuitem")
            remove("activemenuitem")
        }
        activeMenu = menu
    }
    @FXML fun landingClick(){
        if (activeMenu!=Menus.LANDING){
            Scenes.loadScene(LANDING)
            setActiveMenu(mainMenu.children[0],Menus.LANDING)
        }
    }
    @FXML fun consumptionClick(){
        if (activeMenu!=Menus.CONSUMPTION){
            Scenes.loadScene(CONSUMPTION)
            setActiveMenu(mainMenu.children[1],Menus.CONSUMPTION)
        }
    }
    @FXML fun paymentClick(){
        if (activeMenu!=Menus.PAYMENT){
            // TODO Scenes.loadScene(PAYMENT)
            setActiveMenu(mainMenu.children[2],Menus.PAYMENT)
        }
    }
    @FXML fun clientsClick(){
        if (activeMenu!=Menus.CLIENT){
            Scenes.loadScene(CLIENTS)
            setActiveMenu(mainMenu.children[3],Menus.CLIENT)
        }
    }
    @FXML fun regionsClick(){
        if (activeMenu!=Menus.REGION){
            Scenes.loadScene(REGION)
            setActiveMenu(mainMenu.children[4],Menus.REGION)
        }
    }
    @FXML fun sessionsClick(){
        if (activeMenu!=Menus.SESSION){
            Scenes.loadScene(SESSION)
            setActiveMenu(mainMenu.children[5],Menus.SESSION)
        }
    }
    @FXML fun onLogoutClick(){
        Scenes.loadRootScene()
    }
    @FXML fun onSettingClick(){}

    @FXML fun onMenuToggleClick(){
        AnchorPane.setLeftAnchor(root,if(AnchorPane.getLeftAnchor(root)==70.0)238.0 else 70.0)
    }

}