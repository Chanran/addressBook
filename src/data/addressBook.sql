-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 2016-06-09 14:56:57
-- 服务器版本： 5.5.49-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `addressBook`
--

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL COMMENT 'user id',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '加密盐'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `name`, `password`, `salt`) VALUES
(1, 'test', 'eecef6880f91ad90608a9dd18b6c199b', '8SLQo1RC2ZvqnDd0HlXm58ZXZiuPYPd7'),
(5, 'lancan', 'df0822c180a6fbad2df2b7aef351f824', 'n7Ovp3j2qca4r5HrVadmLQUwR7V6Hz62'),
(6, 'blue', '934684be9ef214e8b9604431a6b1485c', 'WYorMh8DtVuJquNBirDlqHKtVoRLlvU0'),
(7, 'testtest', 'dce09d7a30383141f146d57495e2b1e8', 'vM7DiRe0JcH7uL5yhVRafBLnnJPezJPX');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'user id', AUTO_INCREMENT=8;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
