package cat.institutmontilivi.tasquesfirebase.model.app

data class Usuari(
    val id:String,
    val nom:String,
    val cognom:String,
    val correu:String,
    val llistes:List<String>,
    val tasques:List<String>,
    val usuarisHabituals:List<String>
)
