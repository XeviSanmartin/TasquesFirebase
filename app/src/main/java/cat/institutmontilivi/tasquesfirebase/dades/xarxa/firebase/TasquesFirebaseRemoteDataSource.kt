package cat.institutmontilivi.tasquesfirebase.dades.xarxa.firebase
import cat.institutmontilivi.tasquesfirebase.dades.BBDDFactory
import cat.institutmontilivi.tasquesfirebase.dades.TasquesRepositori
import cat.institutmontilivi.tasquesfirebase.dades.xarxa.manegadors.firestore.ManegadorFirestore
import cat.institutmontilivi.tasquesfirebase.dades.xarxa.manegadors.firestore.usuariActual
import cat.institutmontilivi.tasquesfirebase.model.app.Resposta
import cat.institutmontilivi.tasquesfirebase.model.app.Tasca
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class TasquesFirebaseRemoteDataSource(manegadorFirestore: ManegadorFirestore): TasquesRepositori {
    val db = manegadorFirestore

    override suspend fun obtenTasques(idUsuari: String): Flow<Resposta<List<Tasca>>> = callbackFlow{
        var llista = mutableListOf<Tasca>()
        lateinit var subscripcio : ListenerRegistration
        try{
            val refTasques = db.firestoreDb.collection(db.TASQUES).whereArrayContains("usuaris", idUsuari)

            subscripcio = refTasques.addSnapshotListener{
                    snapshot, _ ->
                snapshot?.let { querySnapshot ->  //Si l'snapshot no es null, processem la llista de documents
                    val tasques = mutableListOf<Tasca>()
                    for (document in querySnapshot.documents) {
                        val tasca = document.toObject(Tasca::class.java)
                        tasca?.let { tasques.add(it) }
                    }
                    trySend(Resposta.Exit(tasques)).isSuccess
                }
            }

        }catch (e: Exception) {
            trySend (Resposta.Fracas(e.message ?: "Error obtenint llista de tasques"))
        }
        finally {
            awaitClose { subscripcio.remove() }
        }
    }

    override suspend fun afegeixTasca(tasca: Tasca): Resposta<Boolean> {
        var existeix = false
        try {
            runBlocking {//cal posar el runblocking per fer chaining i que bloquegi fins que no acabi la crida
                val resposta = existeixTasca(tasca.titol)
                if (resposta is Resposta.Exit)
                    existeix = resposta.dades
                else if (resposta is Resposta.Fracas)
                    throw Exception(resposta.missatgeError)

                if (!existeix) {
                    val refTasques = db.firestoreDb.collection(db.TASQUES)
                    val refTascaNova = refTasques.document()
                    tasca.id = refTascaNova.id
                    tasca.usuaris = listOf(usuariActual.id)
                    refTascaNova.set(tasca)
                    existeix = true
                }
            }
        }catch (e: Exception) {
            return Resposta.Fracas(missatgeError = e.message?: "Error afegint un estat nou (${tasca.titol})")
        }
        return Resposta.Exit(existeix)
    }

    override suspend fun eliminaTasca(idTasca: String): Resposta<Boolean> {
        var eliminat = false
        try {
            val refTasques = db.firestoreDb.collection(db.TASQUES)
            val refTasca = refTasques.document(idTasca)
            val document = refTasca.get().await()
            val tasca = document.toObject(Tasca::class.java)
            checkNotNull(tasca)
            //Eliminem l'usuari actual de la tasca
            if(tasca.usuaris.contains(usuariActual.id) ) {
                val usuaris = tasca.usuaris.filter { it != usuariActual.id }
                tasca.usuaris = usuaris
            }
            //si no queda cap usuari a la tasca, la eliminem
            if (tasca.usuaris.isEmpty())
                refTasca.delete().await()
            else
                //en cas contrari, actualitzem la tasca treien l'usuari
                refTasca.set(tasca).await()

            //Eliminem la tasca de la llista de tasques de l'usuari
            BBDDFactory.obtenRepositoriUsuaris(null, BBDDFactory.DatabaseType.FIREBASE)
                .eliminaTascaDeUsuari(idTasca, usuariActual.id)


            eliminat = true
        }catch (e: Exception) {
            return Resposta.Fracas(e.message?:"Error eliminant la tasca ${idTasca}")
        }
        return Resposta.Exit(eliminat)
    }

    override suspend fun actualitzaTasca(tasca: Tasca): Resposta<Boolean> {
        var actualitzat = false
        try {
            val refTasques = db.firestoreDb.collection(db.TASQUES)
            val refTasca = refTasques.document(tasca.id)
            refTasca.set(tasca).await()
            actualitzat = true
        }catch (e: Exception) {
            return Resposta.Fracas(e.message?:"Error cercant l'estat ${tasca.titol}")
        }
        return Resposta.Exit(actualitzat)
    }

    override suspend fun existeixTasca(titol: String): Resposta<Boolean> {
        var noHiEs = false
        try {
            val refTasques = db.firestoreDb.collection(db.TASQUES)
            val consulta = refTasques.whereEqualTo("titol", titol)
            noHiEs = consulta.get().await().documents.isEmpty()
        }catch (e: Exception) {
            return Resposta.Fracas(e.message?:"Error cercant la tasca $titol")
        }
        return Resposta.Exit(!noHiEs)
    }

    override suspend fun obtenTasca(idTasca: String): Resposta<Tasca> {
        var tasca = Tasca()
        try {
            val refTasques = db.firestoreDb.collection(db.TASQUES)
            val refTasca = refTasques.document(idTasca)
            val document = refTasca.get().await()
            tasca = document.toObject(Tasca::class.java)?: Tasca()
        }catch (e: Exception) {
            return Resposta.Fracas(e.message?:"Error cercant la tasca $idTasca")
        }
        return Resposta.Exit(tasca)
    }

    override suspend fun convidaUsuari(idTasca: String, idUsuari: String): Resposta<Tasca> {
        var tasca = Tasca()
        try {
            val refTasques = db.firestoreDb.collection(db.TASQUES)
            val refTasca = refTasques.document(idTasca)
            val document = refTasca.get().await()
            tasca = document.toObject(Tasca::class.java)?: Tasca()
            val llista = tasca.usuaris.toMutableList()
            llista.add(idUsuari)
            tasca.usuaris = llista.toList()
            refTasca.set(tasca).await()
            BBDDFactory.obtenRepositoriUsuaris(null, BBDDFactory.DatabaseType.FIREBASE)
                .afegeixTascaAUsuari(idTasca, idUsuari)
        }catch (e: Exception) {
            return Resposta.Fracas(e.message?:"Error cercant la tasca $idTasca")
        }
        return Resposta.Exit(tasca)
    }
}