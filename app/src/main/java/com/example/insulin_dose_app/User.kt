
package com.example.insulin_dose_app

class User(var uid:String, var full_name:String, var email:String, var phone:Int) {
    constructor(uid:String, full_name:String, email:String, phone:Int,
                dateOfBirth:String?, gender:String? , height:Int? ,  weight:Int?,
                dateOfDiagnosis:String? , diabet:String?) : this(uid, full_name, email, phone)
}