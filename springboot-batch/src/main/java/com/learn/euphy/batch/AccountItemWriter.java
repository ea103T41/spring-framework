package com.learn.euphy.batch;

import com.learn.euphy.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountItemWriter implements ItemWriter<Account> {

    private static final Logger log = LoggerFactory.getLogger(AccountItemWriter.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void write(Chunk<? extends Account> chunk) {
        String INSERT_ACCOUNT = "INSERT INTO t_account VALUES (?, ?, ?, ?)";
        String INSERT_ACCOUNT_ROLE = "INSERT INTO t_account_role VALUES(?, 'unverified')";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_ACCOUNT);
             PreparedStatement pstmt2 = conn.prepareStatement(INSERT_ACCOUNT_ROLE)) {

            conn.setAutoCommit(false);
            for (Account account : chunk) {
                // Set the variables
                pstmt.setString(1, account.name());
                pstmt.setString(2, account.email());
                pstmt.setString(3, account.encrypt());
                pstmt.setString(4, account.salt());
                // Add it to the batch
                pstmt.addBatch();

                pstmt2.setString(1, account.name());
                pstmt2.addBatch();
            }
            int[] count1 = pstmt.executeBatch();
            int[] count2 = pstmt2.executeBatch();
            if (count1.length != count2.length) {
                throw new RuntimeException("Batch sizes don't match");
            }
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
