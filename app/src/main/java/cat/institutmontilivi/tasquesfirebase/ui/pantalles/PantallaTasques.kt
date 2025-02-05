package cat.institutmontilivi.tasquesfirebase.ui.pantalles
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.institutmontilivi.tasquesfirebase.model.app.Estat
import cat.institutmontilivi.tasquesfirebase.model.app.Tasca
import cat.institutmontilivi.tasquesfirebase.ui.viewmodels.ViewModelTasques
import java.time.LocalDate

@Composable
fun PantallaTasques(viewModel: ViewModelTasques = viewModel())
{
    val estat = viewModel.tasques.collectAsState()
    var mostraDialegAfegeixTasca by remember { mutableStateOf(false) }
    var mostraDialegActualitzaTasca by remember { mutableStateOf(false) }
    var tascaEditada = Tasca()

    val afegeixTasca = { tasca: Tasca -> viewModel.afegeixTasca(tasca) }
    val eliminaTasca = { id: String -> viewModel.eliminaTasca(id) }
    val actualizaTasca = { tasca: Tasca -> viewModel.actualitzaTasca(tasca) }
    val editaTasca = { tasca: Tasca ->
        tascaEditada = tasca
        mostraDialegActualitzaTasca = true
    }

    val onDialogDismissed = {
        mostraDialegAfegeixTasca = false
        mostraDialegActualitzaTasca = false
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                mostraDialegAfegeixTasca = true
            })
            { Icon(Icons.Filled.Add, "Floating action button.") }
            if (mostraDialegAfegeixTasca){
                DialegAfegeixTasca(estat.value.usuariActual, estat.value.estats,afegeixTasca, onDialogDismissed)
            }
            if (mostraDialegActualitzaTasca){
                DialegActualitzaTasca (estat.value.usuariActual, estat.value.estats, tascaEditada, actualizaTasca, onDialogDismissed)
            }
        }
    )
    {padding->
        LazyColumn (modifier = Modifier.padding(padding))
        {
            items(estat.value.tasques)
            {
                ElementTasca(it, eliminaTasca, editaTasca, estat.value.estats)
            }
        }
    }
}

@Preview
@Composable
fun ElementTasca(
    tasca: Tasca = Tasca(),
    eliminaTasca: (String) -> Unit = {},
    editaTasca: (Tasca) -> Unit = {},
    estats: Map<String, Estat> =mapOf(),
)
{
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color= Color((estats[tasca.estat]?.colorFons?:"#FF000000").toColorInt()))
    ){

        Text(
            text = tasca.titol,
            modifier = Modifier.padding(8.dp)
                .weight(1F),
            style = MaterialTheme.typography.displayMedium,
            color = Color((estats[tasca.estat]?.colorText?:"#FFFFFFFF").toColorInt())
        )
        IconButton(
            onClick = {eliminaTasca(tasca.id)},
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Filled.Delete, "Elimina", tint = MaterialTheme.colorScheme.onError)
        }
        IconButton(
            onClick = { editaTasca(tasca)},
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialegAfegeixTasca(
    usuariActual: String,
    estats: Map<String, Estat>,
    afegeixTasca: (Tasca) -> Unit,
    onDialogDismissed: () -> Unit
) {
    var titol by remember { mutableStateOf("") }
    var descripcio by remember { mutableStateOf("") }
    var dataLimit by remember { mutableStateOf(LocalDate.now().toEpochDay()) }
    val estatDatePicker = rememberDatePickerState()
    var desplegat by remember { mutableStateOf(false) }
    var estat by remember { mutableStateOf(estats.keys.first()) }


    AlertDialog(
        onDismissRequest = onDialogDismissed,
        title = { Text(text = "Afegeix una tasca nova") },
        confirmButton = {
            if (titol.isNotEmpty()) {
                Button(
                    onClick = {
                        val tascaNova = Tasca(
                            titol = titol,
                            descripcio = descripcio,
                            dataLimit = dataLimit,
                            estat = estat,
                            usuaris = listOf(usuariActual)
                        )
                        afegeixTasca(tascaNova)
                        titol = ""
                        descripcio = ""
                        dataLimit = estatDatePicker.selectedDateMillis?:LocalDate.now().toEpochDay()
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
                    value = titol,
                    onValueChange = { titol = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    maxLines = 1,
                    label = { Text(text = "Títol de la tasca") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = descripcio,
                    onValueChange = { descripcio = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    maxLines = 8,
                    label = { Text(text = "Descripció de la tasca") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box( modifier = Modifier.fillMaxWidth()
                    .wrapContentSize(Alignment.TopEnd)
                ) {
                    Row {
                        Text(
                            text = estats[estat]!!.nom,
                            color = Color(estats[estat]!!.colorText.toColorInt()),
                            modifier = Modifier
                                .background(Color(estats[estat]!!.colorFons.toColorInt()))
                                .padding(8.dp),
                            style = MaterialTheme.typography.headlineLarge)

                        IconButton(onClick = { desplegat = !desplegat }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More"
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = desplegat,
                        onDismissRequest = { desplegat = false }
                    ) {
                        estats.forEach {
                            DropdownMenuItem(
                                text = { Text(it.value.nom) },
                                onClick = { estat = it.key; desplegat = false }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                DatePicker(
                    state = estatDatePicker,
                    title = { Text(text = "Data límit de la tasca") },
                    headline = {
                        Text("Data")
                    },
                    showModeToggle = true
                )

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialegActualitzaTasca(usuariActual: String,
                          estats: Map<String, Estat>,
                          tasca: Tasca,
                          actualizaTasca: (Tasca) -> Unit,
                          onDialogDismissed: () -> Unit
) {
    var titol by remember { mutableStateOf(tasca.titol) }
    var descripcio by remember { mutableStateOf(tasca.descripcio) }
    var dataLimit by remember { mutableStateOf(tasca.dataLimit) }
    val estatDatePicker = rememberDatePickerState()
    var desplegat by remember { mutableStateOf(false) }
    var estat by remember { mutableStateOf(tasca.estat) }


    AlertDialog(
        onDismissRequest = onDialogDismissed,
        title = { Text(text = "Actualitza la tasca") },
        confirmButton = {
            if (titol.isNotEmpty()) {
                Button(
                    onClick = {
                        val tascaNova = Tasca(
                            id=tasca.id,
                            titol = titol,
                            descripcio = descripcio,
                            dataLimit = dataLimit,
                            estat = estat,
                            usuaris = listOf(usuariActual)
                        )
                        actualizaTasca(tascaNova)
                        titol = ""
                        descripcio = ""
                        dataLimit = estatDatePicker.selectedDateMillis?:LocalDate.now().toEpochDay()
                        onDialogDismissed()
                    }
                ) {
                    Text(text = "Actualiza")
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
                    value = titol,
                    onValueChange = { titol = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    maxLines = 1,
                    label = { Text(text = "Títol de la tasca") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = descripcio,
                    onValueChange = { descripcio = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    maxLines = 8,
                    label = { Text(text = "Descripció de la tasca") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box( modifier = Modifier.fillMaxWidth()
                    .wrapContentSize(Alignment.TopEnd)
                ) {
                    Row {
                        Text(
                            text = estats[estat]!!.nom,
                            color = Color(estats[estat]!!.colorText.toColorInt()),
                            modifier = Modifier
                                .background(Color(estats[estat]!!.colorFons.toColorInt()))
                                .padding(8.dp),
                            style = MaterialTheme.typography.headlineLarge)

                        IconButton(onClick = { desplegat = !desplegat }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More"
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = desplegat,
                        onDismissRequest = { desplegat = false }
                    ) {
                        estats.forEach {
                            DropdownMenuItem(
                                text = { Text(it.value.nom) },
                                onClick = { estat = it.key; desplegat = false }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                DatePicker(
                    state = estatDatePicker,
                    title = { Text(text = "Data límit de la tasca") },
                    headline = {
                        Text("Data")
                    },
                    showModeToggle = true
                )
            }
        }
    )
}
