package com.example.myfirstapp.Model

data class Entry(var title: String, var description: String?){

    var id = 0

    constructor() : this("","")


}