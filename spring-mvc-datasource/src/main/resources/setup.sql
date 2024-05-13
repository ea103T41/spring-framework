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

INSERT INTO T_ACCOUNT values ('ea103t41', 'ea103t41@gmail.com', '-307216397', '1554136943');
INSERT INTO T_ACCOUNT_ROLE values ('ea103t41', 'member');