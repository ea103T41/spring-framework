package com.learn.euphy.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    private final AccountDAO acctDAO;
    private final AccountRoleDAO acctRoleDAO;

    @Autowired
    public UserService(AccountDAO acctDAO, AccountRoleDAO acctRoleDAO) {
        this.acctDAO = acctDAO;
        this.acctRoleDAO = acctRoleDAO;
    }

    public List<User> accounts() {
        List<Account> accounts = acctDAO.accounts();
        return accounts.stream().map(account -> {
            Optional<AccountRole> acctRole = acctRoleDAO.accountRoleBy(account.name());
            String role = acctRole.map(AccountRole::role).orElse("no role");
            User user = new User();
            user.setName(account.name());
            user.setPassword("********");
            user.setEmail(account.email());
            user.setRole(role);
            return user;
        }).toList();
    }

    public void saveAll(List<User> users) {
        List<Account> accounts = new ArrayList<>();
        for (var user : users) {
            accounts.add(createUser(user.getName(), user.getEmail(), user.getPassword()));
        }
        acctDAO.saveAll(accounts);
    }

    public Account createUser(String username, String email, String password) {
        var salt = ThreadLocalRandom.current().nextInt();
        var encrypt = String.valueOf(salt + password.hashCode());
        return new Account(username, email, encrypt, String.valueOf(salt));
    }
}
