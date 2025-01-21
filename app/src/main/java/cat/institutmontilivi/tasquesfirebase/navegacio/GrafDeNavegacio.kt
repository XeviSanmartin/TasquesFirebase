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
import cat.institutmontilivi.tasquesfirebase.ui.pantalles.PantallaRegistre
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaInstruccions
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaPortada
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaPreferencies
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaQuantA
import com.google.firebase.auth.FirebaseUser

@Composable
fun GrafDeNavegacio (controladorDeNavegacio: NavHostController = rememberNavController(), paddingValues: PaddingValues = PaddingValues(0.dp))
{
    val manegadorAnalitiques : ManegadorAnalitiques = ManegadorAnalitiques(LocalContext.current)
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

    val navegaARecuperaMotDePas ={controladorDeNavegacio.navigate(DestinacioRecuperaMotDePas){
        popUpTo(controladorDeNavegacio.graph.findStartDestination().id) { inclusive = false }
    }}

    val navegaAInici = { controladorDeNavegacio.navigate(if (usuari==null) DestinacioLogin else DestinacioPortada){
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
           PantallaPortada()
        }

        composable<DestinacioInstruccions> {
            PantallaInstruccions()
        }

        composable<DestinacioPreferencies> {
            PantallaPreferencies()
        }

        composable<DestinacioQuantA> {
            PantallaQuantA()
        }

        composable<DestinacioLogin> {
            PantallaLogin(manegadorAnalitiques, manegadorAutentificacio,navegaARegistre,navegaARecuperaMotDePas,navegaAInici)
        }

        composable<DestinacioRegistre> {
            PantallaRegistre(manegadorAnalitiques, manegadorAutentificacio,navegaEnrera,navegaAInici)
        }

        composable<DestinacioRecuperaMotDePas> {
            //PantallaPreferencies()
        }

        composable<DestinacioPerfil> {
            //PantallaQuantA()
        }

        composable<DestinacioCategories> {

            //PantallaPelisPopulars(onClic = {id->controladorDeNavegacio.navigate(DestinacioActorsPeli(id))})
        }


        composable<DestinacioTasques> {
            val argument = it.toRoute<DestinacioTasques>()

            //PantallaLlistaActors()
        }
    }

}