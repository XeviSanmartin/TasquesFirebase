package cat.institutmontilivi.tasquesfirebase.dades

import android.content.Context
import cat.institutmontilivi.tasquesfirebase.firestore.ManegadorFirestore


object BBDDFactory
{
    enum class DatabaseType {
        FIREBASE
    }

    var repositoriUsuaris: EstatsRepositoriHelper? = null
    var repositoriEstats: UsuarisRepositori? = null

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

//        fun obtenRepositoriEstats(context: Context?, type: DatabaseType): EstatsRepositoriHelper {
//            return when (type) {
//                DatabaseType.FIREBASE ->
//                {
//                    if (repositoriUsuaris == null)
//                        repositoriUsuaris = EstatsRepositoriHelperImplFirebase(ManegadorFirestore())
//                    repositoriUsuaris!!
//                }
//            }
//        }

}