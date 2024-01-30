package me.topilov.morningstar.entity

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailsImpl(
    val user: User
) : UserDetails {

    override fun getAuthorities(): List<SimpleGrantedAuthority> = listOf(SimpleGrantedAuthority(user.role))

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = !user.isLocked

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
