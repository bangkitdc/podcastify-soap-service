DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS statuses;
DROP TABLE IF EXISTS subscriptions;

CREATE TABLE logs(
    id              int             AUTO_INCREMENT  PRIMARY KEY,
    description     varchar(255)    NOT NULL,
    IP              varchar(16)     NOT NULL,
    endpoint        varchar(255)    NOT NULL,
    requested_at    timestamp       NOT NULL DEFAULT NOW(),
    from_service    varchar(255)    NOT NULL
);

CREATE TABLE statuses(
    status_id       int             AUTO_INCREMENT PRIMARY KEY,
    name            varchar(255)    UNIQUE NOT NULL
);

INSERT INTO statuses(name) VALUES
    ('PENDING'),
    ('ACCEPTED'),
    ('REJECTED');

CREATE TABLE subscriptions(
    creator_id      int             NOT NULL,
    subscriber_id   int             NOT NULL,
    status_id       int             NOT NULL DEFAULT 1,
    PRIMARY KEY (creator_id, subscriber_id),
    FOREIGN KEY (status_id) REFERENCES statuses(status_id)
);