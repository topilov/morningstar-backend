package me.topilov.morningstar.mapper

import me.topilov.morningstar.dto.auth.RegisterDto
import me.topilov.morningstar.dto.user.CreateUserDto
import me.topilov.morningstar.dto.user.UpdateUserDto
import me.topilov.morningstar.dto.user.response.GetUserResponse
import me.topilov.morningstar.entity.User
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toUser(createUserDto: CreateUserDto): User
    fun toUser(updateUserDto: UpdateUserDto): User
    fun toUser(getUserResponse: GetUserResponse): User
    fun toCreateUserDto(registerDto: RegisterDto): CreateUserDto
    fun toGetUserResponse(user: User): GetUserResponse
}