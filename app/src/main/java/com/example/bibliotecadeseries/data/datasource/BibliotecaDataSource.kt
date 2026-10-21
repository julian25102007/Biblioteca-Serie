package com.example.bibliotecadeseries.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.bibliotecadeseries.data.model.Serie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val SERIES_INICIALES = listOf(
 Serie(1, "Breaking Bad", "Drama", 5, 5),
 Serie(2, "Stranger Things", "Ciencia ficción", 4, 2),
 Serie(3, "The Office", "Comedia", 9, 0),
 Serie(4, "Dark", "Misterio", 3, 0),
 Serie(5, "Black Mirror", "Ciencia ficción", 6, 1)
)

private val Context.bibliotecaDataStore by preferencesDataStore(name = "biblioteca_prefs")

class BibliotecaDataSource(private val context: Context) {

 private val claveSeries = stringPreferencesKey("series_json")

 val series: Flow<List<Serie>> = context.bibliotecaDataStore.data.map { preferencias ->
  preferencias[claveSeries]?.let(::desdeJson) ?: SERIES_INICIALES
 }

 suspend fun guardar(lista: List<Serie>) {
  context.bibliotecaDataStore.edit { it[claveSeries] = aJson(lista) }
 }

 private fun aJson(lista: List<Serie>): String {
  val arreglo = JSONArray()
  lista.forEach { serie ->
   arreglo.put(
    JSONObject().apply {
     put("id", serie.id)
     put("titulo", serie.titulo)
     put("genero", serie.genero)
     put("total", serie.temporadasTotales)
     put("vistas", serie.temporadasVistas)
    }
   )
  }
  return arreglo.toString()
 }

 private fun desdeJson(texto: String): List<Serie> {
  val arreglo = JSONArray(texto)
  return List(arreglo.length()) { i ->
   val objeto = arreglo.getJSONObject(i)
   Serie(
    id = objeto.getLong("id"),
    titulo = objeto.getString("titulo"),
    genero = objeto.getString("genero"),
    temporadasTotales = objeto.getInt("total"),
    temporadasVistas = objeto.getInt("vistas")
   )
  }
 }
}