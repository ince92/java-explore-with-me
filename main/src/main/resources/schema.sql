DROP TABLE IF EXISTS users,
    categories,
    compilations,
    locations,
    events,
    requests,
    subscriptions,
    compilations_events CASCADE;

CREATE TABLE IF NOT EXISTS locations
(
    location_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat float NOT NULL,
    lon float NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    category_name varchar(100)  NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    user_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_name varchar(50)  NOT NULL,
    user_email varchar(100)  NOT NULL,
    UNIQUE(user_email)
);

CREATE TABLE IF NOT EXISTS events
(
    event_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_category_id INTEGER REFERENCES categories (category_id) ON DELETE CASCADE,
    event_created TIMESTAMP WITHOUT TIME ZONE,
    event_description varchar(1000)  NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    event_initiator_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    event_location_id INTEGER REFERENCES locations (location_id) ON DELETE CASCADE,
    event_paid BOOLEAN,
    event_participant_limit INTEGER NOT NULL,
    event_published TIMESTAMP WITHOUT TIME ZONE,
    event_request_moderation BOOLEAN,
    event_state varchar(100) NOT NULL,
    event_title varchar(1000) NOT NULL,
    event_annotation varchar(1000) NOT NULL

 );

CREATE TABLE IF NOT EXISTS requests
(
    request_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    request_status varchar(50)  NOT NULL,
    request_requester_id INTEGER REFERENCES USERS (user_id) ON DELETE CASCADE,
    request_created TIMESTAMP WITHOUT TIME ZONE,
    request_event_id INTEGER REFERENCES events (event_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    compilation_title varchar(500)  NOT NULL,
    compilation_pinned boolean
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id integer REFERENCES compilations (compilation_id),
    event_id integer REFERENCES events (event_id),
    PRimary key(compilation_id,event_id)
);

CREATE TABLE IF NOT EXISTS subscriptions
(
    subscription_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status varchar(50)  NOT NULL,
    subscriber_id integer REFERENCES USERS (user_id) ON DELETE CASCADE,
    author_id integer REFERENCES USERS (user_id) ON DELETE CASCADE
);
