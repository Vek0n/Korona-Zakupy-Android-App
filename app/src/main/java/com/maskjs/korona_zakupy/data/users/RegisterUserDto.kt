package com.maskjs.korona_zakupy.data.users

data class RegisterUserDto(
    val username:String ,
    val email: String ,
    val password: String ,
    val confirmPassword: String,
    val address: String,
    val firstName: String,
    val lastName: String,
    val roleName: String
) {}