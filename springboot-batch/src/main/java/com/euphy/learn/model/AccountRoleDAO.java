package com.euphy.learn.model;

import java.util.Optional;

public interface AccountRoleDAO {

    Optional<AccountRole> accountRoleBy(String name);

}
