package com.bmamaral.learning_logic.config

import com.bmamaral.learning_logic.services.app.UsersService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

class CustomUserDetails(
    private val aUsername: String,
    private val aPassword: String,
    private val someAuthorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = someAuthorities

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = aUsername

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = aPassword

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}

@Service
class CustomUserDetailsService(
    val usersService: UsersService
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        username?.let {
            val userOptional = usersService.getUserOptional(username)
            if (userOptional.isPresent) {
                return CustomUserDetails(
                    userOptional.get().username,
                    userOptional.get().password,
                    usersService.getAuthorities(userOptional.get().username)
                )
            } else
                throw UsernameNotFoundException(username)
        }

        throw UsernameNotFoundException("Username not found.")
    }

}