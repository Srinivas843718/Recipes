package com.recipemanagement.serviceimpl;

import com.recipemanagement.dto.AdminDto;
import com.recipemanagement.entity.AdminEntity;
import com.recipemanagement.exception.AdminNotFoundException;
import com.recipemanagement.repository.AdminRepository;
import com.recipemanagement.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AdminDto> getAdmins() {
        List<AdminEntity> admins = adminRepository.findAll();
        return admins.stream().map(adminEntity -> modelMapper.map(adminEntity, AdminDto.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<AdminDto> getAdmin(String id) throws AdminNotFoundException {
        Optional<AdminEntity> adminEntity = adminRepository.findById(id);
        if (adminEntity.isEmpty()) {
            throw new AdminNotFoundException("Admin not found");
        } else {
            AdminDto adminDto = modelMapper.map(adminEntity.get(), AdminDto.class);
            adminDto.setRole("ROLE_ADMIN");
            return Optional.of(adminDto);
        }
    }

    @Override
    public AdminDto addAdmin(AdminDto adminDto) {
        AdminEntity adminEntity = modelMapper.map(adminDto, AdminEntity.class);
        AdminEntity savedAdminEntity = adminRepository.save(adminEntity);
        return modelMapper.map(savedAdminEntity, AdminDto.class);
    }

    @Override
    public Optional<AdminDto> updateAdmin(String id, AdminDto updatedAdminDto) throws AdminNotFoundException {
        Optional<AdminEntity> existingAdminEntity = adminRepository.findById(id);
        if (existingAdminEntity.isEmpty()) throw new AdminNotFoundException("Admin not found");

        AdminEntity updatedAdminEntity = modelMapper.map(updatedAdminDto, AdminEntity.class);
        updatedAdminEntity.setAdminId(id);
        adminRepository.save(updatedAdminEntity);

        return Optional.of(modelMapper.map(updatedAdminEntity, AdminDto.class));
    }

    @Override
    public void deleteAdmin(String id) throws AdminNotFoundException {
        Optional<AdminEntity> existingAdminEntity = adminRepository.findById(id);
        if (existingAdminEntity.isEmpty()) throw new AdminNotFoundException("Admin not found");

        adminRepository.deleteById(id);
    }

    @Override
    public AdminDto findByEmail(String email) throws AdminNotFoundException {
        AdminEntity adminEntity = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("Unknown user"));

        return modelMapper.map(adminEntity, AdminDto.class);
    }

    @Override
    public AdminDto login(AdminDto adminDto) {
        System.out.println("Inside LOGIN....");
        System.out.println(adminDto.getEmail());

        AdminEntity adminEntity = adminRepository.findByEmail(adminDto.getEmail())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        System.out.println("Inside LOGIN....");
        System.out.println(adminEntity);

        if (adminEntity.getPassword().equals(adminDto.getPassword())) {
            AdminDto finalAdminDetails = modelMapper.map(adminEntity, AdminDto.class);
            finalAdminDetails.setPassword(null);
            finalAdminDetails.setRole("ROLE_ADMIN");
            return finalAdminDetails;
        }
        throw new AdminNotFoundException("Invalid password");
    }
}
