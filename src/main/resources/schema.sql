-- schema per le tabelle del database

DROP TABLE IF EXISTS activities;
DROP TABLE IF EXISTS advertisers;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS favorites;


create TABLE IF NOT EXISTS advertisers (
                                           id INTEGER PRIMARY KEY,
                                           balance INTEGER NOT NULL,
                                           agency_name TEXT,
                                           agency_costs INTEGER,
                                           private_name TEXT,
                                           private_last_name TEXT
);


create TABLE IF NOT EXISTS clients (
                                       fiscal_code TEXT PRIMARY KEY,
                                       name TEXT NOT NULL,
                                       last_name TEXT NOT NULL
);

create TABLE IF NOT EXISTS activities (
                                   id INTEGER PRIMARY KEY,
                                   title TEXT NOT NULL,
                                   type TEXT NOT NULL,
                                   description TEXT NOT NULL,
                                   advertiserId INTEGER NOT NULL,
                                   address TEXT NOT NULL,
                                   city TEXT NOT NULL,
                                   duration INTEGER NOT NULL,
                                   price INTEGER NOT NULL,
                                   FOREIGN KEY (advertiserId) REFERENCES advertisers(id) ON delete CASCADE
    );

create TABLE IF NOT EXISTS bookings (
                                        id INTEGER PRIMARY KEY,
                                        date DATE NOT NULL,
                                        time TIME NOT NULL,
                                        idActivity INTEGER NOT NULL,
                                        idClient TEXT NOT NULL,
                                        FOREIGN KEY (idActivity) REFERENCES activities(id) ON delete CASCADE,
    FOREIGN KEY (idClient) REFERENCES clients(fiscal_code) ON delete CASCADE
    );

create TABLE IF NOT EXISTS favorites (
                                         idActivity INTEGER NOT NULL,
                                         idClient TEXT NOT NULL,
                                         PRIMARY KEY (idActivity, idClient),
    FOREIGN KEY (idActivity) REFERENCES activities(id) ON delete CASCADE,
    FOREIGN KEY (idClient) REFERENCES clients(fiscal_code) ON delete CASCADE
    );
