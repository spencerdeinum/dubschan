# --- !Ups
 
CREATE TABLE threads (
	id SERIAL,
	board_id INT references boards(id) NOT NULL,
  created_at timestamp,
  title varchar(255),
	PRIMARY KEY (id)
);

CREATE TABLE posts (
  id SERIAL,
  thread_id INT references threads(id) NOT NULL,
  created_at timestamp,
  content varchar(255),
	image_name varchar(255) NULL,
  PRIMARY KEY (id)
);

# --- !Downs
 
DROP TABLE threads CASCADE;

DROP TABLE posts CASCADE;
