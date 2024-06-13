package com.application.paymate

class MatesInfo {
    var name: String? = null
    var phone: String? = null
    var mate_id:String? = null
    var rent_amount:String? = null
    var isExpanded: Boolean = false
    // Add a no-argument constructor
    constructor() {}

    constructor(name :String,phoneNumber:String,id:String,rentAmount:String) {
        this.name = name
        this.phone = phoneNumber
        this.mate_id = id
        this.rent_amount = rentAmount
    }
}