package com.maskjs.korona_zakupy.data.users

data class RegisterUserDto(
    var username:String ,
    var email: String ,
    var password: String ,
    var confirmPassword: String,
    var address: String,
    var firstName: String,
    var lastName: String,
    var roleName: String
) {}