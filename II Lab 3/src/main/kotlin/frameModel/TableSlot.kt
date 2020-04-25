package frameModel

import library.PtrType
import library.Type

class TableSlot(name: String, ptrType: PtrType, owner: Frame? = null, obj:Any? = null) : Slot(name, Type.BOOL,ptrType,owner,obj) {
    init {
        if (ptrType == PtrType.R && (type != Type.REAL || type != Type.INTEGER)){
            throw IllegalArgumentException("${type.name} cant be ranged")
        }
    }
    override fun iffAdded() {

    }

    override fun ifRemoved() {
        owner?.removeSlot(name)
    }

    fun getMap():HashMap<String,String> {return obj as HashMap<String, String>}

    override fun ifNeeded(): Any? {
        return HashMap<String,String>()
    }


}