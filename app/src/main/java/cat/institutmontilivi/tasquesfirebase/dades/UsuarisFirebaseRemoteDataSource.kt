package cat.institutmontilivi.tasquesfirebase.dades

import cat.institutmontilivi.tasquesfirebase.firestore.ManegadorFirestore
import cat.institutmontilivi.tasquesfirebase.model.app.Categoria
import cat.institutmontilivi.tasquesfirebase.model.app.Tasca
import cat.institutmontilivi.tasquesfirebase.model.app.Usuari

//https://medium.com/firebase-tips-tricks/how-to-read-data-from-cloud-firestore-using-get-bf03b6ee4953

class UsuarisFirebaseRemoteDataSource ( manegadorFirestore: ManegadorFirestore):RepositoriUsuaris {
    val db = manegadorFirestore

    override fun obtenUsuaris(): List<Usuari> {
        TODO("Not yet implemented")
    }

    override fun afegeixUsuari(usuari: Usuari): Boolean {

        val ref = db.firestoreDb.collection(db.USUARIS).document()
        val usuariNou = usuari.copy(id = ref.id)
        ref.set(usuariNou)
        return true
    }

    override fun eliminaUsuari(id: Usuari): Boolean {
        TODO("Not yet implemented")
    }

    override fun modificaUsuari(usuari: Usuari): Boolean {
        TODO("Not yet implemented")
    }

    override fun obtenTasquesUsuari(id: String): List<Tasca> {
        TODO("Not yet implemented")
    }

    override fun obtenCategoriesUsuari(id: String): List<Categoria> {
        TODO("Not yet implemented")
    }
}