package com.improveid.User.repository;

import com.improveid.User.entity.Address;
import com.improveid.User.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {


    List<Address> findByUser(UserProfile user);
}
