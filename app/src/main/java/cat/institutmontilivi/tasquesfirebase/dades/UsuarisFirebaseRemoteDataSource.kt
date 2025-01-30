package cat.institutmontilivi.tasquesfirebase.dades

import cat.institutmontilivi.tasquesfirebase.firestore.ManegadorFirestore
import cat.institutmontilivi.tasquesfirebase.model.app.Categoria
import cat.institutmontilivi.tasquesfirebase.model.app.Resposta
import cat.institutmontilivi.tasquesfirebase.model.app.Tasca
import cat.institutmontilivi.tasquesfirebase.model.app.Usuari
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


//https://medium.com/firebase-tips-tricks/how-to-read-data-from-cloud-firestore-using-get-bf03b6ee4953

class UsuarisFirebaseRemoteDataSource ( manegadorFirestore: ManegadorFirestore):UsuarisRepositori {
    val db = manegadorFirestore

    override suspend  fun obtenUsuaris(): Resposta<List<Usuari>> {
        TODO("Not yet implemented")
    }

    override suspend  fun afegeixUsuari(usuari: Usuari): Resposta<Boolean> {
        var existeix = false
        try {
            runBlocking {//cal posar el runblocking per fer chaining i que bloquegi fins que no acabi la crida
                val resposta = existeixUsuari(usuari.correu)
                if (resposta is Resposta.Exit)
                    existeix = resposta.dades
                else if (resposta is Resposta.Fracas)
                    throw Exception(resposta.missatgeError)
            }

            if (!existeix) {
                val refUsuaris = db.firestoreDb.collection(db.USUARIS)
                val refUsuariNou = refUsuaris.document()
                val usuariNou = usuari.copy(id = refUsuariNou.id)
                refUsuariNou.set(usuariNou)
                existeix = true
            }
        }catch(e: Exception) {
            Resposta.Fracas(e.message ?: "Error en alta d'usuari ${usuari.correu}")
        }
        return Resposta.Exit(existeix)
    }

    override suspend  fun eliminaUsuari(id: Usuari): Resposta<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend  fun modificaUsuari(usuari: Usuari): Resposta<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend  fun obtenTasquesUsuari(id: String): Resposta<List<Tasca>> {
        TODO("Not yet implemented")
    }

    override suspend  fun obtenCategoriesUsuari(id: String): Resposta<List<Categoria>> {
        TODO("Not yet implemented")
    }

    override suspend fun existeixUsuari(correu: String): Resposta<Boolean> {
        var niHiEs = false
        try {
            val refUsuaris = db.firestoreDb.collection(db.USUARIS)
            val consulta = refUsuaris.whereEqualTo("correu", correu)
            niHiEs = consulta.get().await().documents.isEmpty()

        }catch (e: Exception) {
            Resposta.Fracas(e.message ?: "Error cercant l'usuari $correu" )
        }
        return Resposta.Exit(!niHiEs)
    }
}