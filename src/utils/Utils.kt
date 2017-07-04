package utils

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.beans.binding.When
import javafx.beans.property.DoubleProperty
import javafx.beans.property.Property
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.layout.HBox
import javafx.util.Duration
import javafx.util.converter.NumberStringConverter
import viewController.Scenes
import java.util.function.UnaryOperator
import java.util.regex.Pattern

/**
 * Created by Monta on 24/06/2017.
 */

fun bindWithFormat(textField: TextField, doubleProperty: Property<Number>, pattern: String = "\\d*"){
    val number = Pattern.compile(pattern)
    val formatter = TextFormatter<Number>(NumberStringConverter(),doubleProperty.value, UnaryOperator { if(number.matcher(it.controlNewText).matches()) it else null })
    textField.textFormatter = formatter
    formatter.valueProperty().bindBidirectional(doubleProperty)
}

fun hBoxResizePolicy(hBox: HBox) {
    hBox.children.forEach {
        if(it is Label){
            it.contentDisplayProperty().bind(When(Scenes.ROOT.widthProperty().lessThan(500.0)).then(ContentDisplay.GRAPHIC_ONLY).otherwise(ContentDisplay.TOP))
        }
    }
    hBox.spacingProperty().bind(When(Scenes.ROOT.widthProperty().lessThan(600.0)).then(Scenes.ROOT.widthProperty().divide(40.0)).otherwise(60.0))
}
fun bounce(node: Node,eventHandler: EventHandler<ActionEvent> = EventHandler { node.requestFocus() }){
    val timeline = Timeline()
    var range = 0
    fun nextVal():Double{
        range += 1
        return range*100.0
    }
    timeline.keyFrames.addAll(
            KeyFrame(Duration.ZERO, KeyValue(node.translateYProperty(),-30)),
            KeyFrame(Duration.millis(nextVal()), KeyValue(node.translateYProperty(),0)),
            KeyFrame(Duration.millis(nextVal()), KeyValue(node.translateYProperty(),-20)),
            KeyFrame(Duration.millis(nextVal()), KeyValue(node.translateYProperty(),0)),
            KeyFrame(Duration.millis(nextVal()), KeyValue(node.translateYProperty(),-10)),
            KeyFrame(Duration.millis(nextVal()), KeyValue(node.translateYProperty(),0)),
            KeyFrame(Duration.millis(nextVal()), KeyValue(node.translateYProperty(),-5)),
            KeyFrame(Duration.millis(nextVal()), KeyValue(node.translateYProperty(),0))
    )
    timeline.onFinished = eventHandler
    timeline.play()
}
fun runLater(runnable: Any){
    Platform.runLater { runnable}
}