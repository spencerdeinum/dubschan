# --- !Ups
 
CREATE TABLE threads (
	id SERIAL,
  created_at timestamp,
  title varchar(255),
	PRIMARY KEY (id)
);

CREATE TABLE posts (
  id SERIAL,
  thread_id INT,
  created_at timestamp,
  content varchar(255),
	image_name varchar(255) NULL,
  PRIMARY KEY (id)
);

# --- !Downs
 
DROP TABLE threads;

DROP TABLE posts;
