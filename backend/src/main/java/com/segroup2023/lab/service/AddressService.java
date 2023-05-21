package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.Address;
import com.segroup2023.lab.database.repository.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AddressService {
    private static AddressRepository addressRepository;
    @Autowired
    private AddressService(AddressRepository addressRepository) {
        AddressService.addressRepository = addressRepository;
    }

    public static void createAddress(Long userId, String name, String phone, String address) {
        Address addressEntity = new Address(userId, name, phone, address);
        addressRepository.save(addressEntity);
    }

    public static List<Address> getAddresses(Long userId) {
        return addressRepository.findByUserAndValidTrue(userId);
    }

    public static Address getAddress(Long addressId) {
        Optional<Address> optional = addressRepository.findById(addressId);
        assert optional.isPresent():"Address does not exist";
        return optional.get();
    }

    public static void modifyAddress(Long addressId, String name, String phone, String address) {
        Address addressEntity = getAddress(addressId);
        addressEntity.setName(name);
        addressEntity.setPhone(phone);
        addressEntity.setAddress(address);
        addressRepository.save(addressEntity);
    }

    public static void deleteAddress(Long addressId) {
        Address address = getAddress(addressId);
        address.setValid(false);
        addressRepository.save(address);
    }

}
