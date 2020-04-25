package library

enum class PtrType {
    U,O,S,R
}
/*
object TypeGetter {
    fun getType(name: String): PtrType? {
        for (t in PtrType.values()) {
            if (t.name == name) {
                return t
            }
        }
        throw IllegalArgumentException("Имя типа указателя неверное!")
    }
}*/
