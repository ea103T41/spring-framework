drop table T_account_ROLE IF EXISTS;
drop table T_account IF EXISTS;

CREATE TABLE t_account (
  name VARCHAR(15) NOT NULL,
  email VARCHAR(128) NOT NULL,
  encrypt VARCHAR(32) NOT NULL,
  salt VARCHAR(256) NOT NULL,
  PRIMARY KEY (name)
);
CREATE TABLE t_account_role (
    name VARCHAR(15) NOT NULL,
    role VARCHAR(15) NOT NULL,
    PRIMARY KEY (name, role)
);