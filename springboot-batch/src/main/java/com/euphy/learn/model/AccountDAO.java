package com.euphy.learn.model;

import java.util.List;

public interface AccountDAO {

    List<Account> accounts();

    void saveAll(List<Account> accounts);

}
