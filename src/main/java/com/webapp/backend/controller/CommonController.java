package com.webapp.backend.controller;

import com.webapp.backend.core.dto.RoleDto;
import com.webapp.backend.core.dto.UserDto;
import com.webapp.backend.core.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/common")
public class CommonController {

    @GetMapping("/getCurrentUser")
    public ResponseEntity<UserDto> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = new UserDto();
        if (authentication != null && authentication.isAuthenticated()) {
            userDto.setUsername(authentication.getName());
            Object object = authentication.getPrincipal();
            User userDetails = (User) object;

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            List<RoleDto> roleDtos = authorities.stream()
                    .map(authority -> RoleDto.builder().name(authority.getAuthority()).build())
                    .collect(Collectors.toList());
            userDto.setRoles(roleDtos);

            boolean isAdmin = roleDtos.stream()
                    .anyMatch(roleDto -> "ROLE_ADMIN".equals(roleDto.getName()));

            if(isAdmin){
                userDto.setAdmin(true);
                userDto.setShipper(false);
            } else {
                userDto.setAdmin(false);
                userDto.setShipper(true);
            }

            userDto.setId(userDetails.getId());
            userDto.setFirstname(userDetails.getFirstName());
            userDto.setLastname(userDetails.getLastName());

        } else {
            return ResponseEntity.noContent().build();
        }

        return  ResponseEntity.ok(userDto);
    }
}
