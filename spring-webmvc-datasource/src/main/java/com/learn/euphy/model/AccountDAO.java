package com.learn.euphy.model;

import java.util.Optional;

public interface AccountDAO {
    Optional<Account> accountBy(String name);
}
