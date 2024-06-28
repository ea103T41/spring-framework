package com.euphy.learn.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AccountRoleDAOJdbcImpl implements AccountRoleDAO {
    private final DataSource dataSource;

    @Autowired
    public AccountRoleDAOJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<AccountRole> accountRoleBy(String name) {
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(
                     "SELECT * FROM t_account_role WHERE name = ?")) {
            stmt.setString(1, name);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new AccountRole(
                        rs.getString(1),
                        rs.getString(2)
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
