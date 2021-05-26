package com.example.myfirstapp.Model

data class Wish(var name: String, var price: Double, var curr: String, var deadline: String){

    var id = 0
    constructor(): this("", 0.00, "PHP", "")
    var date = deadline.split("-")

    fun getYear() : Int{
        return date[0].toInt()
    }

    fun getMonth() : Int{
        return date[1].toInt()
    }

    fun getDay() : Int{
        return date[2].toInt()
    }
}