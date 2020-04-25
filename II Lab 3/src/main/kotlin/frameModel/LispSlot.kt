package frameModel

import PersonKt
import library.PtrType
import library.Type

enum class  LispType{
    FIND,
    COMPARE,
    PRODUCE
}

class LispSlot(name: String, private val lispType: LispType) : Slot(name, Type.LISP, PtrType.S, null,null) {
    private val args = ArrayList<String>()
    private var returns :Slot? = null
    fun addArg(string: String){args.add(string)}
    fun getList():List<String>{return args.toList()}
    fun getLispType():String {return lispType.name}
    fun getLispTypeT():LispType {return lispType}
    fun getReturns():Slot?{return returns}

    fun execute():Slot?{
        if (lispType == LispType.FIND){
            if (args.size < 2){
                return null
            }
            if (owner?.name != args[0]){
                val res = owner?.execute(name)
                if (res != null){
                    returns = (res as Slot)
                }
            } else {
                for (s in owner?.slots!!){
                    if (s.name == args[1]){
                        return s
                    }
                }
            }
        } else if (lispType == LispType.COMPARE){
            val result = ArrayList<PersonKt>()
            var input = owner!!.getCloneFrames()
            var intyt = ArrayList<Frame>()

            var f = owner
            while (f?.owner != null){f = f.owner}

            if (args.size < 3) { return null }
            for (i in 0 until owner!!.childFramesCount()){
                val ed = (f!!.execute(args[0]) as? Slot)?.obj as? String
                val prof = (f.execute(args[1]) as? Slot)?.obj as? String
                val yearsRange = (f.execute(args[2]) as Slot).obj as IntRange
                if (ed != null){
                    for (e in input){
                        if (e.slots[e.slots.size - 3].obj as String == ed){
                            intyt.add(e)
                        }
                    }
                    input.clear()
                    input.addAll(intyt)
                    intyt.clear()
                }

                if (prof != null){
                    for (e in input){
                        if (e.slots[e.slots.size - 2].obj as String == prof){
                            intyt.add(e)
                        }
                    }
                    input.clear()
                    input.addAll(intyt)
                    intyt.clear()
                }

                for (e in input){
                    if (e.slots[e.slots.size - 1].obj as Int in yearsRange){
                        intyt.add(e)
                    }
                }
                input.clear()
                input.addAll(intyt)
                intyt.clear()
                for (e in input){
                    result.add(PersonKt(
                        e.name,
                        e.slots[e.slots.size - 3].obj as String,
                        e.slots[e.slots.size - 2].obj as String,
                        e.slots[e.slots.size - 1].obj as Int
                    ))
                }
                /*if (
                    owner!!.getChildFrame(i).slots[0].obj as String ==  &&
                    owner!!.getChildFrame(i).slots[1].obj as String == (f.execute(args[1]) as Slot).obj as String &&
                    owner!!.getChildFrame(i).slots[2].obj as Int in (f.execute(args[2]) as Slot).obj as IntRange
                ){
                    result.add(PersonKt(
                        owner!!.getChildFrame(i).name,
                        owner!!.getChildFrame(i).slots[0].obj as String,
                        owner!!.getChildFrame(i).slots[1].obj as String,
                        owner!!.getChildFrame(i).slots[2].obj as Int
                    ))
                }*/
            }
            for (e in result){
                println(e.toString())
            }
        } else if (lispType == LispType.PRODUCE){
            if (args.size < 1){return null}
            if (owner != null){
                var f = owner
                while (f?.owner != null){f = f.owner}
                for (s in f!!.slots){
                    if (s.name == args[0]){
                        s as LispSlot
                        if (s.returns != null) {
                            val list = s.returns as ListSlot<PersonKt>
                            owner!!.clearChildFrames()
                            for (i in list.getList().indices){
                                owner!!.addFrame(list.getList()[i].fio)
                                owner!!.getChildFrame(i).addSlot(OneFieldSlot("Education",PtrType.U,Type.TEXT, obj = list.getList()[i].education))
                                owner!!.getChildFrame(i).addSlot(OneFieldSlot("Profession",PtrType.U,Type.TEXT, obj = list.getList()[i].profession))
                                owner!!.getChildFrame(i).addSlot(OneFieldSlot("Years",PtrType.U,Type.INTEGER, obj = list.getList()[i].years))
                            }
                        } else {
                            val list = f.execute(args[0])as ListSlot<PersonKt>
                            owner!!.clearChildFrames()
                            for (i in list.getList().indices){
                                owner!!.addFrame(list.getList()[i].fio)
                                owner!!.getChildFrame(i).addSlot(OneFieldSlot("Education",PtrType.U,Type.TEXT, obj = list.getList()[i].education))
                                owner!!.getChildFrame(i).addSlot(OneFieldSlot("Profession",PtrType.U,Type.TEXT, obj = list.getList()[i].profession))
                                owner!!.getChildFrame(i).addSlot(OneFieldSlot("Years",PtrType.U,Type.INTEGER, obj = list.getList()[i].years))
                            }
                        }
                    }
                }
            }
        }
        return null
    }


    override fun iffAdded() {}

    override fun ifRemoved() {}

    override fun ifNeeded(): Any? {return null}
}