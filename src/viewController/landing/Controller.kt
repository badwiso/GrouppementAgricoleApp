package viewController.landing

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.layout.TilePane
import viewController.Scenes
import java.net.URL
import java.util.*

/**
 * Created by Monta on 19/06/2017.
 */
class Controller :Initializable{
    @FXML lateinit var tilePane:TilePane
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        with(Scenes._INFOCARD) {
            clear()
            add(Pair("Icons.client","orange"))
        }
    }

}