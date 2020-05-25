package com.maskjs.korona_zakupy.data.users.api_communication

data class LoginResponseDto(
    val userId: String,
    val token: String,
    val roleName: String
) {

}