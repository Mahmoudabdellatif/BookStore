-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 08, 2017 at 08:58 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 7.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `book_store`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `top_10_selling_books` ()  Select book.ISBN, title, SUM(purchases_count) AS total from book, customer_purchases where book.ISBN = customer_purchases.ISBN
						group by customer_purchases.ISBN 
						ORDER BY total DESC$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `top_5_customers` ()  Select F_name, L_name, SUM(purchases_count) AS total from user, customer_purchases where user.user_name = customer_purchases.user_name 
						group by customer_purchases.user_name 
						ORDER BY total DESC$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `total_sales` ()  SELECT customer_purchases.ISBN, SUM(purchases_count) as total, book.price, book.title   
FROM   customer_purchases, book 
WHERE  cp_date > (NOW() - INTERVAL 1 MONTH) AND book.ISBN = customer_purchases.ISBN
group by customer_purchases.ISBN$$

--
-- Functions
--
CREATE DEFINER=`root`@`localhost` FUNCTION `add_author` (`author_name` VARCHAR(40)) RETURNS BIT(1) BEGIN

INSERT INTO author VALUES((SELECT MAX(book.ISBN) from book ), author_name);

RETURN 1;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `add_book` (`title` VARCHAR(40), `publisher_name` VARCHAR(40), `p_year` INT, `price` INT, `category_key` VARCHAR(1), `no_of_copies` INT, `threshold` INT) RETURNS BIT(1) BEGIN


INSERT INTO book(
title,
no_of_copies,
price,
publisher_name,
category_key,
p_year,
threshold
) VALUES (
title,
no_of_copies,
price,
publisher_name,
category_key,
p_year,
threshold
);


RETURN 1;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `add_publisher` (`p_name` VARCHAR(40), `address` VARCHAR(40), `tel_no` VARCHAR(20)) RETURNS BIT(1) BEGIN

INSERT INTO publisher (
p_name,
address,
tel_no
)VALUES(
p_name,
address,
tel_no
);


RETURN 1;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `buy_book` (`ISBN` INT, `quantity` INT, `username` VARCHAR(40), `date` DATE) RETURNS TINYINT(1) BEGIN

Update Book set no_of_copies = no_of_copies - quantity where Book.ISBN = ISBN;

insert into customer_purchases (user_name, ISBN, cp_date, purchases_count) values(username, ISBN, date, quantity)
on duplicate key update purchases_count = (customer_purchases.purchases_count + quantity);



RETURN TRUE;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `author`
--

CREATE TABLE `author` (
  `ISBN` int(11) NOT NULL,
  `A_name` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `author`
--

INSERT INTO `author` (`ISBN`, `A_name`) VALUES
(10, 'ne3ma'),
(10, 'shady');

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `ISBN` int(11) NOT NULL,
  `title` varchar(40) NOT NULL,
  `publisher_name` varchar(40) NOT NULL,
  `p_year` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `category_key` varchar(1) NOT NULL,
  `no_of_copies` int(11) NOT NULL,
  `threshold` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`ISBN`, `title`, `publisher_name`, `p_year`, `price`, `category_key`, `no_of_copies`, `threshold`) VALUES
(1, 'title1', '7oda', 2014, 5, 'A', 20, 5),
(2, 'title2', '7oda', 2015, 30, 'G', 10, 2),
(3, 'title3', '7oda', 2014, 70, 'H', 80, 10),
(4, 'title4', '7oda', 2015, 45, 'R', 50, 15),
(5, 'title5', '7oda', 2017, 60, 'R', 15, 3),
(10, '7oda', '7oda', 2017, 20, 'S', 100, 3);

--
-- Triggers `book`
--
DELIMITER $$
CREATE TRIGGER `modifyExistingBook` BEFORE UPDATE ON `book` FOR EACH ROW BEGIN
      IF (NEW.no_of_copies < 0) THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'canot insert negative number in no_of_copies column';
      
      END IF;
    END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `placeOrdersOnBooks` AFTER UPDATE ON `book` FOR EACH ROW BEGIN
      IF (NEW.no_of_copies < NEW.threshold) THEN
            INSERT INTO book_order (quantity, ISBN) VALUES(100, NEW.ISBN);
      
      END IF;
    END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `book_order`
--

CREATE TABLE `book_order` (
  `order_no` int(11) NOT NULL,
  `ISBN` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `acceptance` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `book_order`
--

INSERT INTO `book_order` (`order_no`, `ISBN`, `quantity`, `acceptance`) VALUES
(3, 2, 20, 1);

--
-- Triggers `book_order`
--
DELIMITER $$
CREATE TRIGGER `confirmOrder` BEFORE DELETE ON `book_order` FOR EACH ROW BEGIN
	 
     IF (OLD.acceptance IS TRUE) THEN
            UPDATE book SET no_of_copies = no_of_copies + OLD.quantity WHERE book.ISBN = OLD.ISBN;
      
      END IF;
     
     
      
      
    END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `c_name` varchar(40) NOT NULL,
  `c_key` varchar(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`c_name`, `c_key`) VALUES
('Art', 'A'),
('Geography', 'G'),
('History', 'H'),
('Religion', 'R'),
('Science', 'S');

-- --------------------------------------------------------

--
-- Table structure for table `customer_purchases`
--

CREATE TABLE `customer_purchases` (
  `user_name` varchar(40) NOT NULL,
  `ISBN` int(11) NOT NULL,
  `cp_date` date NOT NULL,
  `purchases_count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer_purchases`
--

INSERT INTO `customer_purchases` (`user_name`, `ISBN`, `cp_date`, `purchases_count`) VALUES
('ali', 3, '2017-05-01', 2),
('ali', 10, '2017-05-08', 20),
('mahmoud', 2, '2017-05-04', 1),
('mahmoud', 3, '2017-05-02', 1),
('shazy', 2, '2017-05-07', 4),
('shazy', 3, '2017-05-03', 5);

-- --------------------------------------------------------

--
-- Table structure for table `publisher`
--

CREATE TABLE `publisher` (
  `p_name` varchar(40) NOT NULL,
  `address` varchar(100) NOT NULL,
  `tel_no` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `publisher`
--

INSERT INTO `publisher` (`p_name`, `address`, `tel_no`) VALUES
('7oda', 'elbazar streer', '01123387088');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_name` varchar(40) NOT NULL,
  `password` varchar(20) NOT NULL,
  `L_name` varchar(40) NOT NULL,
  `F_name` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `phone_num` varchar(40) DEFAULT NULL,
  `shipping_address` varchar(40) NOT NULL,
  `adminstration` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_name`, `password`, `L_name`, `F_name`, `email`, `phone_num`, `shipping_address`, `adminstration`) VALUES
('ali', '4545', 'ad', 'ac', 'aaa', '5468', 'gfjhkl', 0),
('mahmoud', '123', 'a', 'b', 'd@g.com', '123321', 'gfdh,.kl', 0),
('shazy', 'shady', 'farahat', 'shady', 'shady@a.com', '123', '381 malak', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `author`
--
ALTER TABLE `author`
  ADD PRIMARY KEY (`ISBN`,`A_name`);

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`ISBN`),
  ADD KEY `category_key` (`category_key`),
  ADD KEY `publisher_name` (`publisher_name`);

--
-- Indexes for table `book_order`
--
ALTER TABLE `book_order`
  ADD PRIMARY KEY (`order_no`),
  ADD KEY `ISBN` (`ISBN`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`c_key`);

--
-- Indexes for table `customer_purchases`
--
ALTER TABLE `customer_purchases`
  ADD PRIMARY KEY (`user_name`,`ISBN`,`cp_date`),
  ADD KEY `ISBN` (`ISBN`);

--
-- Indexes for table `publisher`
--
ALTER TABLE `publisher`
  ADD PRIMARY KEY (`p_name`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_name`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `ISBN` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `book_order`
--
ALTER TABLE `book_order`
  MODIFY `order_no` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `author`
--
ALTER TABLE `author`
  ADD CONSTRAINT `author_ibfk_1` FOREIGN KEY (`ISBN`) REFERENCES `book` (`ISBN`);

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `book_ibfk_1` FOREIGN KEY (`category_key`) REFERENCES `category` (`c_key`),
  ADD CONSTRAINT `book_ibfk_2` FOREIGN KEY (`publisher_name`) REFERENCES `publisher` (`p_name`);

--
-- Constraints for table `book_order`
--
ALTER TABLE `book_order`
  ADD CONSTRAINT `book_order_ibfk_1` FOREIGN KEY (`ISBN`) REFERENCES `book` (`ISBN`);

--
-- Constraints for table `customer_purchases`
--
ALTER TABLE `customer_purchases`
  ADD CONSTRAINT `customer_purchases_ibfk_1` FOREIGN KEY (`ISBN`) REFERENCES `book` (`ISBN`),
  ADD CONSTRAINT `customer_purchases_ibfk_2` FOREIGN KEY (`user_name`) REFERENCES `user` (`user_name`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
