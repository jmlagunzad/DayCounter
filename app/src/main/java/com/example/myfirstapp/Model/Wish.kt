package com.example.myfirstapp.Model

data class Wish(var name: String, var price: Double, var curr: String, var deadline: String){

    var id = 0
    //var deadline = ""
    constructor(): this("", 0.00, "PHP", "")
    //constructor(): this("", 0.00, "PHP", "")
}