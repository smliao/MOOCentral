-- phpMyAdmin SQL Dump
-- version 2.11.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 18, 2014 at 10:14 AM
-- Server version: 5.5.36
-- PHP Version: 5.4.22

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

-- --------------------------------------------------------

--
-- Table structure for table `coursedetails`
--

CREATE TABLE IF NOT EXISTS `coursedetails` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `profname` varchar(100) COLLATE utf16_unicode_ci NOT NULL,
  `profimage` text CHARACTER SET latin1 NOT NULL,
  `course_id` int(5) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `course_data`
--

CREATE TABLE IF NOT EXISTS `course_data` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `title` text COLLATE utf16_unicode_ci NOT NULL,
  `short_desc` text COLLATE utf16_unicode_ci NOT NULL,
  `long_desc` text COLLATE utf16_unicode_ci NOT NULL,
  `course_link` text CHARACTER SET latin1 NOT NULL,
  `video_link` text CHARACTER SET latin1 NOT NULL,
  `start_date` date NOT NULL,
  `course_length` int(11) NOT NULL,
  `course_image` text CHARACTER SET latin1 NOT NULL,
  `category` varchar(100) CHARACTER SET latin1 NOT NULL,
  `site` text CHARACTER SET latin1 NOT NULL,
  `course_fee` int(11) NOT NULL,
  `language` text CHARACTER SET latin1 NOT NULL,
  `certificate` enum('yes','no') CHARACTER SET latin1 NOT NULL,
  `university` text CHARACTER SET latin1 NOT NULL,
  `time_scraped` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf16 COLLATE=utf16_unicode_ci;

-- --------------------------------------------------------

--
-- Constraints for table `coursedetails`
--
ALTER TABLE `coursedetails`
  ADD CONSTRAINT `coursedetails_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course_data` (`id`);

-- --------------------------------------------------------
  
--
-- Table structure for table `rating`
--

CREATE TABLE IF NOT EXISTS `rating` (
  `course_id` int(5) DEFAULT NULL,
  `rate` tinyint(1) DEFAULT '0',
  KEY `course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- --------------------------------------------------------

--
-- Table structure for table `rating_vote`
--

CREATE TABLE IF NOT EXISTS `rating_vote` (
  `course_id` int(5) NOT NULL,
  `user_id` int(11) NOT NULL,
  `thumbs_up` tinyint(1) NOT NULL,
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE IF NOT EXISTS `members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `fname` varchar(30) NOT NULL,
  `lname` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` char(128) NOT NULL,
  `salt` char(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf16 AUTO_INCREMENT=1;

-- --------------------------------------------------------

--
-- Table structure for table `member_faves`
--

CREATE TABLE IF NOT EXISTS `member_faves` (
  `member_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  PRIMARY KEY (`member_id`,`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `reviews` (
  `user_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `review` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- --------------------------------------------------------

--
-- Structure for view `courses_no_dup`
--
DROP TABLE IF EXISTS `courses_no_dup`;

CREATE ALGORITHM=UNDEFINED DEFINER=`sjsucsor_s2g414s`@`localhost` SQL SECURITY DEFINER VIEW `sjsucsor_160s2g42014s`.`courses_no_dup` AS select `sjsucsor_160s2g42014s`.`course_data`.`course_image` AS `course_image`,`sjsucsor_160s2g42014s`.`course_data`.`title` AS `title`,`sjsucsor_160s2g42014s`.`course_data`.`start_date` AS `start_date`,`sjsucsor_160s2g42014s`.`course_data`.`course_length` AS `course_length`,`sjsucsor_160s2g42014s`.`course_data`.`site` AS `site`,`sjsucsor_160s2g42014s`.`coursedetails`.`profname` AS `profname`,`sjsucsor_160s2g42014s`.`coursedetails`.`profimage` AS `profimage`,`sjsucsor_160s2g42014s`.`coursedetails`.`course_id` AS `course_id` from (`sjsucsor_160s2g42014s`.`course_data` join `sjsucsor_160s2g42014s`.`coursedetails`) where (`sjsucsor_160s2g42014s`.`coursedetails`.`course_id` = `sjsucsor_160s2g42014s`.`course_data`.`id`) group by `sjsucsor_160s2g42014s`.`course_data`.`course_link`;

-- --------------------------------------------------------

--
-- Structure for view `courses`
--
DROP TABLE IF EXISTS `courses`;

CREATE ALGORITHM=UNDEFINED DEFINER=`sjsucsor_s2g414s`@`localhost` SQL SECURITY DEFINER VIEW `sjsucsor_160s2g42014s`.`courses` AS select `sjsucsor_160s2g42014s`.`rating`.`course_id` AS `course_id`,`sjsucsor_160s2g42014s`.`rating`.`rate` AS `rate`,`courses_no_dup`.`course_image` AS `course_image`,`courses_no_dup`.`title` AS `title`,`courses_no_dup`.`start_date` AS `start_date`,`courses_no_dup`.`course_length` AS `course_length`,`courses_no_dup`.`site` AS `site`,`courses_no_dup`.`profname` AS `profname`,`courses_no_dup`.`profimage` AS `profimage` from (`sjsucsor_160s2g42014s`.`rating` join `sjsucsor_160s2g42014s`.`courses_no_dup` on((`sjsucsor_160s2g42014s`.`rating`.`course_id` = `courses_no_dup`.`course_id`)));
