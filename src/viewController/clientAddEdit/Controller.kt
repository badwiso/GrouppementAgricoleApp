package viewController.clientAddEdit

import dao.Provider
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.util.converter.NumberStringConverter
import models.Region
import utils.bindWithFormat
import utils.bounce
import viewController.Scenes
import java.net.URL
import java.util.*
import viewController.Scenes._CLIENT
import viewController.client.Controller

/**
 * Created by Monta on 22/06/2017.
 */
class Controller :Initializable {
    @FXML lateinit var meterField:TextField
    @FXML lateinit var cinField:TextField
    @FXML lateinit var nameField:TextField
    @FXML lateinit var phoneField:TextField
    @FXML lateinit var regionField:ChoiceBox<Region>
    @FXML lateinit var confirm:Button
    @FXML lateinit var cancel:Button
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        cinField.textProperty().bindBidirectional(_CLIENT.cinProperty)
        nameField.textProperty().bindBidirectional(_CLIENT.nameProperty)
        phoneField.textProperty().bindBidirectional(_CLIENT.phoneProperty)
        meterField.textProperty().bindBidirectional(_CLIENT.refProperty,NumberStringConverter())
        bindWithFormat(meterField, _CLIENT.refProperty)
        regionField.items = FXCollections.observableList(Provider.getDAO().getRegions())
        regionField.valueProperty().bindBidirectional(_CLIENT.regionProperty)
        confirm.onAction = EventHandler {
            when{
                meterField.text.isEmpty()-> bounce(meterField)
                cinField.text.isEmpty()-> bounce(cinField)
                nameField.text.isEmpty()-> bounce(nameField)
                regionField.selectionModel.isEmpty -> bounce(regionField, EventHandler { regionField.show() })
                phoneField.text.isEmpty()-> bounce(phoneField)
                else -> {
                    Provider.getDAO().mergeClient(_CLIENT)
                    onCloseClick()
                }
            }
        }
    }
    @FXML fun onCloseClick(){
        Scenes.unloadSecondaryScene()
        Controller.update(true)
    }
}