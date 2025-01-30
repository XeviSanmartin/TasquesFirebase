package cat.institutmontilivi.tasquesfirebase.ui.pantalles


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import cat.institutmontilivi.tasquesfirebase.model.app.Estat
import cat.institutmontilivi.tasquesfirebase.ui.viewmodels.ViewModelEstats


@Preview
@Composable
fun PantallaEstats(viewModel: ViewModelEstats = ViewModelEstats())
{
    val estat = viewModel.estat.collectAsState()
    var mostraDialegAfegeixEstat by remember { mutableStateOf(false) }

    val afegeixEstat = { estat: Estat -> viewModel.afegeixEstat(estat) }
    val eliminaEstat = { id: String -> viewModel.eliminaEstat(id) }
    val actualizaEstat = {estat:Estat -> viewModel.actualitzaEstat(estat) }

    val onDialogDismissed = { mostraDialegAfegeixEstat = false}

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                mostraDialegAfegeixEstat = true
            })
        {Icon(Icons.Filled.Add, "Floating action button.") }
        if (mostraDialegAfegeixEstat){
            DialegAfegeixEstat(afegeixEstat, onDialogDismissed)
        }
        }
    )
    {padding->

        LazyColumn (modifier = Modifier.padding(padding))
        {
            items(estat.value.estats)
            {
                ElementEstat(it, eliminaEstat, actualizaEstat)
            }
        }

    }
}

@Preview
@Composable
fun ElementEstat(
    estat: Estat = Estat(
        id = "id",
        nom = "Nom",
        colorFons = "#FF444444",
        colorText = "#FFBBBBBB"
    ),
    eliminaEstat: (String) -> Unit={},
    actualizaEstat: (Estat) -> Unit={}
)
{
    Row(
        modifier=Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color= Color(estat.colorFons.toColorInt()))
    ){

        Text(
            text = estat.nom,
            modifier = Modifier.padding(8.dp)
                .weight(1F),
            style = MaterialTheme.typography.displayMedium,
            color = Color(estat.colorText.toColorInt())
        )
        IconButton(
            onClick = {eliminaEstat(estat.id)},
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Filled.Delete, "Elimina", tint = MaterialTheme.colorScheme.onError)
        }
        IconButton(
            onClick = { },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            Icon(Icons.Filled.Edit, "Modifica", tint = MaterialTheme.colorScheme.onSecondary)
        }
    }
}


@Composable
fun DialegAfegeixEstat(onAfegeixEstat: (Estat) -> Unit, onDialogDismissed: () -> Unit) {
    var nom by remember { mutableStateOf("") }
    var colorText by remember { mutableStateOf("#FF000000") }
    var colorFons by remember { mutableStateOf("#FFFFFFFF") }

    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "Afegeix un estat nou") },
        confirmButton = {
            if (nom.isNotEmpty()) {
                Button(
                    onClick = {
                            val estatNou = Estat(
                                nom = nom,
                                colorText = colorText,
                                colorFons = colorFons
                            )
                            onAfegeixEstat(estatNou)
                            nom = ""
                            colorText = "#FF000000"
                            colorFons = "#FFFFFFFF"
                            onDialogDismissed()
                        }

                ) {
                    Text(text = "Afegeix")
                }
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDialogDismissed()
                }
            ) {
                Text(text = "Cancel·la")
            }
        },
        text = {
            Column {
                TextField(
                    value = nom,
                    onValueChange = { nom = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Nom de l'estat") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    value = colorFons,
                    onValueChange = { colorFons = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Ascii),
                    maxLines = 1,
                    label = { Text(text = "Color del fons") }
                )

                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    value = colorText,
                    onValueChange = { colorText = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Ascii),
                    maxLines = 1,
                    label = { Text(text = "Color del text") }
                )
            }
        }
    )
}




