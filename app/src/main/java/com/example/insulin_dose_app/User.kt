package com.example.insulin_dose_app

class User() {

    var uid: String? = null
    var full_name: String? = null
    var email: String? = null
    var phone: Int? = null
    var dateOfBirth: String? = null
    var gender: String? = null
    var height: String? = null
    var weight: String? = null
    var dateOfDiagnosis: String? = null
    var diabet: String? = null

    constructor(uid: String, full_name: String, email: String, phone: Int) : this() {
        this.uid = uid
        this.full_name = full_name
        this.email = email
        this.phone = phone
    }

    constructor(
        uid: String, full_name: String, email: String, phone: Int,
        dateOfBirth: String?, gender: String?, height: String?, weight: String?,
        dateOfDiagnosis: String?, diabet: String?
    ) : this(uid, full_name, email, phone) {
        this.dateOfBirth = dateOfBirth
        this.gender = gender
        this.height = height
        this.weight = weight
        this.dateOfDiagnosis = dateOfDiagnosis
        this.diabet = diabet
    }
}