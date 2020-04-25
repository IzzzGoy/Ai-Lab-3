package frameModel

import library.PtrType
import library.Type



abstract class Slot(name: String, type: Type, ptrType: PtrType, owner: Frame?, obj: Any?){

    var owner = owner
    val name = name
    var ptrType: PtrType = ptrType
    val type = type

    var obj: Any? = obj
        get(){
            return if (field == null) {
                field = ifNeeded()
                field
            } else {
                field
            }
        }
        set(value) {
            if (value != null) {
                field = value
                iffAdded()
            } else {
                field = null
                ifRemoved()
            }
        }
    fun delete(){
        if (owner != null) {
            if (owner?.owner != null) {
                for (s in owner?.owner?.slots!!) {
                    if (s.name == name) {
                        return
                    }
                }
            }
            obj = null
        }
    }
    abstract fun iffAdded()
    abstract fun ifRemoved()
    abstract fun ifNeeded(): Any?
}