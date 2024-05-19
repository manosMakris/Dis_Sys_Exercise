package gr.hua.distSys.backend.BusinessManagement.api.controller;


import gr.hua.distSys.backend.BusinessManagement.config.JwtUtils;
import gr.hua.distSys.backend.BusinessManagement.entity.BusinessRequest;
import gr.hua.distSys.backend.BusinessManagement.entity.Role;
import gr.hua.distSys.backend.BusinessManagement.entity.User;
import gr.hua.distSys.backend.BusinessManagement.payload.request.LoginRequest;
import gr.hua.distSys.backend.BusinessManagement.payload.request.SignupRequest;
import gr.hua.distSys.backend.BusinessManagement.payload.response.JwtResponse;
import gr.hua.distSys.backend.BusinessManagement.payload.response.MessageResponse;
import gr.hua.distSys.backend.BusinessManagement.repository.RoleRepository;
import gr.hua.distSys.backend.BusinessManagement.repository.UserRepository;
import gr.hua.distSys.backend.BusinessManagement.service.RoleService;
import gr.hua.distSys.backend.BusinessManagement.service.UserDetailsImpl;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    private String getUsernameOfActiveUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        return userDetails.getUsername();
    }

    @PostConstruct
    public void setup() {

        if (!roleService.roleExistsByName(RoleService.ADMIN)) {
            roleService.saveRole(new Role(RoleService.ADMIN));
        }
        if (!roleService.roleExistsByName(RoleService.EMPLOYEE_TAX_OFFICE)) {
            roleService.saveRole(new Role(RoleService.EMPLOYEE_TAX_OFFICE));
        }
        if (!roleService.roleExistsByName(RoleService.BUSINESS_REPRESENTATIVE)) {
            roleService.saveRole(new Role(RoleService.BUSINESS_REPRESENTATIVE));
        }

        SignupRequest signupRequest = new SignupRequest();
        if (!userRepository.existsByUsername("admin123")) {
            signupRequest.setUsername("admin123");
            signupRequest.setPassword("pass_admin");
            signupRequest.setEmail("systemAdmin@gmail.com");

            Set<String> roles = new HashSet<>();
            roles.add(RoleService.ADMIN);
            signupRequest.setRoles(roles);

            registerUser(signupRequest);
        }
        if (!userRepository.existsByUsername("employee123")) {
            signupRequest.setUsername("employee123");
            signupRequest.setPassword("pass_emp");
            signupRequest.setEmail("employee123@gmail.com");

            Set<String> roles = new HashSet<>();
            roles.add(RoleService.EMPLOYEE_TAX_OFFICE);
            signupRequest.setRoles(roles);

            registerUser(signupRequest);
        }

        if (!userRepository.existsByUsername("business_repr123")) {
            signupRequest.setUsername("business_repr123");
            signupRequest.setPassword("pass_bus_rep");
            signupRequest.setEmail("businessRepr123@gmail.com");

            Set<String> roles = new HashSet<>();
            roles.add(RoleService.BUSINESS_REPRESENTATIVE);
            signupRequest.setRoles(roles);

            registerUser(signupRequest);
        }
    }

    @Secured(RoleService.BUSINESS_REPRESENTATIVE)
    @GetMapping("/getUsersBusinessRequests")
    public List<BusinessRequest> getBusinessRequests() {

        String username = getUsernameOfActiveUser();
        Optional<User> optionalUser = userRepository.findByUsername(username);

        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            return user.getBusinessRequests();
        }
        return null;
    }

    @Secured(RoleService.ADMIN)
    @GetMapping("/deleteUser/{id}")
    public MessageResponse deleteUser(@PathVariable Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return new MessageResponse("The deletion was successful.");
        } else {
            return new MessageResponse("There isn't a user with id = " + id + ".");
        }
    }

    @Secured(RoleService.ADMIN)
    @PostMapping("/updateUser/{id}")
    public MessageResponse updateUser(@PathVariable Integer id, @RequestBody User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();

            if (user.getUsername() != null) {
                userToUpdate.setUsername(user.getUsername());
            }
            if (user.getEmail() != null) {
                userToUpdate.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                userToUpdate.setPassword(encoder.encode(user.getPassword()));
            }

            userRepository.save(userToUpdate);

            return new MessageResponse("The update was successful.");
        } else {
            return new MessageResponse("There isn't a user with id = " + id + ".");
        }
    }


    @Secured(RoleService.ADMIN)
    @PostMapping("/addRole/{id}")
    public MessageResponse addRole(@RequestBody Role role, @PathVariable Integer id) {

        if (roleRepository.findByName(role.getName()).isPresent()) {
            User user = getUserById(id);
            if (user == null) {
                return new MessageResponse("There isn't a user with id = " + id + ".");
            }
            Set<Role> roles = user.getRoles();
            roles.add(roleRepository.findByName(role.getName()).get());
            user.setRoles(roles);
            userRepository.save(user);
            return new MessageResponse("The user has been successfully updated.");
        } else {
            return new MessageResponse("Invalid role.");
        }
    }

    @Secured(RoleService.ADMIN)
    @PostMapping("/deleteRole/{id}")
    public MessageResponse deleteRole(@RequestBody Role role, @PathVariable Integer id) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            User user = getUserById(id);
            if (user == null) {
                return new MessageResponse("There isn't a user with id = " + id + ".");
            }
            Set<Role> roles = user.getRoles();
            boolean removeSuccess = roles.remove(roleRepository.findByName(role.getName()).get());
            if (!removeSuccess) {
                return new MessageResponse("The user didn't have the role " + role.getName() + ".");
            }

            user.setRoles(roles);
            userRepository.save(user);
            return new MessageResponse("The user has been successfully updated.");
        } else {
            return new MessageResponse("Invalid role.");
        }
    }

    @Secured(RoleService.ADMIN)
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Secured(RoleService.ADMIN)
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userRepository.findById(id).orElse(null);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("authentication");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        System.out.println("authentication: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("post authentication");
        String jwt = jwtUtils.generateJwtToken(authentication);
        System.out.println("jw: " + jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    //@Secured(RoleService.ADMIN)
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles != null) {
            strRoles.forEach(strRole -> {
                Role role = roleRepository.findByName(strRole)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(role);
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }




}
