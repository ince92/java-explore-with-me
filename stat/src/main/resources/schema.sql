CREATE TABLE IF NOT EXISTS hits
(
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app varchar(100)  NOT NULL,
    uri varchar(100)  NOT NULL,
    ip varchar(100)  NOT NULL,
    request_time TIMESTAMP WITHOUT TIME ZONE
    );