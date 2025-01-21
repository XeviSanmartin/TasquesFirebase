package cat.institutmontilivi.tasquesfirebase.autentificacio

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import cat.institutmontilivi.tasquesfirebase.R
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


sealed class RespostaDAutentificacio<out T> {
    data class Exit<T>(val dades: T): RespostaDAutentificacio<T>()
    data class Fracas(val missatgeError: String): RespostaDAutentificacio<Nothing>()
}
class ManegadorAutentificacio (private val context: Context){
    private val autentificacio: FirebaseAuth by lazy { Firebase.auth }
    private val manegadorIniciSessio = Identity.getSignInClient(context)

    suspend fun iniciaSessioAnonima(): RespostaDAutentificacio<FirebaseUser> {
        return try {
            val result = autentificacio.signInAnonymously().await()
            RespostaDAutentificacio.Exit(result.user ?: throw Exception("Error a l'iniciar la sessió"))
        } catch(e: Exception) {
            RespostaDAutentificacio.Fracas(e.message ?: "Error a l'iniciar la sessió")
        }
    }



    suspend fun creaUsuariAmbCorreuIMotDePas(correu: String, motDePas: String): RespostaDAutentificacio<FirebaseUser?> {
        return try {
            val authResult = autentificacio.createUserWithEmailAndPassword(correu, motDePas).await()
            RespostaDAutentificacio.Exit(authResult.user)
        } catch(e: Exception) {
            RespostaDAutentificacio.Fracas(e.message ?: "Error al crear l'usuari")
        }
    }

    suspend fun iniciaSessioAmbCorreuIMotDePas(correu: String, motDePas: String): RespostaDAutentificacio<FirebaseUser?> {
        return try {
            val authResult = autentificacio.signInWithEmailAndPassword(correu, motDePas).await()
            RespostaDAutentificacio.Exit(authResult.user)
        } catch(e: Exception) {
            RespostaDAutentificacio.Fracas(e.message ?: "Error a l'iniciar la sessió")
        }
    }

    suspend fun restableixElMotDePas(correu: String): RespostaDAutentificacio<Unit> {
        return try {
            autentificacio.sendPasswordResetEmail(correu).await()
            RespostaDAutentificacio.Exit(Unit)
        } catch(e: Exception) {
            RespostaDAutentificacio.Fracas(e.message ?: "No s'ha pogut restablir el mot de pas")
        }
    }

    fun tancaSessio() {
        autentificacio.signOut()
        manegadorIniciSessio.signOut()
    }

    fun obtenUsuariActual(): FirebaseUser?{
        return autentificacio.currentUser
    }

    private val clientIniciSessioGoogle: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.ClauDeLaApiWeb))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    fun manegaResultatIniciSessio(task: Task<GoogleSignInAccount>): RespostaDAutentificacio<GoogleSignInAccount>? {
        return try {
            val compte = task.getResult(ApiException::class.java)
            RespostaDAutentificacio.Exit(compte)
        } catch (e: ApiException) {
            RespostaDAutentificacio.Fracas(e.message ?: "No s'ha pogut iniciar sessió amb Google")
        }
    }

    suspend fun iniciDeSessioAmbCredencials(credencial: AuthCredential): RespostaDAutentificacio<FirebaseUser>? {
        return try {
            val usuariDeFirebase = autentificacio.signInWithCredential(credencial).await()
            usuariDeFirebase.user?.let {
                RespostaDAutentificacio.Exit(it)
            } ?: throw Exception("Ha fallat l'inici de sessió amb Google")
        } catch (e: Exception) {
            RespostaDAutentificacio.Fracas(e.message ?: "Ha fallat l'inici de sessió amb Google")
        }
    }

    fun iniciDeSessioAmbGoogle(laucherIniciDeSessioAmbGoogle: ActivityResultLauncher<Intent>) {
        val signInIntent = clientIniciSessioGoogle.signInIntent
        laucherIniciDeSessioAmbGoogle.launch(signInIntent)
    }


}