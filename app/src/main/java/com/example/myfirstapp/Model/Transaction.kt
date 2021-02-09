package com.example.myfirstapp.Model

data class Transaction(var title: String, var amount: Double){

    var id = 0
    var transaction_date = ""
    var active = true

    constructor() : this("",0.0)


}