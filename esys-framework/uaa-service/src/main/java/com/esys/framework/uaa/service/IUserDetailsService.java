package com.esys.framework.uaa.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserDetailsService {

    UserDetails loadUserByUsernameAndDomain(String username, String domain) throws UsernameNotFoundException;

}
