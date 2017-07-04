package viewController.sessionAddEdit

import dao.Provider
import javafx.beans.property.DoubleProperty
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import javafx.util.converter.DoubleStringConverter
import javafx.util.converter.FormatStringConverter
import javafx.util.converter.NumberStringConverter
import utils.bindWithFormat
import utils.bounce
import viewController.Scenes
import viewController.Scenes._SESSION
import viewController.session.SessionController
import java.net.URL
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParsePosition
import java.util.*
import java.util.function.UnaryOperator
import java.util.regex.Pattern

/**
 * Created by Monta on 24/06/2017.
 */
class Controller :Initializable {
    @FXML lateinit var confirm:Button
    @FXML lateinit var fromDate:DatePicker
    @FXML lateinit var toDate:DatePicker
    @FXML lateinit var unitPrice:TextField
    @FXML lateinit var fixedTax:TextField
    @FXML lateinit var delayTax:TextField
    @FXML lateinit var otherTax:TextField
    override fun initialize(location: URL?, resources: ResourceBundle) {
        fromDate.valueProperty().bindBidirectional(_SESSION.fromDateProperty)
        toDate.valueProperty().bindBidirectional(_SESSION.toDateProperty)

        bindWithFormat(unitPrice, _SESSION.uPriceProperty)
        bindWithFormat(fixedTax, _SESSION.fixedTxProperty)
        bindWithFormat(otherTax, _SESSION.otherTxProperty)
        bindWithFormat(delayTax, _SESSION.delayTxProperty)
        confirm.onAction = EventHandler {
            fun isDoubleOrEmpty(textField: TextField,fullTest:Boolean=true):Boolean{
                return textField.text.isEmpty().or(try {
                    val v = textField.text.toDoubleOrNull()
                    v == null || fullTest && v <= 0.0
                }catch (e:Exception){
                    false
                })
            }
            when{
                (fromDate.value ==null).or(fromDate.value.isAfter(toDate.value)) -> bounce(fromDate, EventHandler { fromDate.show() })
                toDate.value ==null -> bounce(toDate, EventHandler { toDate.show() })
                isDoubleOrEmpty(unitPrice) -> bounce(unitPrice)
                isDoubleOrEmpty(fixedTax) -> bounce(fixedTax)
                isDoubleOrEmpty(delayTax) -> bounce(delayTax)
                isDoubleOrEmpty(otherTax,fullTest = false) -> bounce(otherTax)
                else->{
                    Provider.getDAO().mergeSession(_SESSION)
                    SessionController.update(true)
                    onCloseClick()
                }
            }
        }
    }
    @FXML fun onCloseClick(){
        Scenes.unloadSecondaryScene()
    }

}