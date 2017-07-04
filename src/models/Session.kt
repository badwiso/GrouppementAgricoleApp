package models

import javafx.beans.property.*
import viewController.Scenes.RESOURCES_BUNDLE
import java.time.LocalDate
import java.time.temporal.ChronoUnit.*
import java.util.*

/**
 * Created by Monta on 23/06/2017.
 */
class Session(val idProperty: IntegerProperty = SimpleIntegerProperty(-1),
              val fromDateProperty: ObjectProperty<LocalDate> = SimpleObjectProperty(LocalDate.now()),
              val toDateProperty: ObjectProperty<LocalDate> = SimpleObjectProperty(LocalDate.now()),
              val uPriceProperty: DoubleProperty = SimpleDoubleProperty(600.0),
              val fixedTxProperty: DoubleProperty = SimpleDoubleProperty(0.0),
              val delayTxProperty: DoubleProperty = SimpleDoubleProperty(0.0),
              val otherTxProperty: DoubleProperty = SimpleDoubleProperty(0.0)){
    var id:Int
        get() = idProperty.get()
        set(value) = idProperty.set(value)
    var fromDate:LocalDate
        get() = fromDateProperty.get()
        set(value) = fromDateProperty.set(value)
    var toDate:LocalDate
        get() = toDateProperty.get()
        set(value) = toDateProperty.set(value)
    var uPrice:Double
        get() = uPriceProperty.get()
        set(value) = uPriceProperty.set(value)
    var fixedTx:Double
        get() = fixedTxProperty.get()
        set(value) = fixedTxProperty.set(value)
    var delayTx:Double
        get() = delayTxProperty.get()
        set(value) = delayTxProperty.set(value)
    var otherTx:Double
        get() = otherTxProperty.get()
        set(value) = otherTxProperty.set(value)
    val timeBetween:String
        get() {
            val y = YEARS.between(fromDate,toDate)
            val m = MONTHS.between(fromDate,toDate)
            val d =DAYS.between(fromDate,toDate)
            return  when{
                y >  1L  -> "$y ${RESOURCES_BUNDLE.getString("time.year")}"
                y == 1L  -> "$y ${RESOURCES_BUNDLE.getString("time.years")}"
                m >  1L  -> "$m ${RESOURCES_BUNDLE.getString("time.months")}"
                m == 1L  -> "$m ${RESOURCES_BUNDLE.getString("time.month")}"
                d >  1L  -> "$d ${RESOURCES_BUNDLE.getString("time.days")}"
                d == 1L  -> "$d ${RESOURCES_BUNDLE.getString("time.day")}"
                else   -> "0"
            }
        }
    constructor(id:Int,fromDate:LocalDate,toDate:LocalDate,uPrice:Double,fixedTx:Double,delayTx:Double,otherTx:Double):
            this(
                    SimpleIntegerProperty(id),
                    SimpleObjectProperty(fromDate),
                    SimpleObjectProperty(toDate),
                    SimpleDoubleProperty(uPrice),
                    SimpleDoubleProperty(fixedTx),
                    SimpleDoubleProperty(delayTx),
                    SimpleDoubleProperty(otherTx)
            )
}