package cat.institutmontilivi.tasquesfirebase.dades

import cat.institutmontilivi.tasquesfirebase.model.app.Estat
import cat.institutmontilivi.tasquesfirebase.model.app.Resposta
import kotlinx.coroutines.flow.Flow

interface EstatsRepositoriHelper {
    suspend fun obtenEstats(): Flow<Resposta<List<Estat>>>
    suspend fun afegeixEstat(estat: Estat): Flow<Resposta<Boolean>>
    suspend fun eliminaEstat(id: String): Flow<Resposta<Boolean>>
    suspend fun actualitzaEstat(estat: Estat): Flow<Resposta<Boolean>>
    suspend fun existeixEstat(nom: String): Flow<Resposta<Boolean>>
    suspend fun obtenEstat(id: String): Flow<Resposta<Estat>>
}