package library

object DataBase {
    var instance : HashMap<String,String>? = null
    get(){
           if (field == null){
               instance = HashMap()
           }
        return field
    }
    var frameNames : ArrayList<String>? = null
    get() {
        if (field == null){
            frameNames = ArrayList()
        }
        return field
    }
}