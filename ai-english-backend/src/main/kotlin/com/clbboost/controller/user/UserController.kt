package com.clbboost.controller.user

import com.clbboost.auth.model.UpdateUserRequest
import com.clbboost.auth.model.UserPrincipal
import com.clbboost.domain.user.User
import com.clbboost.service.user.UserService
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
