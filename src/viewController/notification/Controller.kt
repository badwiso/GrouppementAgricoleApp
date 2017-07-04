package viewController.notification

import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.util.Duration
import viewController.Scenes
import viewController.Scenes.NOTIFICATION_DATA
import java.net.URL
import java.util.*

/**
 * Created by Monta on 25/06/2017.
 */
class Controller :Initializable {
    val fadeTransition:FadeTransition = FadeTransition()
    @FXML lateinit var notification: AnchorPane
    @FXML lateinit var message:Label
    @FXML lateinit var graphic:Label
    override fun initialize(location: URL?, resources: ResourceBundle) {
        with(NOTIFICATION_DATA){
            notification.styleClass += "ntf-${second.toString().toLowerCase()}"
            message.text = first
            graphic.text = resources.getString("Icons.${second.toString().toLowerCase()}")
        }

        with(fadeTransition){
            node = notification
            fromValue = 1.0
            toValue = 0.0
            delay = Duration.seconds(6.8)
            duration = Duration.seconds(2.0)
            onFinished = EventHandler {
                Scenes.unloadNotification()
                onFinished = null
            }
            play()
        }
    }
    @FXML fun onCloseClick(){
        Scenes.unloadNotification(notification)
    }
}