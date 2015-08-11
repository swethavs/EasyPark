-- phpMyAdmin SQL Dump
-- version 4.4.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 08, 2015 at 10:46 PM
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
-- Table structure for table `freeparkinglots`
--

CREATE TABLE IF NOT EXISTS `freeparkinglots` (
  `id` int(11) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `time` bigint(20) NOT NULL,
  `nooflots` int(11) NOT NULL,
  `user` varchar(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `freeparkinglots`
--

INSERT INTO `freeparkinglots` (`id`, `latitude`, `longitude`, `address`, `time`, `nooflots`, `user`) VALUES
(1, 45.5235712, -122.889016, 'Quatama/NW 205th Ave MAX Station,Or', 1438841175235, 4, 'swethavs'),
(6, 45.5235808, -122.8890029, '611 NW Garswood Terrace,Beaverton, OR 97006,Beaver', 1438979460000, 1, 'hi'),
(7, 45.5235808, -122.8890029, '611 NW Garswood Terrace,Beaverton, OR 97006,Beaver', 1438979460000, 1, 'swethavs'),
(8, 45.5235808, -122.8890029, '611 NW Garswood Terrace\nBeaverton, OR 97006\nBeaver', 1438979460000, 1, 'swethavs'),
(9, 45.5235808, -122.8890029, '611 NW Garswood Terrace\nBeaverton, OR 97006\nBeaver', 1438980720000, 1, 'swethavs'),
(10, 45.5235808, -122.8890029, '611 NW Garswood Terrace\nBeaverton, OR 97006\nBeaver', 1438980720000, 1, 'swethavs'),
(11, 45.5235821, -122.8890267, '611 NW Garswood Terrace\nBeaverton, OR 97006\nBeaver', 1438981080000, 3, 'swethavs'),
(12, 45.517630999999994, -122.882634, '20001-20099 SW Mohican St\nBeaverton, OR 97006\nBeav', 1438984080000, 6, 'swethavs'),
(13, 45.519196, -122.886398, 'Beaverton, OR 97006\nBeaverton\n', 1438993740000, 3, 'swethavs'),
(14, 45.5235833, -122.8890149, '611 NW Garswood Terrace\nBeaverton, OR 97006\nBeaverton\n', 1438998780000, 20, 'swethavs'),
(15, 45.5235658, -122.8889744, '611 NW Garswood Terrace\nBeaverton, OR 97006\nBeaverton\n', 1439007660000, 36, 'hi');

-- --------------------------------------------------------

--
-- Table structure for table `parkinglots`
--

CREATE TABLE IF NOT EXISTS `parkinglots` (
  `parkinglotsid` int(11) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `address` varchar(50) NOT NULL,
  `zipcode` bigint(20) NOT NULL DEFAULT '97201',
  `cost` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parkinglots`
--

INSERT INTO `parkinglots` (`parkinglotsid`, `latitude`, `longitude`, `address`, `zipcode`, `cost`) VALUES
(1, 45.5236201, -122.8889657, '611 NW Garswodd Ter Beaverton 97006', 97006, 12),
(2, 45.5004021, -122.6803699, 'SWBarbur Blvd, Or 97201', 97201, 9),
(3, 45.511061, -122.683509, ' 1872 SW Broadway Portland, OR 97201', 97201, 9),
(5, 45.509534, -122.681081, '1900 SW 4th Ave Portland, OR 97201', 97201, 7),
(6, 45.513799, -122.680006, '1400 SW 4th Ave Portland, OR 97201', 97201, 10),
(7, 45.518292, -122.682242, '914 SW Taylor St Portland, OR 97205', 97201, 11);

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
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parkingspots`
--

INSERT INTO `parkingspots` (`parkingspotsid`, `parkingspotname`, `fk_parkinglotsid`, `fromtime`, `totime`) VALUES
(1, 'Level 3: C2', 1, '2015-07-25 09:08:00', '2015-07-25 10:08:00'),
(2, 'Level 2: B7', 1, '2015-07-25 10:57:00', '2015-07-25 11:57:00'),
(3, 'Level 1: A4', 2, '2015-07-14 10:30:00', '2015-07-14 12:30:00'),
(4, 'Level1:A1', 3, '2015-07-24 14:21:00', '2015-07-24 16:21:00'),
(5, 'Level 1:A1', 5, '2015-07-08 00:00:00', '2015-07-09 00:00:00'),
(6, 'Level 1: A1', 6, '2015-07-16 17:58:00', '2015-07-16 19:59:00'),
(7, 'Level 1: B1', 7, '2015-07-09 00:00:00', '2015-07-10 00:00:00'),
(8, 'Level 2 :A56', 2, '2015-07-01 00:00:00', '2015-07-02 00:00:00'),
(9, 'Level 2:B6', 2, '2015-07-09 00:00:00', '2015-07-10 00:00:00'),
(10, 'Level 3: C6', 2, '2015-07-09 00:00:00', '2015-07-17 00:00:00'),
(11, 'Level 3: D4', 2, '2015-07-09 00:00:00', '2015-07-17 00:00:00'),
(12, 'Level 3: D55', 2, '2015-07-09 00:00:00', '2015-07-17 00:00:00'),
(13, 'Level 3: E56', 2, '2015-07-09 00:00:00', '2015-07-17 00:00:00'),
(14, 'Level 3: E17', 2, '2015-07-09 00:00:00', '2015-07-17 00:00:00'),
(45, 'Level 3: E56', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(46, 'Level 3: E17', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(47, 'Level 3: G56', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(48, 'Level 3: B17', 3, '2015-07-24 13:39:00', '2015-07-24 15:40:00'),
(49, 'Level 3: A56', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(50, 'Level 3: C17', 3, '2015-07-24 14:03:00', '2015-07-24 15:03:00'),
(51, 'Level 3: E56', 5, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(52, 'Level 3: E17', 5, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(53, 'Level 3: G56', 5, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(54, 'Level 3: B17', 5, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(55, 'Level 3: A56', 5, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(56, 'Level 3: C17', 5, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(57, 'Level 3: E56', 6, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(58, 'Level 3: E17', 6, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(59, 'Level 3: G56', 6, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(60, 'Level 3: B17', 6, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(61, 'Level 3: A56', 6, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(62, 'Level 3: C17', 6, '2015-07-15 15:59:00', '2015-07-16 16:00:00'),
(63, 'Level 3: E56', 7, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(64, 'Level 3: E17', 7, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(65, 'Level 3: G56', 7, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(66, 'Level 3: B17', 7, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(67, 'Level 3: A56', 7, '2015-07-28 20:36:00', '2015-07-28 21:36:00'),
(68, 'Level 3: C17', 7, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(69, 'Level 2: E56', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(70, 'Level 2: E17', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(71, 'Level 2: G56', 3, '2015-07-15 16:38:00', '2015-07-15 17:39:00'),
(72, 'Level 2: B17', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(73, 'Level 2: A56', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(74, 'Level 2: C17', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(75, 'Level 4: E56', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(76, 'Level 4: E17', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(77, 'Level 4: G56', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(78, 'Level 4: B17', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(79, 'Level 4: A56', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00'),
(80, 'Level 4: C17', 3, '2015-07-09 00:00:00', '2015-07-14 00:00:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `freeparkinglots`
--
ALTER TABLE `freeparkinglots`
  ADD PRIMARY KEY (`id`);

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
-- AUTO_INCREMENT for table `freeparkinglots`
--
ALTER TABLE `freeparkinglots`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT for table `parkinglots`
--
ALTER TABLE `parkinglots`
  MODIFY `parkinglotsid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `parkingspots`
--
ALTER TABLE `parkingspots`
  MODIFY `parkingspotsid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=81;
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
