package me.topilov.morningstar.exception

data class ErrorResponse(
    val errors: List<String?>
)