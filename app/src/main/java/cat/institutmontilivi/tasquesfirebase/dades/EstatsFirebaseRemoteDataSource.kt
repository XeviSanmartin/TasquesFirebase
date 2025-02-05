package cat.institutmontilivi.tasquesfirebase.dades

import cat.institutmontilivi.tasquesfirebase.firestore.ManegadorFirestore
import cat.institutmontilivi.tasquesfirebase.model.app.Estat
import cat.institutmontilivi.tasquesfirebase.model.app.Resposta
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class EstatsFirebaseRemoteDataSource( manegadorFirestore: ManegadorFirestore) : EstatsRepositori{
    val db = manegadorFirestore

    override suspend fun obtenEstats(): Flow<Resposta<List<Estat>>>  = callbackFlow{
        var llista = mutableListOf<Estat>()
        try{
            val refEstats = db.firestoreDb.collection(db.ESTATS)

            val subscripcio = refEstats.addSnapshotListener{
                snapshot, _ ->
                snapshot?.let { querySnapshot ->  //Si l'snapshot no es null, processem la llista de documents
                    val estats = mutableListOf<Estat>()
                    for (document in querySnapshot.documents) {
                        val estat = document.toObject(Estat::class.java)
                        estat?.let { estats.add(it) }
                    }

                    trySend(Resposta.Exit(estats)).isSuccess
                }
            }
            awaitClose { subscripcio.remove() }
        }catch (e: Exception) {
            trySend (Resposta.Fracas(e.message ?: "Error obtenint llista d'estats"))
            }
    }

    override suspend fun afegeixEstat(estat: Estat): Resposta<Boolean> {
        var existeix = false
        try {
            runBlocking {//cal posar el runblocking per fer chaining i que bloquegi fins que no acabi la crida
                val resposta = existeixEstat(estat.nom)
                if (resposta is Resposta.Exit)
                    existeix = resposta.dades
                else if (resposta is Resposta.Fracas)
                    throw Exception(resposta.missatgeError)

                if (!existeix) {
                    val refEstats = db.firestoreDb.collection(db.ESTATS)
                    val refEstatNou = refEstats.document()
                    //val estatNou = estat.copy(id = refEstatNou.id)
                    estat.id = refEstatNou.id
                    refEstatNou.set(estat)
                    existeix = true
                }
            }
        }catch (e: Exception) {
            return Resposta.Fracas(missatgeError = e.message?: "Error afegint un estat nou (${estat.nom})")
        }
        return Resposta.Exit(existeix)
    }

    override suspend fun eliminaEstat(id: String): Resposta<Boolean> {
        var eliminat = false
        try {
            val refEstats = db.firestoreDb.collection(db.ESTATS)
            val refEstat = refEstats.document(id)
            refEstat.delete().await()
            eliminat = true
        }catch (e: Exception) {
            return Resposta.Fracas(e.message?:"Error eliminant l'estat ${id}")
        }
        return Resposta.Exit(eliminat)
    }

    override suspend fun actualitzaEstat(estat: Estat): Resposta<Boolean> {
        var actualitzat = false
        try {
            val refEstats = db.firestoreDb.collection(db.ESTATS)
            val refEstat = refEstats.document(estat.id)
            refEstat.set(estat).await()
            actualitzat = true
        }catch (e: Exception) {
            return Resposta.Fracas(e.message?:"Error cercant l'estat ${estat.nom}")
        }
        return Resposta.Exit(actualitzat)
    }

    override suspend fun existeixEstat(nom: String): Resposta<Boolean> {
        var noHiEs = false
        try {
            val refEstats = db.firestoreDb.collection(db.ESTATS)
            val consulta = refEstats.whereEqualTo("nom", nom)
             noHiEs = consulta.get().await().documents.isEmpty()
        }catch (e: Exception) {
            return Resposta.Fracas(e.message?:"Error cercant l'estat $nom")
        }
        return Resposta.Exit(!noHiEs)
    }

    override suspend fun obtenEstat(id:String): Resposta<Estat> {
        var estat = Estat()
        try {
            val refEstats = db.firestoreDb.collection(db.ESTATS)
            val refEstat = refEstats.document(id)
            val document = refEstat.get().await()
            estat = document.toObject(Estat::class.java)?: Estat()
        }catch (e: Exception) {
            return Resposta.Fracas(e.message?:"Error cercant l'estat $id")
        }
        return Resposta.Exit(estat)
    }
}