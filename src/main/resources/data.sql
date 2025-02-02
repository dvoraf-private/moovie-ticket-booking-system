CREATE TABLE IF NOT EXISTS theaters (
     id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     location VARCHAR(255) NOT NULL
    );

INSERT INTO theaters (name, location) VALUES ('Cineplex Cinemas', '123 Hollywood Boulevard, Los Angeles, CA, 90001');
INSERT INTO theaters (name, location) VALUES ('Silver Screen Theater', '456 Elm Street, Chicago, IL, 60601');
INSERT INTO theaters (name, location) VALUES ('Starview Theaters', '789 Sunset Avenue, New York, NY, 10001');
INSERT INTO theaters (name, location) VALUES ('Galaxy Cinemas', '101 Main Street, San Francisco, CA, 94101');
INSERT INTO theaters (name, location) VALUES ('Cinema Royale', '222 King’s Road, Toronto, ON, M5V 1B1');


