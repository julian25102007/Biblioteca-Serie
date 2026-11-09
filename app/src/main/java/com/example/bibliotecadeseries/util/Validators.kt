package com.example.bibliotecadeseries.util

const val MIN_PASSWORD_LENGTH = 8

private val EMAIL_REGEX = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

fun String.isValidEmail(): Boolean = EMAIL_REGEX.matches(trim())

fun String.isValidPassword(): Boolean = length >= MIN_PASSWORD_LENGTH