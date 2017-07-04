package viewController.dialog

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.*
import javafx.scene.control.*
import viewController.Scenes.DialogData
import viewController.Scenes.dialogResult
import viewController.Scenes.unloadDialog
import java.net.URL
import java.util.*

/**
 * Created by Monta on 20/06/2017.
 */
class Controller :Initializable{
    @FXML lateinit var text:Label
    @FXML lateinit var textField:TextField
    @FXML lateinit var confirm: Button
    @FXML lateinit var cancel: Button
    override fun initialize(location: URL?, resources: ResourceBundle) {
        try {
            textField.textProperty().bindBidirectional(dialogResult)
        }catch (ignored:UninitializedPropertyAccessException){

        }
        when(DialogData.message.isNotEmpty()){
            true -> text.text = resources.getString(DialogData.message)
            false -> text.isVisible = false
        }
        when(DialogData.prompt.isNotEmpty()){
            true -> textField.promptText = resources.getString(DialogData.prompt)
            false -> textField.isVisible = false
        }
        fun initButton(button: Button,pair: Pair<String,EventHandler<ActionEvent>>) {
            with(button){
                onAction = pair.second
                when(pair.first.isNotEmpty()){
                    true -> text = resources.getString(pair.first)
                    false -> isVisible = false
                }
            }
        }
        confirm.disableProperty().bind(textField.textProperty().isEmpty.and(textField.visibleProperty()))
        initButton(confirm, DialogData.default)
        initButton(cancel, DialogData.cancel)
        Platform.runLater { if(textField.isVisible) textField.requestFocus() else confirm.requestFocus() }
    }
    @FXML fun closeRequest(){
        unloadDialog()
    }
}
