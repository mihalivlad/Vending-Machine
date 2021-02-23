DROP DATABASE Vending_Machine;
CREATE DATABASE Vending_Machine;

use Vending_Machine;

CREATE TABLE Money(
	mid INT AUTO_INCREMENT PRIMARY KEY,
    bill boolean,
    `valoare` INT not null
);

-- Cocca_Cola, Borsec, Snickers, SevenDays, Boromir_Covrigi, RedBull

CREATE TABLE Item(
	iid INT AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(255) not null,
    `price` float not null,
    `key` varchar(255) not null
);

INSERT INTO Item(`name`, `price`, `key`) VALUES 
					("Borsec", 2.0, "11"),
                    ("Borsec", 2.0, "11"),
                    ("Borsec", 2.0, "11"),
                    ("Coca-Cola", 3.0, "12"),
                    ("Coca-Cola", 3.0, "12"),
                    ("Coca-Cola", 3.0, "12"),
                    ("RedBull", 4.0, "13"),
                    ("RedBull", 4.0, "13"),
					("RedBull", 4.0, "13");
				