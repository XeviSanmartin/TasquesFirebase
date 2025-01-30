package cat.institutmontilivi.tasquesfirebase.dades

import cat.institutmontilivi.tasquesfirebase.firestore.ManegadorFirestore
import cat.institutmontilivi.tasquesfirebase.model.app.Estat
import cat.institutmontilivi.tasquesfirebase.model.app.Resposta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class EstatsRepositoriHelperImplFirebase(manegadorFirestore: ManegadorFirestore):EstatsRepositoriHelper {
    val estatRepositori = EstatsFirebaseRemoteDataSource(manegadorFirestore)
    override suspend fun obtenEstats(): Flow<Resposta<List<Estat>>> = flow{
        //emit(estatRepositori.obtenEstats())
    }

    override suspend fun afegeixEstat(estat: Estat): Flow<Resposta<Boolean>> =flow{
        emit(estatRepositori.afegeixEstat(estat))
    }

    override suspend fun eliminaEstat(id: String): Flow<Resposta<Boolean>> =flow{
        emit(estatRepositori.eliminaEstat(id))
    }

    override suspend fun actualitzaEstat(estat: Estat): Flow<Resposta<Boolean>>  = flow{
        emit(estatRepositori.actualitzaEstat(estat))
    }

    override suspend fun existeixEstat(nom: String): Flow<Resposta<Boolean>> =flow{
        emit(estatRepositori.existeixEstat(nom))
    }

    override suspend fun obtenEstat(id: String): Flow<Resposta<Estat>> =flow{
        emit(estatRepositori.obtenEstat(id))
    }
}