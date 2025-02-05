package cat.institutmontilivi.tasquesfirebase.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.institutmontilivi.tasquesfirebase.dades.BBDDFactory
import cat.institutmontilivi.tasquesfirebase.model.app.Estat
import cat.institutmontilivi.tasquesfirebase.model.app.Resposta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class ViewModelEstats :ViewModel() {
    private var _estat = MutableStateFlow<EstatEstats>(EstatEstats())
    val estat: StateFlow<EstatEstats> = _estat.asStateFlow()
    val estatsRepositori = BBDDFactory.obtenRepositoriEstats(null, BBDDFactory.DatabaseType.FIREBASE)

    init{
       obtenEstats()
    }

    fun afegeixEstat(estat: Estat)
    {
        viewModelScope.launch (Dispatchers.IO){

            estatsRepositori.afegeixEstat (estat)
        }
    }



fun obtenEstats()
{
    viewModelScope.launch (Dispatchers.IO){
        _estat.emit ( _estat.value.copy(estaCarregant = true))
        estatsRepositori.obtenEstats().collect{
                resposta ->
            if(resposta is Resposta.Exit)
            {
                val dades = resposta.dades
                _estat.emit(
                    estat.value.copy(
                        estaCarregant = false,
                        estats = dades,
                        esErroni = false,
                        missatgeError = ""
                    )
                )
            }
            else if(resposta is Resposta.Fracas)
            {
                _estat.emit(
                    estat.value.copy(
                        estaCarregant = false,
                        estats = listOf(),
                        esErroni = true,
                        missatgeError = resposta.missatgeError
                    )
                )
            }
        }
    }
}
    fun eliminaEstat(id:String)
    {
        viewModelScope.launch (Dispatchers.IO){
            estatsRepositori.eliminaEstat(id)
        }
    }

    fun actualitzaEstat(estat:Estat)
    {
        viewModelScope.launch (Dispatchers.IO){
            estatsRepositori.actualitzaEstat(estat)
        }
    }

    data class EstatEstats(
        val estaCarregant:Boolean = true,
        val estats:List<Estat> = listOf(),
        val esErroni:Boolean = false,
        val missatgeError:String = ""
    )
}