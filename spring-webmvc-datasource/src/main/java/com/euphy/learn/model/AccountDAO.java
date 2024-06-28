package com.euphy.learn.model;

import java.util.Optional;

public interface AccountDAO {
    Optional<Account> accountBy(String name);
}
