package cat.institutmontivi.themoviedb.ui.pantalles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import cat.institutmontilivi.tasquesfirebase.dades.xarxa.manegadors.analitiques.ManegadorAnalitiques

//@Preview
@Composable
fun PantallaInstruccions (manegadorAnalitiques: ManegadorAnalitiques)
{
    manegadorAnalitiques.registraVisitaAPantalla("PantallaInstruccions")
    Box(Modifier.fillMaxSize(). background(color = MaterialTheme.colorScheme.surfaceVariant))
    {
        Text(text = "Pantalla d'instruccions",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center)
    }
}

