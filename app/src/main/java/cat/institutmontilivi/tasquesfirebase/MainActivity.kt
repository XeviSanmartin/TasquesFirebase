package cat.institutmontilivi.tasquesfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cat.institutmontilivi.tasquesfirebase.analitiques.ManegadorAnalitiques
import cat.institutmontilivi.tasquesfirebase.autentificacio.ManegadorAutentificacio
import cat.institutmontilivi.tasquesfirebase.firestore.ManegadorFirestore
import cat.institutmontilivi.tasquesfirebase.ui.Aplicacio
import cat.institutmontilivi.tasquesfirebase.ui.theme.TasquesFirebaseTheme
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var manegadorFirestore = ManegadorFirestore()
        var manegadorAutentificacio = ManegadorAutentificacio(this)
        val crashlytics = FirebaseCrashlytics.getInstance()
        val manegadorAnalitiques = ManegadorAnalitiques(this)
        setContent {
            TasquesFirebaseTheme {
                Aplicacio(manegadorFirestore= manegadorFirestore,
                    manegadorAutentificacio = manegadorAutentificacio,
                    crashlytics = crashlytics,
                    manegadorAnalitiques = manegadorAnalitiques)
                }
            }
    }
}

