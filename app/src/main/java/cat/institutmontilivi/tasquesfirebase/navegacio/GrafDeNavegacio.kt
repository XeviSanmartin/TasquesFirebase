package cat.institutmontilivi.tasquesfirebase.navegacio

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cat.institutmontilivi.tasquesfirebase.analitiques.ManegadorAnalitiques
import cat.institutmontilivi.tasquesfirebase.autentificacio.ManegadorAutentificacio
import cat.institutmontilivi.tasquesfirebase.ui.pantalles.PantallaLogin
import cat.institutmontilivi.tasquesfirebase.ui.pantalles.PantallaPerfil
import cat.institutmontilivi.tasquesfirebase.ui.pantalles.PantallaRegistre
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaInstruccions
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaPortada
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaPreferencies
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaQuantA
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.FirebaseCrashlytics

@Composable
fun GrafDeNavegacio (
    manegadorAnalitiques: ManegadorAnalitiques,
    controladorDeNavegacio: NavHostController = rememberNavController(),
    paddingValues: PaddingValues = PaddingValues(0.dp),
    crashlytics: FirebaseCrashlytics
)
{
    //val manegadorAnalitiques : ManegadorAnalitiques = ManegadorAnalitiques(LocalContext.current)
    val manegadorAutentificacio = ManegadorAutentificacio(LocalContext.current)

    val usuari: FirebaseUser? = manegadorAutentificacio.obtenUsuariActual()

//    controladorDeNavegacio.navigate(opcio.ruta) {
//        popUpTo(controladorDeNavegacio.graph.findStartDestination().id){
//            //guarda l'estat de la pantalla de la que marxem (funciona d'aquella manera,
//            // No tots els valors es guarden))
//            saveState = true
//        }
//        launchSingleTop = true
//        //Restaura l'estat de la pantalla i la deixa tal i com estava quan vam navegar a un altre lloc
//        restoreState = true
    //region Lambdas
    val navegaARegistre ={controladorDeNavegacio.navigate(DestinacioRegistre){
        popUpTo(controladorDeNavegacio.graph.findStartDestination().id) { inclusive = false }
    }}

    val navegaAInici = { controladorDeNavegacio.navigate(
        if (usuari==null)
            DestinacioLogin
        else
            DestinacioPortada){
        popUpTo(controladorDeNavegacio.graph.findStartDestination().id) { inclusive = false }
        launchSingleTop = true
    }}
    val navegaALogin = {controladorDeNavegacio.navigate(DestinacioLogin){
        popUpTo(controladorDeNavegacio.graph.findStartDestination().id) { inclusive = false }
        launchSingleTop = true
    }}

    val navegaEnrera: ()->Unit = {controladorDeNavegacio.popBackStack()}
    //endregion

    NavHost(
        navController = controladorDeNavegacio,
        startDestination = if (usuari==null) DestinacioLogin else DestinacioPortada,
        //startDestination = DestinacioPortada,
        modifier = Modifier.padding(paddingValues))
    {
        composable<DestinacioPortada> {
           PantallaPortada(manegadorAnalitiques)
        }

        composable<DestinacioInstruccions> {
            PantallaInstruccions(manegadorAnalitiques)
        }

        composable<DestinacioPreferencies> {
            PantallaPreferencies(manegadorAnalitiques)
        }

        composable<DestinacioQuantA> {
            PantallaQuantA(manegadorAnalitiques)
        }

        composable<DestinacioLogin> {
            PantallaLogin(manegadorAnalitiques, manegadorAutentificacio,crashlytics,navegaARegistre,navegaAInici)
        }

        composable<DestinacioRegistre> {
            PantallaRegistre(manegadorAnalitiques, manegadorAutentificacio,navegaEnrera,navegaAInici)
        }

        composable<DestinacioPerfil> {
            PantallaPerfil(manegadorAnalitiques, manegadorAutentificacio, navegaALogin)
        }

        composable<DestinacioCategories> {

        }


        composable<DestinacioTasques> {
            val argument = it.toRoute<DestinacioTasques>()

        }
    }

}