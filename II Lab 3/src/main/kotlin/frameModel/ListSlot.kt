package frameModel

import PersonKt
import library.PtrType
import library.Type

class ListSlot<T:Any>(name: String, val varType : ListSlotType, owner: Frame? = null) : Slot(name, Type.LIST,PtrType.S,owner,null)  {
    init {
        if (ptrType == PtrType.R){
            throw IllegalArgumentException("${type.name} cant be ranged")
        }
    }

    override fun iffAdded() {
        owner?.refreshChildFrames()
    }

    override fun ifRemoved() {
        owner?.removeSlot(name)
    }

    override fun ifNeeded(): Any? {
        return ArrayList<T>()
    }

    //fun copyList

    fun addElement( e : Any){
        when(varType){
            ListSlotType.PERSON -> (obj as ArrayList<PersonKt>).add((e as PersonKt))
            ListSlotType.INT -> (obj as ArrayList<Int>).add((e as Int))
            ListSlotType.DOUBLE -> (obj as ArrayList<Double>).add((e as Double))
            ListSlotType.TEXT -> (obj as ArrayList<String>).add((e as String))
        }
        iffAdded()
    }

    fun removeElement(e:Any){
        when(e){
            is PersonKt ->(obj as ArrayList<PersonKt>).remove(e)
            is Int ->(obj as ArrayList<Int>).remove(e)
            is Double ->(obj as ArrayList<Double>).remove(e)
            else -> (obj as ArrayList<String>).remove((e as String))
        }
    }

    fun getList() : List<T>{return (obj as ArrayList<T>).toList()}
    fun checkType(varType: ListSlotType): Boolean {
        return this.varType == varType
    }
}

enum class ListSlotType{
    PERSON,INT,DOUBLE,TEXT
}