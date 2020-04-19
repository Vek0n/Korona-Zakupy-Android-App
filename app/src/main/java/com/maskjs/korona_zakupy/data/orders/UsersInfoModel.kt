package com.maskjs.korona_zakupy.data.orders

data class UsersInfoModel(
    val userId:String,
    val address:String,
    val name:String,
    val firstName:String,
    val lastName:String,
    val rating: Long,
    val photoDirectory:String?
) {
}