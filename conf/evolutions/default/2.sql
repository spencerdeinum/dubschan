# --- !Ups
 
CREATE TABLE threads (
	id SERIAL,
  title varchar(255),
	PRIMARY KEY (id)
);

CREATE TABLE posts (
  id SERIAL,
  thread_id INT,
  content varchar(255),
  PRIMARY KEY (id)
);

# --- !Downs
 
DROP TABLE threads;

DROP TABLE posts;
