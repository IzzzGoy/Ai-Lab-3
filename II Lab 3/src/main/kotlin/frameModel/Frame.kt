package frameModel

import library.DataBase
import library.PtrType
import library.Type

class Frame(name: String, owner: Frame? = null) : Slot(name,Type.FRAME,PtrType.U, owner,null) {
    val slots = ArrayList<Slot>()
    private val frames = ArrayList<Frame>()


    init {refresh() }

    private fun refresh(){obj = owner}

    override fun iffAdded() {
        if (owner != null){
            for (e in owner!!.slots){
                if (checkSlot(e.name))copySlot(e)?.let { slots.add(it) }
            }
        }
    }

    fun refreshChildFrames(){
        for (f in frames){
            /*for (s in slots){
                f.addSlot(s)
            }*/
            f.iffAdded()
            f.refreshChildFrames()
        }
    }

    override fun ifRemoved() {}

    override fun ifNeeded(): Any? { return this }

    fun getChildFrame(i:Int):Frame { return frames[i] }

    fun removeChildFrame(frame: Frame){
        DataBase.frameNames?.remove(frame.name)
        frames.remove(frame)
    }
    fun clearChildFrames(){
        for (f in frames){
            DataBase.frameNames!!.remove(f.name)
        }
        frames.clear()}

    fun execute(lispName: String):Any?{
        var r : Any? = null
        for (f in frames){
            for (l in f.slots){
                if (l.name == lispName){
                    r =  (l as LispSlot).execute()
                    if (r != null){return r}
                }
            }
        }
        return null
    }

    fun getCloneFrames(): ArrayList<Frame> {return frames.clone() as ArrayList<Frame>}

    fun getSlotByName(slotName: String):Slot?{
        for (slot in slots){
            if (slot.name == slotName){
                return slot
            }
        }
        return null
    }
    fun addSlot(slot: Slot){
        if (checkSlot(slot.name)){
            slot.owner = this
            slots.add(slot)
            slot.obj
        }
    }

    fun removeSlot(slotName: String){
        slots.removeIf { s: Slot -> s.name == slotName }
        for(f in frames){
            for (s in f.slots){
                if (s.name  == slotName){
                    s.delete()
                    break
                }
            }
        }
    }

    fun addFrame(frameName: String){
        if (checkSlot(frameName) && !DataBase.frameNames?.contains(frameName)!!){
            DataBase.frameNames!!.add(frameName)
            frames.add(Frame(frameName,this))
        }
    }

    fun childFramesCount():Int {return frames.size}

    private fun copySlot(slot: Slot) :Slot?{
        when (slot.type){
            Type.INTEGER,Type.REAL,Type.BOOL,Type.TEXT ->{
                when (slot.ptrType) {
                    PtrType.U -> {
                        return OneFieldSlot(slot.name,slot.ptrType,slot.type,this)
                    }
                    PtrType.S -> {
                        val cloneObject = OneFieldSlot(slot.name,slot.ptrType,slot.type,this)
                        cloneObject.obj = slot.obj
                        return cloneObject
                    }
                    PtrType.O -> {
                        val cloneObject = OneFieldSlot(slot.name,slot.ptrType,slot.type,this)
                        cloneObject.obj = slot.obj
                        return cloneObject
                    }
                    else -> {return null}
                }
            }
            Type.LIST ->{
                slot as ListSlot<*>
                when (slot.ptrType) {
                    PtrType.U -> {
                        return ListSlot<Any>(slot.name,slot.varType,this)
                    }
                    PtrType.S -> {
                        val cloneObject = ListSlot<Any>(slot.name,slot.varType,this)
                        cloneObject.obj = slot.obj
                        return cloneObject
                    }
                    PtrType.O -> {
                        val cloneObject = ListSlot<Any>(slot.name,slot.varType,this)
                        cloneObject.obj = slot.obj
                        return cloneObject
                    }
                    else -> {return null}
                }
            }
            Type.TABLE ->{
                when (slot.ptrType) {
                    PtrType.U -> {
                        return TableSlot(slot.name,slot.ptrType,this)
                    }
                    PtrType.S -> {
                        val cloneObject = TableSlot(slot.name,slot.ptrType,this)
                        cloneObject.obj = slot.obj
                        return cloneObject
                    }
                    PtrType.O -> {
                        val cloneObject = TableSlot(slot.name,slot.ptrType,this)
                        cloneObject.obj = slot.obj
                        return cloneObject
                    }
                    else -> {return null}
                }
            }
            Type.LISP -> {
                slot as LispSlot
                val clone = LispSlot(slot.name,slot.getLispTypeT())
                for (e in slot.getList()){
                    clone.addArg(e)
                }
                clone.owner = this
                return clone
            }
            else -> return null
        }
    }

    private fun checkSlot (slotName: String): Boolean {
        for (s in slots){
            if (s.name == slotName){
                return false
            }
        }
        for (f in frames){
            if (f.name == slotName){
                return false
            }
        }
        return true
    }

}