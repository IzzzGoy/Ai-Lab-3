package frameModel

import library.DataBase
import library.PtrType
import library.Type
import tornadofx.isDouble
import tornadofx.isInt

class OneFieldSlot(name: String, ptrType: PtrType, type: Type, owner: Frame? = null, obj: Any? = null) : Slot(name, type,ptrType,owner,obj) {
    init {
        if (obj != null){
            if (!checkType(obj)){
                throw IllegalArgumentException("Slot type != type in constructor")
            }
        }
        if (ptrType == PtrType.R && (type != Type.INTEGER)){
            throw IllegalArgumentException("${type.name} cant be ranged")
        }
    }
    override fun iffAdded() {
        if (owner != null && checkType(obj as String)){
            if (owner!!.owner != null){
                ptrType = if (owner!!.owner?.getSlotByName(name) != null){
                    if (owner!!.owner?.getSlotByName(name)?.ptrType == PtrType.S || owner!!.owner?.getSlotByName(name)?.ptrType == PtrType.O){
                        owner!!.owner?.getSlotByName(name)?.ptrType!!
                    } else {
                        PtrType.U
                    }
                } else {
                    PtrType.U
                }
            }
        }
    }

    override fun ifRemoved() {
        owner?.removeSlot(name)
    }

    override fun ifNeeded(): Any? {
        if (owner?.owner != null){
            when(owner!!.owner?.getSlotByName(name)?.ptrType) {
                PtrType.U -> {
                    if (DataBase.instance?.get(name) == null) {
                        throw IllegalArgumentException("No value in storage (PtrType U)")
                    } else {
                        if (checkType(DataBase.instance?.get(name)!!)) {
                            return DataBase.instance?.get(name)
                        } else {
                            throw IllegalStateException("Slot type != type in storage")
                        }
                    }
                }
                PtrType.S -> {
                    if (owner!!.owner?.getSlotByName(name) == null) {
                        throw IllegalArgumentException("No value in root Frame (PtrType S)")
                    } else {
                        return owner!!.owner?.getSlotByName(name)!!.obj
                    }
                }
                PtrType.O -> {
                    return when {
                        DataBase.instance?.get(name) != null -> {
                            if (checkType(DataBase.instance?.get(name)!!)) {
                                DataBase.instance?.get(name)
                            } else {
                                throw IllegalStateException("Slot type != type in storage")
                            }
                        }
                        owner!!.owner?.getSlotByName(name) == null -> {
                            throw IllegalArgumentException("No value in root Frame and storage (PtrType O)")
                        }
                        else -> {
                            owner!!.owner?.getSlotByName(name)!!.obj
                        }
                    }
                }
                PtrType.R -> {
                    if (ptrType == PtrType.R && (type != Type.INTEGER)){
                        throw IllegalArgumentException("${type.name} cant be ranged")
                    } else {
                        if (DataBase.instance?.get(name) != null) {
                            try {
                                if (DataBase.instance?.get(name)?.isInt()?.and(DataBase.instance?.get(name)?.toInt() in owner!!.owner?.getSlotByName(name)?.obj as IntRange)!!) {
                                    return DataBase.instance?.get(name)?.toInt()
                                } else {
                                    throw IllegalArgumentException("Value is`nt in Range")
                                }
                            } catch (e : Exception){
                                e.printStackTrace()
                            }
                        } else {
                            throw IllegalArgumentException("No value in storage for Range")
                        }

                    }
                }
                else -> {
                    return if (DataBase.instance?.get(name) == null){
                        null
                    } else {
                        DataBase.instance?.get(name)
                    }
                }
            }
        }
        return if (DataBase.instance?.get(name) == null){
            null
        } else {
            DataBase.instance?.get(name)
        }
    }

    private fun checkType(candidate: Any):Boolean{
        when(type){
            Type.INTEGER -> {
                try {candidate as? Int} catch (e:Exception){
                        return candidate as? IntRange != null
                    }
                return true
                }
            Type.REAL -> {
                return (candidate as String).isDouble()
            }
            Type.BOOL -> {
                try {
                    (candidate as String).toBoolean()
                } catch (e : Exception){
                    return false
                }
                return true
            }
            Type.TEXT -> { return true }
            else -> return false
        }
    }
}