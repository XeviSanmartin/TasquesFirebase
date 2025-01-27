package cat.institutmontilivi.tasquesfirebase.firestore

import com.google.firebase.firestore.FirebaseFirestore

class ManegadorFirestore {
    public val CATEGORIES = "Categories"
    public val USUARIS = "Usuaris"
    public val ESTATS = "Estats"
    public val TASQUES = "Tasques"

    public val firestoreDb = FirebaseFirestore.getInstance()

    suspend fun EstatsInicials()
    {
        val estats = listOf("Per començar", "En procès", "Finalitzat")
        estats.forEachIndexed() { index,nomEstat->
            //afegeixProducteAUnaLlista(Producte(index.toString(),nomProducte,"",0, false), LlistaDeLaCompra("Nmlgwb9flgnaw7EdCAAT","",0))
        }

    }

}