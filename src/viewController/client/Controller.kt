package viewController.client

import dao.Provider
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.ChoiceBoxTableCell
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.util.converter.IntegerStringConverter
import javafx.util.converter.NumberStringConverter
import models.Client
import models.Region
import utils.hBoxResizePolicy
import utils.runLater
import viewController.DialogBuilder
import viewController.Scenes
import viewController.Scenes._CLIENT
import viewController.Scenes.CLIENTADDEDIT
import viewController.Scenes.REGION
import viewController.Scenes.unloadDialog
import java.net.URL
import java.util.*


/**
 * Created by Monta on 20/06/2017.
 */
class Controller :Initializable{
    var isFastEdit = SimpleBooleanProperty(false)
    @FXML lateinit var edit:Label
    @FXML lateinit var delete:Label
    @FXML lateinit var searchField:TextField
    @FXML lateinit var hbox:HBox
    @FXML lateinit var table:TableView<Client>
    @FXML lateinit var ref:TableColumn<Client,Int>
    @FXML lateinit var name:TableColumn<Client,String>
    @FXML lateinit var cin:TableColumn<Client,String>
    @FXML lateinit var phone:TableColumn<Client,String>
    @FXML lateinit var region:TableColumn<Client,Region>
    override fun initialize(location: URL?, resources: ResourceBundle) {
        ref.cellValueFactory = PropertyValueFactory("ref")
        name.cellValueFactory = PropertyValueFactory("name")
        phone.cellValueFactory = PropertyValueFactory("phone")
        cin.cellValueFactory = PropertyValueFactory("cin")
        try {ref.cellFactory = TextFieldTableCell.forTableColumn(IntegerStringConverter())}catch (ignored:Exception){}
        name.cellFactory = TextFieldTableCell.forTableColumn()
        phone.cellFactory = TextFieldTableCell.forTableColumn()
        cin.cellFactory = TextFieldTableCell.forTableColumn()
        region.cellFactory = ChoiceBoxTableCell.forTableColumn(FXCollections.observableList(Provider.getDAO().getRegions()))
        // TODO("Toggle fast edit option")
        hBoxResizePolicy(hbox)
        //Note to Self: update Listner Goes here
        updateListener.addListener { _, _, newValue -> if (newValue){ updateTable();update(false)}}
        region.cellValueFactory = PropertyValueFactory("region")
        with(table){
            nodeOrientationProperty().bind(table.parent.nodeOrientationProperty())
            placeholder = Label(resources.getString("noData"))
            selectionModel.selectedIndexProperty().addListener { _, _, newValue ->run{
                delete.isDisable =  newValue.toInt()==-1
                edit.isDisable = newValue.toInt()==-1
            }  }
            updateTable()
        }
    }
    @FXML fun onKeyPressed(event:KeyEvent){
        with(searchField){
            when(event.code){
                in KeyCode.A..KeyCode.Z -> text += event.text.toLowerCase()
                in KeyCode.NUMPAD0..KeyCode.NUMPAD9 -> text += event.text
                in KeyCode.DIGIT0..KeyCode.DIGIT9 -> text += event.text
            }
        }
        when{
            event.code == KeyCode.DELETE && !table.selectionModel.isEmpty -> onDeleteClick()
            event.code == KeyCode.ENTER && !table.selectionModel.isEmpty -> onEditClick()
            event.code == KeyCode.ENTER && table.selectionModel.isEmpty -> onAddClick()
        }
    }
    @FXML fun onAddClick(){
        _CLIENT = Client()
        Scenes.loadSecondaryScene(CLIENTADDEDIT)
    }
    @FXML fun onEditClick(){
        _CLIENT = table.selectionModel.selectedItem
        Scenes.loadSecondaryScene(CLIENTADDEDIT)
    }
    @FXML fun onDeleteClick(){
        DialogBuilder.create().setMessage("client.delete").
                setDefault("button.delete", EventHandler { Provider.getDAO().deleteClient(table.selectionModel.selectedItem);updateTable();unloadDialog()}).
                show()
    }
    fun updateTable(){
        runLater(with(table.items){
            clear()
            addAll(Provider.getDAO().getClients())
        })
    }
    companion object{
        var updateListener:BooleanProperty = SimpleBooleanProperty(false)
        @JvmStatic fun update(value:Boolean){
            updateListener.set(value)
        }
    }
}