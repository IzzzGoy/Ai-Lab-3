class PersonKt(val fio: String,
               val education: String,
               val profession: String,
               val years: Int) {
    /*fun getFio(): String = fio
    fun getEducation(): String = education
    fun getProfession(): String = profession
    fun getYears(): Int = years*/

    fun checkProperty(propertyValue: String): Boolean {
        return when (education.toUpperCase() == propertyValue) {
            true -> true
            else -> when (profession.toUpperCase() == propertyValue) {
                true -> true
                else -> false
            }
        }
    }

    override fun toString(): String {
        return "FIO is : $fio Education is : $education Profession is : $profession Years : $years"
    }
    fun checkRange(range: IntRange): Boolean{
        return years in range
    }
}











