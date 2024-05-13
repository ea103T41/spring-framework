package com.learn.euphy.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDAOJdbcImpl implements AccountDAO {

    private final DataSource dataSource;

    @Autowired
    public AccountDAOJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Account> accounts() {
        List<Account> accounts = new ArrayList<>();
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            var rs = stmt.executeQuery("SELECT * FROM t_account");
            while (rs.next()) {
                accounts.add(new Account(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }


    @Override
    public void saveAll(List<Account> accounts) {
        String INSERT_ACCOUNT_ROLE = "INSERT INTO t_account_role VALUES(?, 'unverified')";
        String INSERT_ACCOUNT = "INSERT INTO t_account VALUES (?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var pstmt = conn.prepareStatement(INSERT_ACCOUNT);
             var pstmt2 = conn.prepareStatement(INSERT_ACCOUNT_ROLE)) {
            conn.setAutoCommit(false);

            for (var account : accounts) {
                pstmt.setString(1, account.name());
                pstmt.setString(2, account.email());
                pstmt.setString(3, account.encrypt());
                pstmt.setString(4, account.salt());
                pstmt.addBatch();

                pstmt2.setString(1, account.name());
                pstmt2.addBatch();
            }
            pstmt.executeBatch();
            pstmt2.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
