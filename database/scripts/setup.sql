-- USERS
DROP TABLE IF EXISTS USERS CASCADE;
CREATE TABLE USERS (
    ID SERIAL PRIMARY KEY,
    USERNAME VARCHAR(32) UNIQUE NOT NULL,
    PW VARCHAR(256) UNIQUE NOT NULL,
    LAST_NAME VARCHAR(32) NOT NULL,
    FIRST_NAME VARCHAR(32) NOT NULL,
    EMAIL VARCHAR(32) NOT NULL,
    BIRTH_DATE TIMESTAMP NOT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    UPDATED_DATE TIMESTAMP,
    LAST_LOGGED_IN TIMESTAMP
);

-- DECISION
DROP TABLE IF EXISTS DECISION CASCADE;
CREATE TABLE DECISION (
    ID SERIAL PRIMARY KEY,
    NAME VARCHAR(32) NOT NULL,
    DESCRIPTION VARCHAR(1000) NOT NULL,
    OWNER_ID INTEGER REFERENCES USERS(ID) NOT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    UPDATED_DATE TIMESTAMP
);

-- BALLOT
DROP TABLE IF EXISTS BALLOT CASCADE;
CREATE TABLE BALLOT (
    ID SERIAL PRIMARY KEY,
    DECISION_ID INTEGER REFERENCES DECISION(ID) NOT NULL,
    EXPIRATION_DATE TIMESTAMP NOT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    UPDATED_DATE TIMESTAMP
);

-- BALLOT_OPTION
DROP TABLE IF EXISTS BALLOT_OPTION CASCADE;
CREATE TABLE BALLOT_OPTION (
    ID SERIAL PRIMARY KEY,
    BALLOT_ID INTEGER REFERENCES BALLOT(ID) NOT NULL,
    USER_ID INTEGER REFERENCES USERS(ID) NOT NULL,
    TITLE VARCHAR(32) NOT NULL,
    DESCRIPTION VARCHAR(1000) NOT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    UPDATED_DATE TIMESTAMP
);

-- BALLOT_RESULTS
DROP TABLE IF EXISTS BALLOT_RESULTS CASCADE;
CREATE TABLE BALLOT_RESULTS (
    BALLOT_ID INTEGER REFERENCES BALLOT(ID) NOT NULL,    
    BALLOT_OPTION_ID INTEGER REFERENCES BALLOT_OPTION(ID) NOT NULL,
    USER_ID INTEGER REFERENCES USERS(ID) NOT NULL,
    VOTE_DATE TIMESTAMP NOT NULL,
    VOTE_UPDATED_DATE TIMESTAMP
);

-- ROLE
DROP TABLE IF EXISTS ROLE_REF CASCADE;
CREATE TABLE ROLE_REF (
    ID INTEGER UNIQUE PRIMARY KEY NOT NULL,
    NAME VARCHAR(32) UNIQUE NOT NULL
);

INSERT INTO ROLE_REF VALUES (1, 'User');
INSERT INTO ROLE_REF VALUES (2, 'Admin');

-- USER_ROLES
DROP TABLE IF EXISTS USER_ROLES CASCADE;
CREATE TABLE USER_ROLES (
    USER_ID INTEGER REFERENCES USERS(ID) NOT NULL,
    ROLE_ID INTEGER REFERENCES ROLE_REF(ID) NOT NULL
);

-- DECISION_USERS
DROP TABLE IF EXISTS DECISION_USERS CASCADE;
CREATE TABLE DECISION_USERS (
    DECISION_ID INTEGER REFERENCES DECISION(ID) NOT NULL,
    USER_ID INTEGER REFERENCES USERS(ID) NOT NULL
);
