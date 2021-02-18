package com.thaisbl.desafio4.shared.model.data

sealed class Response {
    class Success(val data: Any?) : Response()
    class Failure(val error: String?) : Response()
}