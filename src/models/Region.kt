package models

import javafx.beans.property.*

/**
 * Created by Monta on 19/06/2017.
 */
open class Region( val idProperty:IntegerProperty= SimpleIntegerProperty(-1), val nameProperty:StringProperty= SimpleStringProperty("")){
    var id:Int
        get() = idProperty.get()
        set(value) = idProperty.set(value)
    var name:String
        get() = nameProperty.get()
        set(value) = nameProperty.set(name)

    constructor(idProperty: Int, nameProperty: String?) : this(SimpleIntegerProperty(idProperty),SimpleStringProperty(nameProperty))

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Region) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}

class RegionWrapper(idProperty: IntegerProperty, nameProperty: StringProperty,val countProperty:IntegerProperty=SimpleIntegerProperty(0),val percentProperty: DoubleProperty=SimpleDoubleProperty(0.0)) : Region(idProperty, nameProperty) {
    var count:Int
        get() = countProperty.get()
        set(value) = countProperty.set(value)
    var percent:Double
        get() = percentProperty.get()
        set(value) = percentProperty.set(value)
    constructor(id:Int, name: String, count: Int,percent:Double) : this(SimpleIntegerProperty(id),SimpleStringProperty(name),SimpleIntegerProperty(count),SimpleDoubleProperty(percent))

}