# Users schema
 
# --- !Ups
 
CREATE TABLE board (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    short_name varchar(3) NOT NULL,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO board VALUES ( 1, 'v', 'Video Games' );
 
# --- !Downs
 
DROP TABLE board;
