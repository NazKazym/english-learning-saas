package com.englishprep.auth.controller

import com.englishprep.auth.model.UpdateUserRequest
import com.englishprep.auth.model.UserPrincipal
import com.englishprep.domain.user.User
import com.englishprep.domain.user.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal user: UserPrincipal): User {
        return userService.getById(user.id)
    }

    @PutMapping("/me")
    fun updateProfile(@AuthenticationPrincipal user: UserPrincipal, @RequestBody update: UpdateUserRequest): User {
        return userService.update(user.id, update)
    }
}
