package com.example.myfirstapp.Model

data class Log(var title: String, var value: Double, var unit: String){

    var id = 0
    var log_date = ""
    var selected = false

    constructor() : this("",0.0, "KG")


}