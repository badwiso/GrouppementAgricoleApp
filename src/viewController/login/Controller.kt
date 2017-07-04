package viewController.login

import javafx.collections.FXCollections
import javafx.fxml.*
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.VBox
import viewController.Scenes
import java.net.URL
import java.util.*

/**
 * Created by Monta on 19/06/2017.
 */
class Controller :Initializable{
    @FXML lateinit var username:TextField
    @FXML lateinit var password:PasswordField
    @FXML lateinit var confirm:Button
    @FXML lateinit var notificationPane:VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        Scenes.initNotificationPane(notificationPane)
        confirm.disableProperty().bind(username.textProperty().isEmpty.or(password.textProperty().isEmpty))
    }
    @FXML fun onClick(){
        Scenes.loadRootScene(login = false)
    }
}