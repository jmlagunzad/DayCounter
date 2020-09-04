package com.example.myfirstapp

data class Wish(var name: String, var price: Double, var curr: String){

    var id = 0
    constructor(): this("", 0.00, "PHP")
}