package cat.institutmontilivi.tasquesfirebase.cloudstorage

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class ManegadorCloudStorage() {
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    val PATH_FOTOS = "Fotos"
    val PATH_VIDEOS = "Videos"
    val PATH_AUDIOS = "Audios"

    private fun getStorageReferencePerTascaITipusArxiu(idTasca:String, tipusArxiu:String): StorageReference {
        //return storageRef.child(PATH_FOTOS).child(idUsuariActual ?: "")
        return storageRef.child(idTasca).child(tipusArxiu)
    }
    private fun getStorageReferencePerTasca(idTasca:String): StorageReference {
        return storageRef.child(idTasca)
    }
    suspend fun carregaArxiu(idTasca:String, tipusArxiu:String,nomArxiu: String, pathArxiu: Uri):String {
        val fileRef = getStorageReferencePerTascaITipusArxiu(idTasca, tipusArxiu).child(nomArxiu)
        val tascaDeCarrega = fileRef.putFile(pathArxiu)
        tascaDeCarrega.await()
        return fileRef.downloadUrl.await().toString()
    }

    suspend fun obtenLlistaImatgesPerTascaITipusArxiu(idTasca: String, tipusArxiu: String): List<String> {
        val urlsImatges = mutableListOf<String>()
        val llistaDeResultats: ListResult = getStorageReferencePerTascaITipusArxiu(idTasca, tipusArxiu).listAll().await()
        for (element in llistaDeResultats.items) {
            val url = element.downloadUrl.await().toString()
            urlsImatges.add(url)
        }
        return urlsImatges
    }

}