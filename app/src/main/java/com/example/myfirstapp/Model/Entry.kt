package com.example.myfirstapp.Model

data class Entry(var title: String, var amount: Double){

    var id = 0

    constructor() : this("",0.0)


}