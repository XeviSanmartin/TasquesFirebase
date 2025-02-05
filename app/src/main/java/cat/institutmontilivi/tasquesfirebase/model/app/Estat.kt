package cat.institutmontilivi.tasquesfirebase.model.app


/*
Els camps de la classe cal que siguin "var" i que tinguin un valor per defecte per tal que
es pugui fer la deserialització automàtica de Firestore.
 */
class Estat(
    var id:String = "",
    var nom:String = "",
    var colorFons:String = "",
    var colorText:String ="",
)
