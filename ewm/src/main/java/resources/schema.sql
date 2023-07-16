DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS comments CASCADE;


CREATE TABLE IF NOT EXISTS users
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(250)                            NOT NULL,
    email   VARCHAR(254)                            NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_email UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS categories
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uq_name UNIQUE (name)

    );

CREATE TABLE IF NOT EXISTS compilations
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned  BOOLEAN                                 NOT NULL,
    title   VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL REFERENCES categories (id),
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT                                  NOT NULL REFERENCES users (id),
    location_lat       REAL                                    NOT NULL,
    location_lon       REAL                                    NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INT,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR(25),
    title              VARCHAR(120),
    CONSTRAINT pk_event PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_fk  BIGINT NOT NULL REFERENCES compilations (id),
    event_fk        BIGINT NOT NULL REFERENCES events (id)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT generated by default as identity NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id     BIGINT                                  NOT NULL REFERENCES events (id),
    requester_id BIGINT                                  NOT NULL REFERENCES users (id),
    status       VARCHAR(25)                             NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS comments
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    comment         VARCHAR(2000)                           NOT NULL,
    created         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    status          VARCHAR(25)                             NOT NULL,
    event_id        BIGINT                                  NOT NULL REFERENCES events (id),
    commenter_id    BIGINT                                  NOT NULL REFERENCES users (id),
    CONSTRAINT pk_comments PRIMARY KEY (id)
    );