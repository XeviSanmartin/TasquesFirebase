package cat.institutmontilivi.tasquesfirebase.dades

import android.content.Context
import cat.institutmontilivi.tasquesfirebase.firestore.ManegadorFirestore


class BBDDFactory()
{
    enum class DatabaseType {
        FIREBASE, ROOM
    }

    fun creaRepositoriUsuaris(context: Context?, type: DatabaseType): RepositoriUsuaris {
        return when (type) {
            DatabaseType.FIREBASE -> UsuarisFirebaseRemoteDataSource(ManegadorFirestore())
            DatabaseType.ROOM -> RepositoriUsuarisRoom(context)
        }

    }
}