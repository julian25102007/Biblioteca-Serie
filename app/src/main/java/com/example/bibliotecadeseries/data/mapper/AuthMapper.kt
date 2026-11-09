package com.example.bibliotecadeseries.data.mapper

import com.example.bibliotecadeseries.data.dto.UserDto
import com.example.bibliotecadeseries.domain.model.User

fun UserDto.toDomain(): User = User(id = id, nombre = nombre, correo = correo)