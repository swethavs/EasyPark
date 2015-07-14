-- phpMyAdmin SQL Dump
-- version 4.4.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 14, 2015 at 02:31 AM
-- Server version: 5.6.24
-- PHP Version: 5.5.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `easypark`
--

-- --------------------------------------------------------

--
-- Table structure for table `parkinglots`
--

CREATE TABLE IF NOT EXISTS `parkinglots` (
  `parkinglotsid` int(11) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `address` varchar(50) NOT NULL,
  `cost` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parkinglots`
--

INSERT INTO `parkinglots` (`parkinglotsid`, `latitude`, `longitude`, `address`, `cost`) VALUES
(1, 45.5236201, -122.8889657, '611 NW Garswodd Ter Beaverton 97006', 12),
(2, 45.5094021, -122.6813699, 'SW Pedestrian Trail Portland, Or 97201', 5);

-- --------------------------------------------------------

--
-- Table structure for table `parkingspots`
--

CREATE TABLE IF NOT EXISTS `parkingspots` (
  `parkingspotsid` int(11) NOT NULL,
  `parkingspotname` varchar(50) DEFAULT NULL,
  `fk_parkinglotsid` int(11) NOT NULL,
  `fromtime` datetime NOT NULL,
  `totime` datetime NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parkingspots`
--

INSERT INTO `parkingspots` (`parkingspotsid`, `parkingspotname`, `fk_parkinglotsid`, `fromtime`, `totime`) VALUES
(1, 'Level 3: C2', 1, '2015-07-10 23:21:00', '2015-07-11 00:19:00'),
(2, 'Level 2: B7', 1, '2015-07-13 16:54:00', '2015-07-13 16:57:00'),
(3, 'Level 1: A4', 2, '2015-07-13 10:52:00', '2015-07-13 11:17:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `parkinglots`
--
ALTER TABLE `parkinglots`
  ADD PRIMARY KEY (`parkinglotsid`);

--
-- Indexes for table `parkingspots`
--
ALTER TABLE `parkingspots`
  ADD PRIMARY KEY (`parkingspotsid`),
  ADD KEY `fk_parkinglotsid` (`fk_parkinglotsid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `parkinglots`
--
ALTER TABLE `parkinglots`
  MODIFY `parkinglotsid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `parkingspots`
--
ALTER TABLE `parkingspots`
  MODIFY `parkingspotsid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `parkingspots`
--
ALTER TABLE `parkingspots`
  ADD CONSTRAINT `fk_parkinglotsid` FOREIGN KEY (`fk_parkinglotsid`) REFERENCES `parkinglots` (`parkinglotsid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
