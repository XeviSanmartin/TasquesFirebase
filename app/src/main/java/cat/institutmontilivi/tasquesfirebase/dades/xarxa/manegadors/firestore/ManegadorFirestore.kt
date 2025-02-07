package cat.institutmontilivi.tasquesfirebase.dades.xarxa.manegadors.firestore

import cat.institutmontilivi.tasquesfirebase.model.app.Usuari
import com.google.firebase.firestore.FirebaseFirestore

class ManegadorFirestore {
    public val USUARIS = "Usuaris"
    public val ESTATS = "Estats"
    public val TASQUES = "Tasques"

    public val firestoreDb = FirebaseFirestore.getInstance()
}