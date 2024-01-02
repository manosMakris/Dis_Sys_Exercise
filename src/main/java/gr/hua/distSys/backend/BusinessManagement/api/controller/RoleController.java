package gr.hua.distSys.backend.BusinessManagement.api.controller;

import gr.hua.distSys.backend.BusinessManagement.entity.Role;
import gr.hua.distSys.backend.BusinessManagement.payload.response.MessageResponse;
import gr.hua.distSys.backend.BusinessManagement.service.RoleService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/")
    public Role saveRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @GetMapping("/deleteRole/{id}")
    public MessageResponse deleteRole(@PathVariable Integer id) {
        boolean success = roleService.deleteRoleById(id);
        if (success) {
            return new MessageResponse("Role deleted successfully.");
        } else {
            return new MessageResponse("Unsuccessful deletion of role.");
        }
    }

    @PostMapping("/updateRole/{id}")
    public Role updateRole(@RequestBody Role role, @PathVariable Integer id) {
        Role updatedRole = roleService.getRoleById(id);
        if (updatedRole.getId() == -1) {
            return updatedRole;
        }
        updatedRole.setName(role.getName());
        return roleService.saveRole(updatedRole);
    }

    @GetMapping("/{id}")
    public Role getRole(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }
}
