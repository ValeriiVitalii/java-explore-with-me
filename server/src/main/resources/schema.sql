CREATE TABLE IF NOT EXISTS hits (
                                    hit_id     INTEGER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                    app        VARCHAR(512)    NOT NULL,
                                    uri        VARCHAR(512)    NOT NULL,
                                    ip         VARCHAR(512)    NOT NULL,
                                    timestamp  TIMESTAMP WITHOUT TIME ZONE   NOT NULL
);
