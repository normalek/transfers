-- Dumping structure for table accountdb.account
DROP TABLE IF EXISTS account;

CREATE TABLE account (
    id INT NOT NULL PRIMARY KEY,
    alias VARCHAR(100),
    balance DECIMAL(10,2) NOT NULL,
    opened_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(10) DEFAULT 'ACTIVE',
    version INT DEFAULT 0
);