package viewController.region

import dao.Provider
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.util.converter.PercentageStringConverter
import models.Region
import models.RegionWrapper
import utils.hBoxResizePolicy
import utils.runLater
import viewController.DialogBuilder
import viewController.Scenes
import viewController.Scenes.ROOT
import viewController.Scenes.dialogResult
import viewController.Scenes.unloadDialog
import java.net.URL
import java.util.*

/**
 * Created by Monta on 20/06/2017.
 */
class Controller :Initializable {

    @FXML lateinit var edit:Label
    @FXML lateinit var delete:Label
    @FXML lateinit var hBox:HBox
    @FXML lateinit var table:TableView<RegionWrapper>

    override fun initialize(location: URL?, resources: ResourceBundle) {
        val name:TableColumn<RegionWrapper,String> = TableColumn(resources.getString("region"))
        val count:TableColumn<RegionWrapper,Int> = TableColumn(resources.getString("client.count"))
        val percent:TableColumn<RegionWrapper,Number> = TableColumn(resources.getString("client.percent"))
        name.cellValueFactory = PropertyValueFactory("name")
        name.cellFactory = TextFieldTableCell.forTableColumn()
        name.isEditable = true
        name.onEditCommit = EventHandler {
            Provider.getDAO().mergeRegion(Region(table.selectionModel.selectedItem.id, it.newValue))
            updateTable()
        }
        count.cellValueFactory = PropertyValueFactory("count")
        percent.cellValueFactory = PropertyValueFactory("percent")
        percent.cellFactory = TextFieldTableCell.forTableColumn(PercentageStringConverter())
        hBoxResizePolicy(hBox)
        with(table) {
            columns.addAll(name,count,percent)
            selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
                run {
                    delete.isDisable = newValue.toInt() == -1
                    edit.isDisable = newValue.toInt() == -1
                }
            }
            nodeOrientationProperty().bind(ROOT.nodeOrientationProperty())
            placeholder = Label(resources.getString("noData"))
        }
        updateTable()
    }

    @FXML fun onAddClick(){
        dialogResult = SimpleStringProperty()
        DialogBuilder.create()
                .setMessage("region.add")
                .setPrompt("region")
                .setDefault("button.add", EventHandler {Provider.getDAO().mergeRegion(Region(-1, dialogResult.get()));updateTable();Scenes.unloadDialog()})
                .show()
    }

    private fun updateTable() {
        runLater(with(table.items){
            clear()
            addAll(Provider.getDAO().getRegionsWrapper())
        })
    }

    @FXML fun onEditClick(){
        dialogResult = SimpleStringProperty(table.selectionModel.selectedItem.name)
        DialogBuilder.create()
                .setMessage("region.edit")
                .setPrompt("region")
                .setDefault("button.save", EventHandler {Provider.getDAO().mergeRegion(Region(table.selectionModel.selectedItem.id, dialogResult.get()));updateTable();Scenes.unloadDialog()})
                .setCancel("button.cancel", EventHandler { runLater(table.requestFocus());unloadDialog()})
                .show()
    }
    @FXML fun onDeleteClick(){
        DialogBuilder.create()
                .setMessage("region.delete")
                .setDefault("button.delete", EventHandler {Provider.getDAO().deleteRegion(table.selectionModel.selectedItem);updateTable();Scenes.unloadDialog()})
                .setCancel("button.cancel",EventHandler { runLater(table.requestFocus());unloadDialog() })
                .show()
    }

    @FXML fun onKeyPressed(event:KeyEvent){
        when{
            event.code == KeyCode.DELETE && !table.selectionModel.isEmpty -> {onDeleteClick();println("delete")}
            event.code == KeyCode.ENTER && !table.selectionModel.isEmpty -> {onEditClick();println("edit")}
            event.code == KeyCode.ENTER && table.selectionModel.isEmpty -> {onAddClick();println("add")}
        }
    }
}
