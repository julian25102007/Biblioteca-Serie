package com.example.bibliotecadeseries.data.session

interface SessionStorage {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clear()
}