package gr.hua.distSys.backend.BusinessManagement.service;

import gr.hua.distSys.backend.BusinessManagement.config.JwtUtils;
import gr.hua.distSys.backend.BusinessManagement.entity.BusinessRequest;
import gr.hua.distSys.backend.BusinessManagement.entity.User;
import gr.hua.distSys.backend.BusinessManagement.payload.response.MessageResponse;
import gr.hua.distSys.backend.BusinessManagement.repository.BusinessRequestRepository;
import gr.hua.distSys.backend.BusinessManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BusinessRequestService {

    public static String SUBMITTED = "submitted";
    public static String ACCEPTED = "accepted";
    public static String REJECTED = "rejected";
    public static String TEMPORARILY_SAVED = "temporarily saved";

    private String getUsernameOfActiveUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        return userDetails.getUsername();
    }


    @Autowired
    private BusinessRequestRepository businessRequestRepository;

    @Autowired
    private UserRepository userRepository;


    public List<BusinessRequest> getAllBusinessRequests() {
        return businessRequestRepository.findAll();
    }

    public List<BusinessRequest> getAllBusinessRequestsByState(String state) {
        return businessRequestRepository.findAllByStateOfRequest(state);
    }

    public BusinessRequest saveBusinessRequest(BusinessRequest businessRequest) {

        // Get username
        String username = getUsernameOfActiveUser();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user;

        if (optionalUser.isPresent()) {
            // Get user by username
            user = optionalUser.get();

            // Add the business request to the user
            List<BusinessRequest> businessRequests = user.getBusinessRequests();
            businessRequests.add(businessRequest);
            user.setBusinessRequests(businessRequests);

            // Set the user to the new business request
            businessRequest.setUser(user);

            // Save the changes to the database
            userRepository.save(user);

            return businessRequestRepository.save(businessRequest);
        }
        return null;
    }

    public User getUserById(Integer id) {
        Optional<BusinessRequest> optionalBusinessRequest = businessRequestRepository.findById(id);
        BusinessRequest businessRequest;

        if (optionalBusinessRequest.isPresent()) {
            businessRequest = optionalBusinessRequest.get();
            return businessRequest.getUser();
        } else {
            throw new RuntimeException("There is not a business request with id = " + id + ".");
        }
    }

    public List<String> getAllAvailableStates() {
        return List.of(SUBMITTED, ACCEPTED, REJECTED, TEMPORARILY_SAVED);
    }

    // Create a controller method to call this function.
    public MessageResponse setStateById(Integer id, String state) {
        if (!getAllAvailableStates().contains(state)) {
            return new MessageResponse("Invalid state.");
        }

        Optional<BusinessRequest> optionalBusinessRequest = businessRequestRepository.findById(id);
        if (optionalBusinessRequest.isPresent()) {
            optionalBusinessRequest.get().setStateOfRequest(state);
            return new MessageResponse("The state has been successfully updated.");
        } else {
            return new MessageResponse("There isn't a business request with id = " + id + ".");
        }
    }

    public BusinessRequest setAfmById(Integer id) {
        Optional<BusinessRequest> optionalBusinessRequest = businessRequestRepository.findById(id);

        String afm;
        do{
            afm = generateNumberString(10);
        } while (businessRequestRepository.existsByAfm(afm));


        if (optionalBusinessRequest.isPresent()) {
            BusinessRequest br = optionalBusinessRequest.get();
            br.setAfm(afm);

            return br;
        } else {
            return null;
        }
    }

    private static String generateNumberString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generates a random digit between 0 and 9
            stringBuilder.append(digit);
        }

        return stringBuilder.toString();
    }

    public BusinessRequest handleRequestById(Integer id, String state) {

        Optional<BusinessRequest> optionalBusinessRequest = businessRequestRepository.findById(id);

        // Check if the state is valid
        if (!state.equals(ACCEPTED) && !state.equals(REJECTED)) {
            throw new RuntimeException("The state should be " + ACCEPTED + " or " + REJECTED + ".");
        }

        BusinessRequest br;

        // Check if the business request exists
        if (optionalBusinessRequest.isPresent()) {
            // Get the business request
            br = optionalBusinessRequest.get();
        } else {
            throw new RuntimeException("There is not a business request with id = " + id + ".");
        }

        // Check if the business request's state is submitted
        if (!br.getStateOfRequest().equals(SUBMITTED)) {
            throw new RuntimeException("The business request should be submitted first.");
        }

        // Set the business request's state to the given state
        br.setStateOfRequest(state);


        // Check if the business request is accepted
        if (state.equals(ACCEPTED)) {
            // Set the business request's afm to the given afm
            br = setAfmById(br.getId());
        }

        // Save the updated version of the business request back to the database
        // and return the updated version of the business request to the caller.
        return businessRequestRepository.save(br);
    }

    public BusinessRequest getBusinessRequestById(Integer id) {
        return businessRequestRepository.findById(id).orElse(null);
    }

    public BusinessRequest updateBusinessRequest(BusinessRequest businessRequest, Integer id) {
        Optional<BusinessRequest> optionalBusinessRequest = businessRequestRepository.findById(id);
        BusinessRequest br;

        if (optionalBusinessRequest.isPresent()) {
            br = optionalBusinessRequest.get();
            if (businessRequest.getMissionStatement() != null) {
                br.setMissionStatement(businessRequest.getMissionStatement());
            }
            if (businessRequest.getLocation() != null) {
                br.setLocation(businessRequest.getLocation());
            }
            if (businessRequest.getPurpose() != null) {
                br.setPurpose(businessRequest.getPurpose());
            }
            if (businessRequest.getMembers() != null) {
                br.setMembers(businessRequest.getMembers());
            }
            if (businessRequest.getStateOfRequest() != null) {
                br.setStateOfRequest(businessRequest.getStateOfRequest());
            }

            return businessRequestRepository.save(br);
        }
        return null;
    }
}
