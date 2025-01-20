package cat.institutmontilivi.tasquesfirebase.model.app

data class Tasca (
    val id:String,
    val titol:String,
    val descripcio:String,
    val dataLimit:Long,
    val estat:String,
    val idCategoria:String,
    val usuaris:List<String>,
    val uriFotos:List<String>,
    val uriVideos:List<String>,
    val uriAudios:List<String>,
)