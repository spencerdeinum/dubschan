# --- !Ups
 
CREATE TABLE boards (
	id SERIAL,
	short_name varchar(3) NOT NULL,
	name varchar(255) NOT NULL,
	PRIMARY KEY (id)
);

INSERT INTO boards VALUES ( 1, 'v', 'Video Games' );
 
# --- !Downs
 
DROP TABLE boards;
