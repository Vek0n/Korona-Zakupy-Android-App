package com.maskjs.korona_zakupy.data.layoutModels.interfaces

interface IInputTextLayoutModel {
    fun validate(errorsMessages: Map<String,String>, extraParameter:String ="")
}