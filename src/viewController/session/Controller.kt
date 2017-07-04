package viewController.session

import dao.Provider
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.StringConverter
import models.Session
import utils.runLater
import viewController.Scenes
import viewController.Scenes._SESSION
import java.net.URL
import java.time.LocalDate
import java.time.Month
import java.util.*
import java.time.temporal.ChronoUnit.MONTHS

/**
 * Created by Monta on 24/06/2017.
 */
class SessionController:Initializable {
    @FXML lateinit var table:TableView<Session>
    @FXML lateinit var edit:Label
    @FXML lateinit var delete:Label
    override fun initialize(location: URL?, resources: ResourceBundle) {
        updateListener.addListener { _, _, newValue -> if (newValue){ updateTable();update(false)}}
        val fromDate = TableColumn<Session,LocalDate>(resources.getString("session.from"))
        val toDate = TableColumn<Session,LocalDate>(resources.getString("session.to"))
        val unitPrice = TableColumn<Session,Double>(resources.getString("session.uprice"))
        val fixedTx = TableColumn<Session,LocalDate>(resources.getString("session.ftx"))
        val delayTx = TableColumn<Session,Double>(resources.getString("session.dtx"))
        val otherTx = TableColumn<Session,Double>(resources.getString("session.otx"))
        val span = TableColumn<Session,LocalDate>(resources.getString("session.span"))
        fromDate.cellValueFactory = PropertyValueFactory("fromDate")
        toDate.cellValueFactory = PropertyValueFactory("toDate")
        span.cellValueFactory = PropertyValueFactory("timeBetween")

        unitPrice.cellValueFactory = PropertyValueFactory("uPrice")
        fixedTx.cellValueFactory = PropertyValueFactory("fixedTx")
        delayTx.cellValueFactory = PropertyValueFactory("delayTx")
        otherTx.cellValueFactory = PropertyValueFactory("otherTx")
        val session = TableColumn<Session,Any>(resources.getString("session"))
        session.columns.addAll(fromDate,toDate,span)
        with(table){
            nodeOrientationProperty().bind(table.parent.nodeOrientationProperty())
            columns.addAll(session,unitPrice,fixedTx,delayTx,otherTx)
            placeholder = Label(resources.getString("noData"))
            selectionModel.selectedIndexProperty().addListener { _, _, newValue ->run{
                delete.isDisable =  newValue.toInt()==-1
                edit.isDisable = newValue.toInt()==-1
            }  }
            updateTable()
        }
        runLater(table.requestFocus())
    }
    @FXML fun onAddClick(){
        _SESSION = Session()
        Scenes.loadSecondaryScene(Scenes.SESSIONADDEDIT)
    }
    @FXML fun onEditClick(){
        _SESSION = table.selectionModel.selectedItem
        Scenes.loadSecondaryScene(Scenes.SESSIONADDEDIT)
    }
    @FXML fun onDeleteClick(){}
    @FXML fun onKeyPressed(){}
    fun updateTable(){
        runLater(with(table.items){
            clear()
            addAll(Provider.getDAO().getSessions())
        })
    }
    companion object{
        var updateListener: BooleanProperty = SimpleBooleanProperty(false)
        @JvmStatic fun update(value:Boolean){
            updateListener.set(value)
        }
    }

}
class LocalDateStringConverter(date:LocalDate): StringConverter<LocalDate>(){
    private var date:LocalDate = date
    override fun toString(date: LocalDate?): String {
        return "${MONTHS.between(date,this.date)}"
    }
    override fun fromString(string: String?): LocalDate? {
        return null
    }

}