package gr.hua.distSys.backend.BusinessManagement.api.controller;

import gr.hua.distSys.backend.BusinessManagement.config.JwtUtils;
import gr.hua.distSys.backend.BusinessManagement.entity.BusinessRequest;
import gr.hua.distSys.backend.BusinessManagement.entity.User;
import gr.hua.distSys.backend.BusinessManagement.payload.response.MessageResponse;
import gr.hua.distSys.backend.BusinessManagement.repository.UserRepository;
import gr.hua.distSys.backend.BusinessManagement.service.BusinessRequestService;
import gr.hua.distSys.backend.BusinessManagement.service.RoleService;
import gr.hua.distSys.backend.BusinessManagement.service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/businessRequests")
public class BusinessRequestController {

    @Autowired
    private BusinessRequestService businessRequestService;

    @Autowired
    private UserRepository userRepository;


    @Secured(RoleService.BUSINESS_REPRESENTATIVE)
    @PostMapping("/")
    public BusinessRequest saveBusinessRequest(@RequestBody BusinessRequest businessRequest) {

        return businessRequestService.saveBusinessRequest(businessRequest);
    }

    @Secured(RoleService.BUSINESS_REPRESENTATIVE)
    @PostMapping("/{id}")
    public BusinessRequest updateBusinessRequest(@PathVariable Integer id, @RequestBody BusinessRequest businessRequest) {
        return businessRequestService.updateBusinessRequest(businessRequest, id);
    }

    @Secured(RoleService.ADMIN)
    @GetMapping("/getUser/{id}")
    public User getUser(@PathVariable Integer id) {
        return businessRequestService.getUserById(id);
    }


    @Secured(RoleService.ADMIN)
    @GetMapping("/")
    public List<BusinessRequest> getAllBusinessRequests() {
        return businessRequestService.getAllBusinessRequests();
    }

    @Secured({RoleService.EMPLOYEE_TAX_OFFICE, RoleService.BUSINESS_REPRESENTATIVE})
    @GetMapping("/getById/{id}")
    public BusinessRequest getBusinessRequestById(@PathVariable Integer id) {
        return businessRequestService.getBusinessRequestById(id);
    }

    @Secured(RoleService.EMPLOYEE_TAX_OFFICE)
    @GetMapping("/getByState/{state}")
    public List<BusinessRequest> getAllBusinessRequestByState(@PathVariable String state) {
        return businessRequestService.getAllBusinessRequestsByState(state);
    }

    @Secured({RoleService.EMPLOYEE_TAX_OFFICE, RoleService.BUSINESS_REPRESENTATIVE})
    @GetMapping("/getAllAvailableStates")
    public List<String> getAllAvailableStates() {
        return businessRequestService.getAllAvailableStates();
    }

    @Secured(RoleService.EMPLOYEE_TAX_OFFICE)
    @GetMapping("/rejectBusinessRequest/{id}")
    public BusinessRequest rejectBusinessRequest(@PathVariable Integer id) {
        return businessRequestService.handleRequestById(id, BusinessRequestService.REJECTED);
    }

    @Secured(RoleService.EMPLOYEE_TAX_OFFICE)
    @GetMapping("/acceptBusinessRequest/{id}")
    public BusinessRequest acceptBusinessRequest(@PathVariable Integer id) {
        return businessRequestService.handleRequestById(id, BusinessRequestService.ACCEPTED);
    }

}
