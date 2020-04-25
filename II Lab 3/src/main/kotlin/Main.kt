
import library.DataBase
import tornadofx.launch

fun main(args:Array<String>){
    DataBase.instance?.put("Kill","2")
    DataBase.instance?.put("Fuck","U")
    launch<MyApp>()
}