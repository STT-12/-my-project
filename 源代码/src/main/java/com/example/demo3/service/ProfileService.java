package com.example.demo3.service;

import com.example.demo3.entity.Address;
import com.example.demo3.mapper.AddressMapper;
import com.example.demo3.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AddressMapper addressMapper;
    private final UserMapper userMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // --- 地址管理 ---
    public List<Address> getAddresses(Integer userId) {
        return addressMapper.findByUserId(userId);
    }

    @Transactional
    public Address addAddress(Address address) {
        if (address.getIsDefault()) {
            addressMapper.clearDefault(address.getUserId());
        }
        addressMapper.insert(address);
        return address;
    }

    @Transactional
    public Address updateAddress(Address address) {
        if (address.getIsDefault()) {
            addressMapper.clearDefault(address.getUserId());
        }
        addressMapper.update(address);
        return address;
    }

    public void deleteAddress(Integer addressId, Integer userId) {
        addressMapper.deleteById(addressId, userId);
    }

    @Transactional
    public void setDefaultAddress(Integer userId, Integer addressId) {
        addressMapper.clearDefault(userId);
        addressMapper.setDefault(addressId, userId);
    }

    // --- 头像上传 ---
    public String updateAvatar(Integer userId, String base64Data) throws IOException {
        if (!StringUtils.hasText(base64Data)) {
            throw new IllegalArgumentException("头像数据不能为空");
        }

        String[] parts = base64Data.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Base64数据格式错误");
        }

        String imageString = parts[1];
        String header = parts[0]; // e.g., "data:image/png;base64"
        String extension = header.substring(header.indexOf('/') + 1, header.indexOf(';'));

        byte[] imageBytes = Base64.getDecoder().decode(imageString);
        String filename = UUID.randomUUID().toString() + "." + extension;

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, filename);

        try (OutputStream stream = new FileOutputStream(file)) {
            stream.write(imageBytes);
        }

        String avatarUrl = "/uploads/" + filename;
        userMapper.updateAvatar(userId, avatarUrl);

        return avatarUrl;
    }
}