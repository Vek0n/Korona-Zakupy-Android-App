package com.maskjs.korona_zakupy.data.users

data class LoginResponseDto(
    val userId: String,
    val token: String,
    val roleName: String
) {

}