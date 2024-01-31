package me.topilov.morningstar.utils

class View {

    interface Anonymous
    interface User : Anonymous
    interface AuthenticatedUser : User
    interface Admin : AuthenticatedUser

}

