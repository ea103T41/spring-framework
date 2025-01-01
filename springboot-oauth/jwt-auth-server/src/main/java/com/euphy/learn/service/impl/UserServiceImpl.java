package com.euphy.learn.service.impl;

import com.euphy.learn.repository.UserRepository;
import com.euphy.learn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  @Override
  // UserDetailsService is an interface, so we need to implement it
  public UserDetailsService userDetailsService() {
    return username -> userRepository.findByEmail(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    // same as
    // return new UserDetailsService() {
    //        @Override
    //        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //            return userRepository.findByEmail(username)
    //                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    //        }
    //    };
  }
}
