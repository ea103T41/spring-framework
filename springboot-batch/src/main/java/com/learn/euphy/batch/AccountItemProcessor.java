package com.learn.euphy.batch;

import com.learn.euphy.model.Account;
import com.learn.euphy.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.concurrent.ThreadLocalRandom;

public class AccountItemProcessor implements ItemProcessor<User, Account> {

    private static final Logger log = LoggerFactory.getLogger(AccountItemProcessor.class);

    @Override
    public Account process(final User user) {
        var name = user.getName();
        var email = user.getEmail();
        var salt = ThreadLocalRandom.current().nextInt();
        var encrypt = String.valueOf(salt + user.getPassword().hashCode());
        Account transformedAccount = new Account(name, email, encrypt, String.valueOf(salt));
        log.debug("Converting (" + user + ") into (" + transformedAccount + ")");
        return transformedAccount;
    }
}
