package com.example.groceryapp

class Product {
    var name: String? = null
    var price: String? = null
    var quantity: String? = null
    var itemId:String? = null
    var listName:String? = null

    constructor(){}
    constructor(name: String?, price: String?, quantity: String?) {
        this.name = name
        this.price = price
        this.quantity = quantity
    }
    constructor(itemId:String?){
        this.itemId = itemId
    }
}



