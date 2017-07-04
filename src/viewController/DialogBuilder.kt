package viewController

import javafx.event.ActionEvent
import javafx.event.EventHandler
import viewController.Scenes.unloadDialog

/**
 * Created by Monta on 21/06/2017.
 */
class DialogBuilder private constructor() {
    var message:String=""
    var prompt:String=""
    var default:Pair<String,EventHandler<ActionEvent>> = Pair("button.confirm", EventHandler {  })
    var cancel:Pair<String,EventHandler<ActionEvent>> = Pair("button.cancel", EventHandler {unloadDialog()})
    fun setMessage(message:String):DialogBuilder{
        this.message = message
        return this
    }
    fun setPrompt(prompt:String):DialogBuilder{
        this.prompt = prompt
        return this
    }
    fun setDefault(name:String, actionEvent: EventHandler<ActionEvent>):DialogBuilder{
        return setButton(1,name,actionEvent)
    }
    fun setCancel(name:String, actionEvent: EventHandler<ActionEvent>):DialogBuilder{
        return setButton(2,name,actionEvent)
    }
    private fun setButton(button:Int,name:String, actionEvent: EventHandler<ActionEvent>):DialogBuilder{
        if (button==1) default = Pair(name,actionEvent) else cancel = Pair(name,actionEvent)
        return this
    }
    fun show(){
        Scenes.DialogData = this
        Scenes.loadDialog()
    }
    companion object{
        @JvmStatic fun create():DialogBuilder{
            return DialogBuilder()
        }
    }

}