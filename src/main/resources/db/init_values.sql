INSERT INTO Countries(CT_name)
VALUES ('Poland');
INSERT INTO Countries(CT_name)
VALUES ('Germany');
INSERT INTO Countries(CT_name)
VALUES ('France');
INSERT INTO Countries(CT_name)
VALUES ('Spain');
INSERT INTO Countries(CT_name)
VALUES ('Italy');

INSERT INTO Payment_Methods(PMTM_name)
VALUES ('CARD');
INSERT INTO Payment_Methods(PMTM_name)
VALUES ('TRANSFER');
INSERT INTO Payment_Methods(PMTM_name)
VALUES ('CASH');

INSERT INTO Payment_Statuses(PMTS_name)
VALUES ('PENDING');
INSERT INTO Payment_Statuses(PMTS_name)
VALUES ('PAID');
INSERT INTO Payment_Statuses(PMTS_name)
VALUES ('FAILED');
INSERT INTO Payment_Statuses(PMTS_name)
VALUES ('REFUNDED');

INSERT INTO Room_type(RT_name) VALUES ('EXCLUSIVE');
INSERT INTO Room_type(RT_name) VALUES ('STANDARD');

COMMIT;