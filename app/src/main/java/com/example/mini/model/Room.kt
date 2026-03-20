package com.example.mini.model

data class Room(
    var id: String,
    var name: String,
    var price: Double,
    var isRented: Boolean, // true = Rented, false = Empty
    var tenantName: String = "",
    var tenantPhone: String = ""
)

