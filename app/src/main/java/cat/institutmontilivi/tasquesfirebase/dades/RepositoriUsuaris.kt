package cat.institutmontilivi.tasquesfirebase.dades

import cat.institutmontilivi.tasquesfirebase.model.app.Categoria
import cat.institutmontilivi.tasquesfirebase.model.app.Tasca
import cat.institutmontilivi.tasquesfirebase.model.app.Usuari

interface RepositoriUsuaris {
    fun obtenUsuaris(): List<Usuari>
    fun afegeixUsuari(usuari: Usuari): Boolean
    fun eliminaUsuari(id: Usuari): Boolean
    fun modificaUsuari(usuari: Usuari): Boolean
    fun obtenTasquesUsuari(id: String): List<Tasca>
    fun obtenCategoriesUsuari(id: String): List<Categoria>
}