package cat.institutmontilivi.tasquesfirebase.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import cat.institutmontilivi.tasquesfirebase.dades.BBDDFactory
import cat.institutmontilivi.tasquesfirebase.model.app.Estat
import cat.institutmontilivi.tasquesfirebase.model.app.Resposta
import cat.institutmontilivi.tasquesfirebase.navegacio.DestinacioEstatAltaEdicio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelEstatAltaEdicio(savedStateHandle: SavedStateHandle): ViewModel() {
//    private var _estat = MutableStateFlow(EstatsAltaEdicioEstat())
//    val estat: StateFlow<EstatsAltaEdicioEstat> = _estat
//    val estatsRepositoriHelper = BBDDFactory.obtenRepositoriEstats(null, BBDDFactory.DatabaseType.FIREBASE)
//
//    init{
//        val args = savedStateHandle.toRoute<DestinacioEstatAltaEdicio>()
//        if (args.idEstat!=null) {
//
//            viewModelScope.launch(Dispatchers.IO) {
//                estatsRepositoriHelper.obtenEstat(args.idEstat).collect { resposta ->
//                    var estatRecuperat = Estat()
//                    if (resposta is Resposta.Exit)
//                        estatRecuperat = resposta.dades
//                    _estat.update {
//                        _estat.value.copy(
//                            id = args.idEstat,
//                            nom = estatRecuperat.nom,
//                            colorFons = estatRecuperat.colorFons,
//                            colorText = estatRecuperat.colorText
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    fun actualitzaEstat()
//    {
//        viewModelScope.launch (Dispatchers.IO){
//            estatsRepositoriHelper.actualitzaEstat(_estat.value.toEstat())
//        }
//    }
//
//    fun afegeixEstat()
//    {
//        viewModelScope.launch (Dispatchers.IO){
//            estatsRepositoriHelper.afegeixEstat (_estat.value.toEstat())
//        }
//    }
//
//    data class EstatsAltaEdicioEstat(
//        val id:String="",
//        val nom:String = "",
//        val colorText:String = "",
//        val colorFons:String = ""
//    )
//
//    fun EstatsAltaEdicioEstat.toEstat():Estat
//    {
//        return Estat(
//            id = id,
//            nom = nom,
//            colorFons = colorFons,
//            colorText = colorText
//        )
//    }
}