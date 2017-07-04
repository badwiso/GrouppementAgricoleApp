package models

import javafx.beans.property.*

/**
 * Created by Monta on 20/06/2017.
 */
data class Client(val idProperty: IntegerProperty = SimpleIntegerProperty(-1),
                  val refProperty: IntegerProperty = SimpleIntegerProperty(0), // aka meter
                  val nameProperty: StringProperty = SimpleStringProperty(""),
                  val phoneProperty: StringProperty = SimpleStringProperty(""),
                  val cinProperty: StringProperty = SimpleStringProperty(""),
                  val regionProperty: ObjectProperty<Region> = SimpleObjectProperty(Region()),
                  val suspendedProperty: BooleanProperty = SimpleBooleanProperty(false)
){
    var id:Int
        get() = idProperty.get()
        set(value) = idProperty.set(value)
    var ref:Int
        get() = refProperty.get()
        set(value) = refProperty.set(value)
    var name:String
        get() = nameProperty.get()
        set(value) = nameProperty.set(value)
    var phone:String
        get() = phoneProperty.get()
        set(value) = phoneProperty.set(value)
    var cin:String
        get() = cinProperty.get()
        set(value) = cinProperty.set(value)
    var region:Region
        get() = regionProperty.get()
        set(value) = regionProperty.set(value)
    var suspended:Boolean
        get() = suspendedProperty.get()
        set(value) = suspendedProperty.set(value)
    constructor(id:Int,ref:Int,name:String,phone:String,cin:String,region:Region,suspended:Boolean):
            this(
                    SimpleIntegerProperty(id),
                    SimpleIntegerProperty(ref),
                    SimpleStringProperty(name),
                    SimpleStringProperty(phone),
                    SimpleStringProperty(cin),
                    SimpleObjectProperty(region),
                    SimpleBooleanProperty(suspended)
            )
    override fun equals(other: Any?): Boolean {
        if (other !is Client) return false
        if (idProperty != other.idProperty) return false
        return true
    }

    override fun hashCode(): Int {
        return idProperty.get()
    }
}

