DROP TABLE IF EXISTS statistics;

CREATE TABLE IF NOT EXISTS statistics(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    service varchar(512) NOT NULL,
	uri varchar(512) NOT NULL,
	ip varchar(512) NOT NULL,
	time_stamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);