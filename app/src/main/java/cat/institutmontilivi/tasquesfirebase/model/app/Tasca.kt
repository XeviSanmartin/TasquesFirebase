package cat.institutmontilivi.tasquesfirebase.model.app

class Tasca (
    var id:String="",
    var titol:String="",
    var descripcio:String="",
    var dataLimit:Long=0,
    var estat:String="",
    var usuaris:List<String> =listOf(),
    var uriFotos:List<String> =listOf(),
    var uriVideos:List<String> =listOf(),
    var uriAudios:List<String> =listOf(),
)