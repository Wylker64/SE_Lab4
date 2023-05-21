CREATE TABLE flow(
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender VARCHAR(255),
    receiver VARCHAR(255),
    amount DECIMAL(10,2),
    remark VARCHAR(255),
    timestamp TIMESTAMP
);