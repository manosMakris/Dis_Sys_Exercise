package gr.hua.distSys.backend.BusinessManagement.service;

import gr.hua.distSys.backend.BusinessManagement.entity.Role;
import gr.hua.distSys.backend.BusinessManagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String EMPLOYEE_TAX_OFFICE = "ROLE_EMPLOYEE_TAX_OFFICE";

    public static final String BUSINESS_REPRESENTATIVE = "ROLE_BUSINESS_REPRESENTATIVE";
    @Autowired
    RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public boolean deleteRoleById(Integer id) {
        if (getRoleById(id).getId() == -1) {
            return false;
        }
        roleRepository.deleteById(id);
        return true;
    }

    public Role getRoleById(Integer id) {
        Role badRole = new Role("ROLE_NOT_FOUND");
        badRole.setId(-1);
        return roleRepository.findById(id).orElse(badRole);
    }

    public boolean roleExistsByName(String roleName) {
        return roleRepository.existsByName(roleName);
    }
}
