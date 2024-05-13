package com.learn.euphy.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final AccountDAO acctDAO;
    private final AccountRoleDAO acctRoleDAO;

    @Autowired
    public UserService(AccountDAO acctDAO, AccountRoleDAO acctRoleDAO) {
        this.acctDAO = acctDAO;
        this.acctRoleDAO = acctRoleDAO;
    }

    public Optional<User> accountBy(String username) {
        return acctDAO.accountBy(username)
                .map(acct -> {
                    Optional<AccountRole> acctRole = acctRoleDAO.accountRoleBy(acct.name());
                    String role = acctRole.map(AccountRole::role).orElse("no role");
                    User user = new User();
                    user.setName(acct.name());
                    user.setEmail(acct.email());
                    user.setRole(role);
                    return user;
                });
    }

}
