DROP TABLE IF EXISTS users,
            categories,
            compilations,
            event_compilations,
            locations,
            events,
            requests,
            ratings,
            user_ratings;

CREATE TABLE IF NOT EXISTS users(
    user_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    email varchar(320) NOT NULL,
	name varchar(320) NOT NULL,
	rating DOUBLE,
	CONSTRAINT uq_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories(
    category_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	category_name varchar(50) NOT NULL,
	CONSTRAINT uq_category_name UNIQUE (category_name)
);

CREATE TABLE IF NOT EXISTS compilations(
    compilation_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    is_pinned boolean NOT NULL,
	title varchar(50) NOT NULL,
	CONSTRAINT uq_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS locations(
    location_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS events(
    event_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    annotation varchar(2000) NOT NULL,
    id_category INTEGER REFERENCES categories (category_id) ON DELETE CASCADE NOT NULL,
    confirmed_requests INTEGER,
	created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description varchar(7000) NOT NULL,
	event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator INTEGER REFERENCES users (user_id) ON DELETE CASCADE NOT NULL,
    location INTEGER REFERENCES locations (location_id) ON DELETE CASCADE NOT NULL,
    paid boolean,
    participant_limit INTEGER,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation boolean NOT NULL,
    state VARCHAR(30),
    title varchar(120) NOT NULL,
    rating DOUBLE,
    my_rating INTEGER,
    views INTEGER
);

CREATE TABLE IF NOT EXISTS event_compilations(
    id_compilation INTEGER REFERENCES compilations (compilation_id) NOT NULL,
    id_event INTEGER REFERENCES events (event_id) NOT NULL,
    PRIMARY KEY (id_compilation, id_event)
);

CREATE TABLE IF NOT EXISTS requests(
    request_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    id_event INTEGER REFERENCES events (event_id) ON DELETE CASCADE NOT NULL,
    id_requester INTEGER REFERENCES users (user_id) ON DELETE CASCADE NOT NULL,
    status VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS ratings(
    rating_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    id_event INTEGER NOT NULL,
    rating INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS user_ratings(
    id_user INTEGER REFERENCES users (user_id) NOT NULL,
    id_rating INTEGER REFERENCES ratings (rating_id) NOT NULL,
    PRIMARY KEY (id_user, id_rating)
);

