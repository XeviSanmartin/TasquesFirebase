package cat.institutmontilivi.tasquesfirebase.navegacio

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaInstruccions
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaPortada
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaPreferencies
import cat.institutmontivi.themoviedb.ui.pantalles.PantallaQuantA

@Composable
fun GrafDeNavegacio (controladorDeNavegacio: NavHostController = rememberNavController(), paddingValues: PaddingValues = PaddingValues(0.dp))
{
    val usuari = null

    NavHost(
        navController = controladorDeNavegacio,
       // startDestination = if (usuari==null) DestinacioLogin else DestinacioPortada,
        startDestination = DestinacioPortada,
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
            // PantallaPortada()
        }

        composable<DestinacioRegistre> {
            //PantallaInstruccions()
        }

        composable<DestinacioRecuperaMotDePAs> {
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