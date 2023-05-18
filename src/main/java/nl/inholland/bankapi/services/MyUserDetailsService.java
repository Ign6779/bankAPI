package nl.inholland.bankapi.services;

import nl.inholland.bankapi.models.UserTest;
import nl.inholland.bankapi.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final UserTest user= userRepository.findUserTestByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
        return User
                .withUsername(email)
                .password(user.getPassword())
                .authorities((GrantedAuthority) user.getRoles())
                .build();
    }
}
