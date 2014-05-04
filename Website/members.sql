CREATE TABLE `members` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  `username` VARCHAR(30) NOT NULL,
  `fname` VARCHAR(30) NOT NULL,
  `lname` VARCHAR(30) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `password` CHAR(128) NOT NULL,
  `salt` CHAR(128) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE member_faves (
  member_id INT NOT NULL REFERENCES members (member_id),
  course_id INT NOT NULL REFERENCES course_data (course_id),
  PRIMARY KEY (member_id, course_id)
)ENGINE = InnoDB DEFAULT CHARSET=latin1;

