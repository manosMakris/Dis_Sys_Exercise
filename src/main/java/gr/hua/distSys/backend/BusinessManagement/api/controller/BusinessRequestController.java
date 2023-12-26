package gr.hua.distSys.backend.BusinessManagement.api.controller;

import gr.hua.distSys.backend.BusinessManagement.entity.BusinessRequest;
import gr.hua.distSys.backend.BusinessManagement.payload.response.MessageResponse;
import gr.hua.distSys.backend.BusinessManagement.service.BusinessRequestService;
import gr.hua.distSys.backend.BusinessManagement.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/businessRequests")
public class BusinessRequestController {

    @Autowired
    private BusinessRequestService businessRequestService;

    @Secured(RoleService.BUSINESS_REPRESENTATIVE)
    @PostMapping("/")
    public BusinessRequest saveBusinessRequest(@RequestBody BusinessRequest businessRequest) {
        return businessRequestService.saveBusinessRequest(businessRequest);
    }

    @Secured(RoleService.ADMIN)
    @GetMapping("/")
    public List<BusinessRequest> getAllBusinessRequests() {
        return businessRequestService.getAllBusinessRequests();
    }

    @Secured({RoleService.EMPLOYEE_TAX_OFFICE, RoleService.BUSINESS_REPRESENTATIVE})
    @GetMapping("/{id}")
    public BusinessRequest getBusinessRequestById(@PathVariable Integer id) {
        return businessRequestService.getBusinessRequestById(id);
    }

    @Secured(RoleService.BUSINESS_REPRESENTATIVE)
    @GetMapping("/{id}/{state}")
    public MessageResponse setStateById(@PathVariable String state, @PathVariable Integer id) {
        return businessRequestService.setStateById(id, state);
    }

    @Secured(RoleService.EMPLOYEE_TAX_OFFICE)
    @GetMapping("/{state}")
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
    @GetMapping("/acceptBusinessRequest/{id}/{afm}")
    public BusinessRequest acceptBusinessRequest(@PathVariable Integer id, @Valid @PathVariable String afm) {
        return businessRequestService.handleRequestById(id, BusinessRequestService.ACCEPTED);
    }

    @Secured(RoleService.BUSINESS_REPRESENTATIVE)
    @PostMapping("temp_save/{id}")
    public MessageResponse setStateById(@PathVariable Integer id) {
        return businessRequestService.setStateById(id, BusinessRequestService.TEMPORARILY_SAVED);
    }

}
