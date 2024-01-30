package me.topilov.morningstar.utils

class View {

    interface GuestUser
    interface AuthenticatedUser : GuestUser
    interface Admin : AuthenticatedUser

}

