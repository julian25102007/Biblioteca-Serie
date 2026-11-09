package com.example.bibliotecadeseries.ui.navigation

object Routes {
    const val ARG_ID = "id"
    const val NEW_ID = -1

    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val DETAIL = "detail/{$ARG_ID}"
    const val FORM = "form?$ARG_ID={$ARG_ID}"

    fun detail(id: Int) = "detail/$id"
    fun form(id: Int? = null) = if (id == null) "form" else "form?$ARG_ID=$id"
}