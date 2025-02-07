package cat.institutmontilivi.tasquesfirebase.dades.xarxa.manegadors.autentificacio

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import cat.institutmontilivi.tasquesfirebase.model.app.Resposta
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException



class ManegadorAutentificacio (private val context: Context){
    private val idClientWeb="848698198631-2e7n0987pms64v55nn0mhi8alkp32iev.apps.googleusercontent.com"
    private val tag = "MANEGADOR_AUTENTIFICACIO"
    private val autentificacio: FirebaseAuth by lazy {
        Firebase.auth
    }


    private val manegadorDeCredencials = CredentialManager.create(context)


    suspend fun iniciaSessioAnonima(): Resposta<FirebaseUser> {
        return try {
            val result = autentificacio.signInAnonymously().await()
            Resposta.Exit(result.user ?: throw Exception("Error a l'iniciar la sessió"))
        } catch(e: Exception) {
            Resposta.Fracas(e.message ?: "Error a l'iniciar la sessió")
        }
    }



    suspend fun creaUsuariAmbCorreuIMotDePas(correu: String, motDePas: String): Resposta<FirebaseUser?> {
        return try {
            val authResult = autentificacio.createUserWithEmailAndPassword(correu, motDePas).await()
            Resposta.Exit(authResult.user)
        } catch(e: Exception) {
            Resposta.Fracas(e.message ?: "Error al crear l'usuari")
        }
    }

    suspend fun iniciaSessioAmbCorreuIMotDePas(correu: String, motDePas: String): Resposta<FirebaseUser?> {
        return try {
            val authResult = autentificacio.signInWithEmailAndPassword(correu, motDePas).await()
            Resposta.Exit(authResult.user)
        } catch(e: Exception) {
            Resposta.Fracas(e.message ?: "Error a l'iniciar la sessió")
        }
    }

    suspend fun restableixElMotDePas(correu: String): Resposta<Unit> {
        return try {
            autentificacio.sendPasswordResetEmail(correu).await()
            Resposta.Exit(Unit)
        } catch(e: Exception) {
            Resposta.Fracas(e.message ?: "No s'ha pogut restablir el mot de pas")
        }
    }

    suspend fun tancaSessio() {

        manegadorDeCredencials.clearCredentialState(ClearCredentialStateRequest())
        autentificacio.signOut()
    }

    fun obtenUsuariActual(): FirebaseUser?{
        return autentificacio.currentUser
    }

    fun hiHaUsuariIniciat() =obtenUsuariActual() != null


    private suspend fun creaPeticioDeCredencials(): GetCredentialResponse
    {
        val peticio = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false) //true si tan sols volem que surtin els comptes registrats al dispositiu
                    .setServerClientId(idClientWeb)
                    .setAutoSelectEnabled(false) //true si volem que seleccioni automàticament el compte principal
                    .build()
            )
            .build()
        return manegadorDeCredencials.getCredential(
            request = peticio,
            context = context
        )
    }

    private suspend fun manegaIniciDeSessio(resultat: GetCredentialResponse): Boolean {
        val credencial = resultat.credential
        if (credencial is CustomCredential &&
            credencial.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)
        {
            try{
                val token = GoogleIdTokenCredential.createFrom(credencial.data)
                Log.i(tag, "Token: ${token.displayName}")
                Log.i(tag, "Token: ${token.familyName}")
                Log.i(tag, "Token: ${token.phoneNumber}")
                Log.i(tag, "Token: ${token.profilePictureUri}")

                val credencialDeGoogle = GoogleAuthProvider.getCredential(token.idToken, null)
                val resultatDeAutenticacio = autentificacio.signInWithCredential(credencialDeGoogle).await()

                return resultatDeAutenticacio.user != null
            }
            catch (e:GoogleIdTokenParsingException)
            {
                Log.e(tag, "Error al parsejar el token: ${e.message}")
                return false
            }

        }
        return false;
    }

    suspend fun iniciDeSessioAmbGoogle():Boolean
    {
        if(hiHaUsuariIniciat())
        {
            Log.d(tag,"Ja hi ha un usuari iniciat" )
            return true;
        }
        else{
            try {
                val resultat = creaPeticioDeCredencials()
                manegaIniciDeSessio(resultat)
                return hiHaUsuariIniciat()
            }
            catch (e:Exception)
            {
                if (e is CancellationException) throw e //Cal rellençar aquesta excepció perquè torni a provar de fer el login
                Log.e(tag, "Error al iniciar sessió amb Google: ${e.message}")
                return false
            }
        }

    }
}