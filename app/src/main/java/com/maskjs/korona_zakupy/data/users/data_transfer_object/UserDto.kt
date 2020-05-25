package com.maskjs.korona_zakupy.data.users.data_transfer_object

data class UserDto(
    val userId:String,
    val address:String,
    val name:String,
    val firstName:String,
    val lastName:String,
    val rating: Double,
    val photoDirectory:String?,
    val userRole: String
) {}