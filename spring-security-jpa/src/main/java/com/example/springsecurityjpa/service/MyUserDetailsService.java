package com.example.springsecurityjpa.service;

import com.example.springsecurityjpa.model.MyUserDetails;
import com.example.springsecurityjpa.model.User;
import com.example.springsecurityjpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<User> user = userRepository.findByUserName(username);
      user.orElseThrow(() -> new UsernameNotFoundException("Not found : " + username));
      return user.map(MyUserDetails::new).get();
    }
}
