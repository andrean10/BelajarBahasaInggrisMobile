package com.tribuanabagus.belajarbahasainggris.local_db

data class User(
    var id: Int? = null,
    var nama: String? = null,
    var email: String? = null,
    var role: Int? = null,
    var roleName: String? = null,
    var password: String? = null,
    var gambar: String? = null
)