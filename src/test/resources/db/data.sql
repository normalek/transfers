-- Initial data
DELETE from account;

INSERT INTO account(id, balance, alias, version) VALUES (1000340, 0, 'ZERO_ACCOUNT', 0),
                                               (1000341, 20.15, 'FIRST_ACCOUNT', 0),
                                               (1000342, 101.9, 'SECOND_ACCOUNT', 0),
                                               (1000343, 999.99, 'THIRD_ACCOUNT', 0),
                                               (1000344, 83.27, 'FOURTH_ACCOUNT', 0);