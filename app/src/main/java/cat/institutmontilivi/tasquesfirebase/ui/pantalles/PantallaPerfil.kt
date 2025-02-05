package cat.institutmontilivi.tasquesfirebase.ui.pantalles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cat.institutmontilivi.tasquesfirebase.R
import cat.institutmontilivi.tasquesfirebase.analitiques.ManegadorAnalitiques
import cat.institutmontilivi.tasquesfirebase.autentificacio.ManegadorAutentificacio
import cat.institutmontilivi.tasquesfirebase.firestore.ManegadorFirestore
import cat.institutmontilivi.tasquesfirebase.firestore.usuariActual
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewPerfil()
{
    PantallaPerfil(
        manegadorAnalitiques = ManegadorAnalitiques(LocalContext.current),
        manegadorAutentificacio = ManegadorAutentificacio(LocalContext.current),
        manegadorFirestore = ManegadorFirestore(),
        navegaALogin = { },
        //manegadorFirestore = ManegadorFirestore()//(LocalContext.current)
    )
}

@Composable
fun PantallaPerfil(
    manegadorAnalitiques: ManegadorAnalitiques,
    manegadorAutentificacio: ManegadorAutentificacio,
    manegadorFirestore: ManegadorFirestore,
    navegaALogin: () -> Unit,
    //manegadorFirestore: ManegadorFirestore
)
{

    val usuari = manegadorAutentificacio.obtenUsuariActual()
    val ambit = rememberCoroutineScope()

    manegadorAnalitiques.registraVisitaAPantalla(nomPantalla = "Perfil")
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(24.dp))
        if (usuari?.photoUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(usuari.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imatge de l'usuari",
                placeholder = painterResource(id = R.drawable.ic_perfil_incognit),

                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1F)
                    .padding(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_perfil_incognit),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                contentDescription = "Foto de perfil per defecte",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1F)
                    .padding(48.dp)
                    .clip(CircleShape), contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = usuari?.displayName?:"Usuari sense nom",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = usuari?.email?:"Usuari sense correu",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = usuari?.phoneNumber?:"Usuari sense número de telèfon",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = if (usuari?.isAnonymous?:true) "Usuari anònim" else "Usuari registrat",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                ambit.launch {
                    manegadorAutentificacio.tancaSessio()
                    usuariActual.id = ""
                    usuariActual.nom = ""
                    usuariActual.correu = ""
                    usuariActual.usuarisHabituals = listOf()
                    navegaALogin()
                }
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .padding(40.dp, 0.dp, 40.dp, 0.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Tanca la sessió".uppercase())
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}