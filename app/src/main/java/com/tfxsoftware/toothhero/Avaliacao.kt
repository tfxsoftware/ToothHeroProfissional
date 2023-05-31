package com.tfxsoftware.toothhero

data class Avaliacao(val aid:String, val dentistaId:String, val socorristaId: String,
                     val atendimentoId:String, val nomeSocorrista:String, val dataHora:String,
                     val comentario: String, val nota:Int)
