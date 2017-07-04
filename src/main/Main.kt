package main

import dao.DAOModel.Companion.connection
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.stage.Stage
import viewController.Scenes.LOGIN
import viewController.Scenes.STAGE

class Main : Application() {
    override fun start(primaryStage: Stage) {
        STAGE = primaryStage
        with(primaryStage){
            scene = Scene(LOGIN)
            centerOnScreen()
            isMaximized=true
            show()
            onCloseRequest = EventHandler { assert(connection != null,{connection!!.close()}) }
        }
    }
    companion object{
        @JvmStatic
        fun main(args:Array<String>){
            launch(Main::class.java)
        }
    }
}