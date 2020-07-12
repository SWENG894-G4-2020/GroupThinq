
DROP TABLE IF EXISTS ballot_option;
CREATE TABLE ballot_option
(
    id                 BIGSERIAL PRIMARY KEY,
    ballot_id          Text  NOT NULL,
    decision_id        Text  NOT NULL,
    user_name          Text  NOT NULL,
    expiration_date    TIMESTAMP   NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    updated_date       TIMESTAMP,
    option_title       TEXT  NOT NULL,
    option_description TEXT
);


ALTER TABLE ballot add ballot_options TEXT default NULL;

ALTER TABLE ballot add ballot_votes TEXT default NULL;