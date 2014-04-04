CREATE DATABASE IF NOT EXISTS `MOOCentral` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `MOOCentral`;

DROP TABLE IF EXISTS `coursedata`;

CREATE TABLE IF NOT EXISTS `coursedata` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `title` text COLLATE utf8_unicode_ci NOT NULL,
  `short_desc` text COLLATE utf8_unicode_ci NOT NULL,
  `long_desc` text COLLATE utf8_unicode_ci NOT NULL,
  `course_link` text COLLATE utf8_unicode_ci NOT NULL,
  `video_link` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;