package cat.institutmontilivi.tasquesfirebase.dades.sensors

import android.content.Context
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

//https://developer.android.com/reference/androidx/core/content/FileProvider
//Cal demanar els permisos de camera en el manifest:
/*
<uses-permission android:name="android.permission.CAMERA"/>
<uses-feature android:name="android.hardware.camera.any"/>
 */
//Cal crear el provider dins de l'apartart Application del Manifest
/*
        <provider
        android:authorities="cat.institutmontivi.tasquesfirebase.proveidor"
        android:name="androidx.core.content.FileProvider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/paths_fitxers"/>
    </provider>
 */
 // Cal crear als recursos el fitxer paths_fitxers.xml
/*
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-cache-path
        name="multimedia"
        path="/"/>
</paths>
 */

// Ens farà falta la funció que hi ha a Fitxers
/*
enum class TipusMultimedia {
    IMATGE,
    VIDEO,
    AUDIO}


/**
 * Crea un fitxer buit, sense contingut
 */
fun Context.creaUnFitxerMultimedia(format: TipusMultimedia): File {
    val moment = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var nomFitxerMultimedia = ""

    if (format == TipusMultimedia.IMATGE){
        nomFitxerMultimedia = "JPEG_" + moment + "_"
        return java.io.File.createTempFile(
            nomFitxerMultimedia,
            ".jpg",
            externalCacheDir
        )
    }
    else if (format == TipusMultimedia.VIDEO){
        nomFitxerMultimedia = "MPEG_" + moment + "_"
        return java.io.File.createTempFile(
            nomFitxerMultimedia,
            ".mp4",
            externalCacheDir
        )
    }
    else // if (format == TipusMultimedia.AUDIO){
        nomFitxerMultimedia = "MP3_" + moment + "_"
    return java.io.File.createTempFile(
        nomFitxerMultimedia,
        ".mp3",
        externalCacheDir
    )
}
 */



