package com.jc4balos.user_service.mapper.user_mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.jc4balos.user_service.dto.request.user.ModifyUserInfoDto;
import com.jc4balos.user_service.dto.request.user.NewUserDto;
import com.jc4balos.user_service.dto.response.user.UserCredentialsDto;
import com.jc4balos.user_service.dto.response.user.ViewUserDto;
import com.jc4balos.user_service.model.User;

@Component
public class UserMapper {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // TODO: use builder for better readability

    public User newUserDto(NewUserDto newUserDto) {
        User user = new User();
        user.setFirstName(newUserDto.getFirstName());
        user.setMotherSurname(newUserDto.getMotherSurname());
        user.setFatherSurname(newUserDto.getFatherSurname());
        user.setHusbandSurname(newUserDto.getHusbandSurname());
        user.setBirthDate(newUserDto.getBirthDate());
        user.setSex(newUserDto.getSex());
        user.setUsername(newUserDto.getUsername());
        user.setContactNumber(newUserDto.getContactNumber());
        user.setAddressLine1(newUserDto.getAddressLine1());
        user.setAddressLine2(newUserDto.getAddressLine2());
        user.setAddressLine3(newUserDto.getAddressLine3());
        user.setEmail(newUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        user.setIsActive(true);
        return user;
    }

    public ViewUserDto viewUserDto(User user) {
        ViewUserDto viewUserDto = new ViewUserDto();
        viewUserDto.setAddressLine1(user.getAddressLine1());
        viewUserDto.setAddressLine2(user.getAddressLine2());
        viewUserDto.setAddressLine3(user.getAddressLine3());
        viewUserDto.setBirthDate(user.getBirthDate());
        viewUserDto.setContactNumber(user.getContactNumber());
        viewUserDto.setEmail(user.getEmail());
        viewUserDto.setFatherSurname(user.getFatherSurname());
        viewUserDto.setFirstName(user.getFirstName());
        viewUserDto.setHusbandSurname(user.getHusbandSurname());
        viewUserDto.setMotherSurname(user.getMotherSurname());
        viewUserDto.setSex(user.getSex());
        viewUserDto.setUserUUID(user.getUserUUID());
        viewUserDto.setUsername(user.getUsername());
        return viewUserDto;
    }

    public User modifyUserInfoDto(ModifyUserInfoDto modifyUserDto, User currentUser) {
        currentUser.setAddressLine1(modifyUserDto.getAddressLine1());
        currentUser.setAddressLine2(modifyUserDto.getAddressLine2());
        currentUser.setAddressLine3(modifyUserDto.getAddressLine3());
        currentUser.setBirthDate(modifyUserDto.getBirthDate());
        currentUser.setFatherSurname(modifyUserDto.getFatherSurname());
        currentUser.setFirstName(modifyUserDto.getFirstName());
        currentUser.setMotherSurname(modifyUserDto.getMotherSurname());
        currentUser.setHusbandSurname(modifyUserDto.getHusbandSurname());
        currentUser.setSex(modifyUserDto.getSex());
        return currentUser;
    }

    public UserCredentialsDto userCredentialsDto(User user) {
        UserCredentialsDto userCredentialsDto = UserCredentialsDto.builder().email(user.getEmail())
                .isActive(user.getIsActive()).password(user.getPassword()).userUUID(user.getUserUUID())
                .username(user.getUsername()).build();
        return userCredentialsDto;
    }

}
