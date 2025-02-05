package cat.institutmontilivi.tasquesfirebase.dades

import android.content.Context
import cat.institutmontilivi.tasquesfirebase.firestore.ManegadorFirestore


object BBDDFactory
{
    enum class DatabaseType {
        FIREBASE
    }


    fun obtenRepositoriUsuaris(context: Context?, type: DatabaseType): UsuarisRepositori {
        return when (type) {
            DatabaseType.FIREBASE -> UsuarisFirebaseRemoteDataSource(ManegadorFirestore())
        }
    }

    fun obtenRepositoriEstats(context: Context?, type: DatabaseType): EstatsRepositori {
        return when (type) {
            DatabaseType.FIREBASE ->EstatsFirebaseRemoteDataSource(ManegadorFirestore())
        }
    }

    fun obtenRepositoriTasques(context: Context?, type: DatabaseType): TasquesRepositori {
        return when (type) {
            DatabaseType.FIREBASE ->TasquesFirebaseRemoteDataSource(ManegadorFirestore())
        }
    }
}