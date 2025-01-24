package cat.institutmontilivi.tasquesfirebase.ui.pantalles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.institutmontilivi.tasquesfirebase.R
import cat.institutmontilivi.tasquesfirebase.analitiques.ManegadorAnalitiques
import cat.institutmontilivi.tasquesfirebase.autentificacio.ManegadorAutentificacio
import cat.institutmontilivi.tasquesfirebase.autentificacio.RespostaDAutentificacio
import cat.institutmontilivi.tasquesfirebase.ui.PantallaDeLAplicacio
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.RuntimeException

@Preview
@Composable
fun PreviewLogin()
{
    PantallaDeLAplicacio {
        PantallaLogin(
            manegadorAnalitiques = ManegadorAnalitiques(LocalContext.current),
            manegadorAutentificacio = ManegadorAutentificacio(LocalContext.current),
            navegaARegistre={},
            navegaARecuperaMotDePas={}
        ) {}
    }
}

@Composable
fun PantallaLogin(
    manegadorAnalitiques: ManegadorAnalitiques,
    manegadorAutentificacio: ManegadorAutentificacio,
    navegaARegistre: () -> Unit,
    navegaARecuperaMotDePas: () -> Unit,
    navegaAInici: () -> Unit
)
{

    manegadorAnalitiques.registraVisitaAPantalla(nomPantalla = "Login")
    var correu by remember { mutableStateOf("") }
    var motDePas by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    var missatgeError by remember { mutableStateOf("") }
    val context = LocalContext.current
    val ambitDeCorrutina = rememberCoroutineScope()

//region Versió vella que utilitzava Google Singing
//    //Registrem un launcher fer fer l'inici de sessió amb Google
//    val launcherIniciDeSessioAmbGoogle = rememberLauncherForActivityResult(
//        //Llencem un activityForResult
//        contract = ActivityResultContracts.StartActivityForResult()) { result ->
//        //GoogleSignIn.getSignedInAccountFromIntent(result.data)) obre una activity de Google on
//        //L'usuari es valida, i ens retornarà una resposta que contindrà el compte de google o un error
//        when(val resposta = manegadorAutentificacio.manegaResultatIniciSessio(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
//            is RespostaDAutentificacio.Exit -> {
//                //Del compte de google volem les credencials, per tal de poder iniciar sessió amb Firebase
//                val credencial = GoogleAuthProvider.getCredential(resposta.dades.idToken, null)
//                ambit.launch {
//                    val usuariFirebase = manegadorAutentificacio.iniciDeSessioAmbCredencials(credencial)
//                    if (usuariFirebase != null){
//                        navegaAInici()
//                    }
//                }
//            }
//            is RespostaDAutentificacio.Fracas -> {
//                manegadorAnalitiques.registraError("Error d'autentificació: ${resposta.missatgeError}")
//                error = true
//                missatgeError= resposta.missatgeError
//            }
//            else -> {
//                error = true
//                manegadorAnalitiques.registraError("Hi ha hagut un error inesperat")
//                missatgeError = "Hi ha hagut un error inesperat"
//            }
//        }
//    }
    //endregion
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_firebase),
            contentDescription = "Firebase",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Firebase per a Android",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            modifier= Modifier
                .fillMaxWidth()
                .padding(40.dp, 0.dp, 40.dp, 0.dp),
            label = { Text(text = "Correu electrònic") },
            value = correu,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = {
                correu = it
                error=false
                missatgeError=""
            })
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            modifier= Modifier
                .fillMaxWidth()
                .padding(40.dp, 0.dp, 40.dp, 0.dp),
            label = { Text(text = "Mot de pas") },
            value = motDePas,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                motDePas = it
                error=false
                missatgeError=""
            })
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                manegadorAnalitiques.registreClicABoto("Inicia sessió amb correu i mot de pas")
                val deferredJob = ambitDeCorrutina.async{
                    return@async IniciDeSessioCorreu(correu, motDePas, manegadorAutentificacio, navegaAInici)
                }
                ambitDeCorrutina.launch {
                    val resultat = deferredJob.await()
                    error = resultat.first
                    missatgeError = resultat.second
                }
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .padding(40.dp, 0.dp, 40.dp, 0.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Inicia Sessió".uppercase())
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Encara no tens un compte? Regístrat!"),
            onClick = {
                manegadorAnalitiques.registreClicABoto("Encara no tens un compte? Regístrat!")
                navegaARegistre()
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = AnnotatedString("No recordes el teu mot de pas?"),
            onClick = {
                manegadorAnalitiques.registreClicABoto("Click: No recordes el teu mot de pas?")
                val deferredJob = ambitDeCorrutina.async{
                    return@async RecuperaMotDePas(correu,  manegadorAutentificacio)
                }
                ambitDeCorrutina.launch {
                    val resultat = deferredJob.await()
                    error = resultat.first
                    missatgeError = resultat.second
                }
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(25.dp))
        if(error)
        {
            Text(text = missatgeError, style = TextStyle(color = MaterialTheme.colorScheme.error))
        }
        else {
            Text(text = "-------- o --------", style = TextStyle(color = Color.Gray))
        }
        Spacer(modifier = Modifier.height(25.dp))
        BotoXarxaSocial(
            onClick = {
                manegadorAnalitiques.registreClicABoto("Continua com a convidat")
                ambitDeCorrutina.launch{
                    IniciDeSessioIncognit(manegadorAutentificacio, navegaAInici, manegadorAnalitiques)
                }
            },
            text = "Continua com a convidat",
            icon = R.drawable.ic_incognit,
            colorFons = MaterialTheme.colorScheme.inverseSurface, // Color(0xFF363636)
            colorLletra = MaterialTheme.colorScheme.inverseOnSurface
        )
        Spacer(modifier = Modifier.height(15.dp))
        BotoXarxaSocial(
            onClick = {
                manegadorAnalitiques.registreClicABoto("Inici de sessió amb Google")
                ambitDeCorrutina.launch {
                    error = !manegadorAutentificacio.iniciDeSessioAmbGoogle();
                    missatgeError = "Ha fallat l'autentificació amb Google"
                    if(!error)
                    {
                        navegaAInici()
                    }
                }

                //versió vella de google Signing
                //Enviem el launcher d'inici de sessió amb Google
                //manegadorAutentificacio.iniciDeSessioAmbGoogle(launcherIniciDeSessioAmbGoogle)
            },
            text = "Continua amb Google",
            icon = R.drawable.ic_google,
            colorFons = MaterialTheme.colorScheme.surfaceVariant, //Color(0xFFF1F1F1)
            colorLletra = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(25.dp))
        ClickableText(
            text = AnnotatedString("Força el tancament de Crashlytics"),
            onClick = {
                //val crashlytics = FirebaseCrashlytics.getInstance()
                //crashlytics.setCustomKey("clauDeProva", "valor de la prova")
                //crashlytics.log("Missatge personalitzat des d'un log")

                throw RuntimeException("Això força un error a la pantalla de login")
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary
            )
        )

    }
}



@Composable
fun BotoXarxaSocial(onClick: () -> Unit, text: String, icon: Int, colorFons: Color, colorLletra: Color) {
    var click by remember { mutableStateOf(false) }
    Surface(
        onClick = onClick,
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .clickable { click = !click },
        shape = RoundedCornerShape(50),
        border = BorderStroke(width = 1.dp, color = colorLletra),
        color = colorFons
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = Modifier.size(24.dp),
                contentDescription = text,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$text", color = colorLletra)
            click = true
        }
    }
}




suspend fun IniciDeSessioIncognit(
    manegadorAutentificacio: ManegadorAutentificacio,
    navegaAInici: () -> Unit,
    manegadorAnalitiques: ManegadorAnalitiques
) {
    when(manegadorAutentificacio.iniciaSessioAnonima()){
        is RespostaDAutentificacio.Exit->{
            navegaAInici()
        }
        is RespostaDAutentificacio.Fracas->{
            manegadorAnalitiques.registraError("Error a l'iniciar la sessió incognita")
        }
    }
}


suspend fun IniciDeSessioCorreu(
    correu: String,
    motDePas: String,
    manegadorAutentificacio: ManegadorAutentificacio,
    navegaAInici: () -> Unit
):Pair<Boolean,String> {
    lateinit var resultat: Pair<Boolean, String>
    if(correu.isNotEmpty() && motDePas.isNotEmpty()) {
        when (val resposta = manegadorAutentificacio.iniciaSessioAmbCorreuIMotDePas (correu, motDePas)) {
            is RespostaDAutentificacio.Exit -> {
                navegaAInici()
                resultat = Pair(false, "")
            }
            is RespostaDAutentificacio.Fracas -> {
                resultat = Pair(true, resposta.missatgeError)
            }
        }
    } else {
        resultat = Pair(true, "Cal omplir tots els camps")
    }
    return resultat
}

suspend fun RecuperaMotDePas(
    correu: String,
    manegadorAutentificacio: ManegadorAutentificacio
):Pair<Boolean,String> {
    lateinit var resultat: Pair<Boolean, String>
    if(correu.isNotEmpty()) {
        when (val resposta = manegadorAutentificacio.restableixElMotDePas (correu)) {
            is RespostaDAutentificacio.Exit -> {
                resultat = Pair(false, "")
            }
            is RespostaDAutentificacio.Fracas -> {
                resultat = Pair(true, resposta.missatgeError)
            }
        }
    } else {
        resultat = Pair(true, "Cal omplir tots els camps")
    }
    return resultat
}
