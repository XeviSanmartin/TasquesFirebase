package cat.institutmontilivi.tasquesfirebase.dades

import cat.institutmontilivi.tasquesfirebase.model.app.Categoria
import cat.institutmontilivi.tasquesfirebase.model.app.Resposta
import cat.institutmontilivi.tasquesfirebase.model.app.Tasca
import cat.institutmontilivi.tasquesfirebase.model.app.Usuari

interface UsuarisRepositori {
    suspend fun obtenUsuaris(): Resposta<List<Usuari>>
    suspend fun obtenUsuari(correu: String): Resposta<Usuari>
    suspend fun afegeixUsuari(usuari: Usuari): Resposta<Boolean>
    suspend fun eliminaUsuari(id: Usuari): Resposta<Boolean>
    suspend fun modificaUsuari(usuari: Usuari): Resposta<Boolean>
    suspend fun obtenTasquesUsuari(id: String): Resposta<List<Tasca>>
    suspend fun existeixUsuari(correu: String): Resposta<Boolean>
    suspend fun eliminaTascaDeUsuari(idTasca: String, idUsuari: String): Resposta<Boolean>
    suspend fun afegeixTascaAUsuari(idTasca: String, idUsuari: String): Resposta<Boolean>

}