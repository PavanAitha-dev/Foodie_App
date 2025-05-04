package com.improveid.User.service;


import com.improveid.User.dto.AddressDto;
import com.improveid.User.dto.LoginRequest;
import com.improveid.User.dto.RegisterRequest;
import com.improveid.User.dto.UserProfileDto;
import com.improveid.User.exception.BadRequestException;
import com.improveid.User.entity.*;
import com.improveid.User.repository.AddressRepository;
import com.improveid.User.repository.LoginRepository;
import com.improveid.User.repository.RoleRepository;
import com.improveid.User.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AuthService {

    private final LoginRepository loginRepo;
    private final RoleRepository roleRepo;
    private final UserProfileRepository userRepo;
    private final AddressRepository addressRepo;

    @Transactional
    public UserProfile register(RegisterRequest req) {
        if (loginRepo.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Login login = new Login();
        login.setUsername(req.getUsername());
        login.setPassword(req.getPassword());
        Role role = roleRepo.findByRoleName(req.getRole())
              .orElseThrow(() -> new RuntimeException("Role not found"));
        loginRepo.save(login);
        UserProfile user = new UserProfile();
        user.setLogin(login);
        user.setRole(role);
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        userRepo.save(user);
        return user;
    }

    public UserProfileDto login(LoginRequest req) {
        Optional<Login> loginOpt = loginRepo.findByUsername(req.getUsername());

        if (loginOpt.isEmpty()) {
            throw new BadRequestException("Invalid username");
        }
        else if (!(loginOpt.get().getPassword().equals(req.getPassword()))) {
            throw new BadRequestException("Invalid password");
        }
            Optional<UserProfile>userProfile=userRepo.findByLoginId(loginOpt.get().getId());
        UserProfileDto userProfileDto=new UserProfileDto();
        userProfileDto.setUserID(userProfile.get().getUserId());
        userProfileDto.setUsername(userProfile.get().getLogin().getUsername());
        userProfileDto.setFullName(userProfile.get().getFullName());
        userProfileDto.setEmail(userProfile.get().getEmail());
        userProfileDto.setRoleID(userProfile.get().getRole().getRoleId());

        return userProfileDto;
    }
    public List<UserProfileDto> getAllUsers() {
        List<UserProfile> userProfiles=userRepo.findAll();
        List<UserProfileDto> users =new ArrayList<>();
        for (UserProfile userProfile:userProfiles){
            UserProfileDto userProfileDto=new UserProfileDto();
            userProfileDto.setUserID(userProfile.getUserId());
            userProfileDto.setUsername(userProfile.getLogin().getUsername());
            userProfileDto.setFullName(userProfile.getFullName());
            userProfileDto.setEmail(userProfile.getEmail());
            userProfileDto.setRoleID(userProfile.getRole().getRoleId());
            users.add(userProfileDto);
        }
        return users;
    }
    public void deleteUser(Long ID) {
       userRepo.deleteById(ID);
    }
    public UserProfileDto getUser(Long id) {
        Optional<UserProfile> user=userRepo.findById(id);
        UserProfileDto userDto=new UserProfileDto();
        userDto.setUserID(user.get().getUserId());
        userDto.setEmail(user.get().getEmail());
        userDto.setFullName(user.get().getFullName());
        userDto.setRoleID(user.get().getRole().getRoleId());
        userDto.setUsername(user.get().getLogin().getUsername());
        return userDto;
    }

    public Map<Long,String> getIdName() {
        List<UserProfile> userProfiles=userRepo.findAll();
        Map<Long,String> data=new HashMap<>();
        for (UserProfile userProfile:userProfiles){
            data.put(userProfile.getUserId(),userProfile.getFullName());
        }
        return data;

    }

    public Address addAddress(AddressDto request) {
        Optional<UserProfile> user=userRepo.findById(request.getUserId());
        Address address= Address.builder()
                .address(request.getAddress())
                .addressType(AddressType.valueOf(request.getAddressType()))
                .landMark(request.getLandMark())
                .pincode(request.getPincode())
                .user(user.get())
                .build();
        Address result=addressRepo.save(address);
        return result;
    }
    public Object getAllAddressesById(Long id) {
        Optional<UserProfile> user=userRepo.findById(id);
        List<Address> list=addressRepo.findByUser(user.get());
        List<AddressDto> addressDtoList=new ArrayList<>();
        for(Address address:list){
            AddressDto addressDto=new AddressDto();
            addressDto.setAddress(address.getAddress());
            addressDto.setPincode(address.getPincode());
            addressDto.setLandMark(address.getLandMark());
            addressDto.setAddressType(address.getAddressType().toString());
            addressDto.setId(address.getId());
            addressDto.setUserId(address.getUser().getUserId());
            addressDtoList.add(addressDto);
        }
        return addressDtoList;
    }

    public Object updateAddress(AddressDto request, Long id) {
        Optional<UserProfile> user=userRepo.findById(request.getUserId());
        Address address= Address.builder()
                .address(request.getAddress())
                .addressType(AddressType.valueOf(request.getAddressType()))
                .landMark(request.getLandMark())
                .pincode(request.getPincode())
                .user(user.get())
                .id(id)
                .build();
        Address result=addressRepo.save(address);
        return result;
    }

    public void deleteAddress(Long id) {
        addressRepo.deleteById(id);
    }
}
