package cat.institutmontilivi.tasquesfirebase.model.app

class Usuari(
    var id:String="",
    var nom:String="",
    var cognom:String="",
    var correu:String="",
    var tasques:List<String> = listOf(),
    var usuarisHabituals:List<String> = listOf(),
)
