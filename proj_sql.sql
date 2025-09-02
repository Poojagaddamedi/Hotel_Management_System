
CREATE DATABASE hotelsoft;

USE hotelsoft;



CREATE TABLE `plantype`(
  `plan_id` int(200) NOT NULL AUTO_INCREMENT,
  `plan_name` varchar(2000) NOT NULL,
  `planrate` double NOT NULL,
  PRIMARY KEY (`plan_id`),
  UNIQUE KEY `plantypeid` (`plan_id`)
);

insert into plantype values 
    (1,'Single Occupancy',1000),
    (2,'Double Occupancy',1500),
    (3,'Triple Occupancy',2000),
    (4,'Quadruple Occupancy',2500),
    (5,'Dormitory',500);

-- alteration in roomtype table as compared to orignal db ;



CREATE TABLE `roomtype`(
    `roomtype_id` int(11) NOT NULL AUTO_INCREMENT,
    `roomtype_name` varchar(2000) NOT NULL,
    `roomtype_description` varchar(2000) DEFAULT NULL,
    PRIMARY KEY (`roomtype_id`),
    UNIQUE KEY `roomtypeid` (`roomtype_id`)

);

insert into roomtype values
    (1,'Single Room','Single Room with single bed'),
    (2,'Double Room','Double Room with double bed'),
    (3,'Triple Room','Triple Room with three beds'),
    (4,'Quadruple Room','Quadruple Room with four beds'),
    (5,'Dormitory','Dormitory with multiple beds');


create table `arrivalmode`(
   `arr_id` int(11) NOT NULL,
  `arrivalmode` varchar(45) NULL DEFAULT NULL,
  
  PRIMARY KEY (`arr_id`)  
);

-- optional insertion to be discussed with the client;

insert into arrivalmode values
    (1,'Walk In'),
    (2,'Online Booking'),
    (3,'Phone Booking'),
    (4,'Travel Agent'),
    (5,'Corporate Booking');

    

create table `refmode`(
  `ref_id` int(11) NOT NULL,
  `refmode` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ref_id`)
);

-- optional insertion to be discussed with the client;
insert into refmode values
    (1,'Friend'),
    (2,'Family'),
    (3,'Online Search'),
    (4,'Travel Agent'),    
    (5,'Corporate Booking'),
    (6,'Walk In');



CREATE TABLE settlement_mode(
  `mode_id` int(11) NOT NULL AUTO_INCREMENT,
  `mode_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`mode_id`),
  UNIQUE KEY `mode_name_UNIQUE` (`mode_name`)
);



create table `userType`(
    `user_type_id` int(11) NOT NULL AUTO_INCREMENT,
    `user_type_name` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`user_type_id`)
);

insert into userType values
    (1,'Admin'),
    (2,'Manager'),
    (3,'Receptionist'),
    (4,'Housekeeping'),
    (5,'Maintenance'),
    (6,'Accounting'),
    (7,'Guest'),
    (8,'Staff');



create table nationality(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `country_name` varchar(255) DEFAULT NULL,
    `country_code` varchar(10) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `country_name_UNIQUE` (`country_name`)
);


CREATE TABLE `fo_bill_settlement_types` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `settlement_type` VARCHAR(45) NOT NULL,  -- e.g., 'Cash', 'Credit Card', etc.
  PRIMARY KEY (`id`)
);

INSERT into `fo_bill_settlement_types` (`id`, `settlement_type`) VALUES
(1, 'Cash'),
(2, 'Credit Card'),
(3, 'Debit Card'),
(4, 'Online Transfer'),
(5, 'Cheque'),
(6, 'Other');


CREATE TABLE `resv_mode`(
  `res_id` int(11) NOT NULL,
  `res_source` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`res_id`)
);


-- optional insertion to be discussed with the client;
insert into resv_mode values
    (1,'Website'),
    (2,'Mobile App'),
    (3,'Phone'),
    (4,'Email'),
    (5,'Walk In');



CREATE TABLE `tax_master` (
  `tax_id` INT NOT NULL AUTO_INCREMENT,
  `tax_name` VARCHAR(100),
  `tax_percentage` DECIMAL(5,2) DEFAULT 0,
  PRIMARY KEY (`tax_id`)
);

create table data_flow(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_flow_name` varchar(45) DEFAULT NULL,
  `data_flow_description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `shift` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `no` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `audit_date` datetime DEFAULT NULL,
  `opening_balance` double DEFAULT NULL,
  `closing_balance` double DEFAULT NULL,
  `total_income` double DEFAULT NULL,
  `total_expense` double DEFAULT NULL,
  PRIMARY KEY (`id`),
    UNIQUE KEY `shift_no_UNIQUE` (`no`)
);



CREATE TABLE `credit_card` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `company_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `hotel_account_head` (
  `acc_head_id` INT NOT NULL AUTO_INCREMENT,
  `acc_head_name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`acc_head_id`)
);



CREATE TABLE `notification_log` (
  `notification_id` INT NOT NULL AUTO_INCREMENT,
  `guest_id` INT,
  `type` ENUM('SMS', 'Email') NOT NULL,
  `status` ENUM('Sent', 'Failed', 'Pending') NOT NULL,
  `sent_date` DATETIME,
  `message_content` TEXT,
  PRIMARY KEY (`notification_id`),
);

CREATE TABLE `housekeeping` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `room_no` VARCHAR(8),
  `cleaning_status` ENUM('Clean', 'Dirty', 'In Progress') NOT NULL,
  `last_cleaned_date` DATETIME,
  `staff_id` INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`room_no`) REFERENCES `room`(`room_no`),
  FOREIGN KEY (`staff_id`) REFERENCES `users`(`id`)
);

-- CREATING TABLES WITHOUT FOREIGN KEY WILL ALTER THEN AT THE END -- 

create table `checkout`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `booking_id` varchar(45) DEFAULT NULL,
  `bill_no` varchar(45) DEFAULT NULL,
  `payment_clear` tinyint(4) DEFAULT NULL,
  `remark` longtext,
  `folio_no` varchar(200) DEFAULT NULL,
  `room_discount_percentage` double DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
   Primary Key (`id`)
);

CREATE TABLE `account_year` (
  `acc_year` varchar(5) NOT NULL,
  `h_folio_no` varchar(45) DEFAULT NULL,
  `h_reservation_no` varchar(45) DEFAULT NULL,
  `h_bill_no` varchar(45) DEFAULT NULL,
  `h_receipt_no` varchar(45) DEFAULT NULL,
  `start_year` varchar(10) DEFAULT NULL,
  `end_year` varchar(10) DEFAULT NULL,
  `report_email` varchar(45) DEFAULT NULL,
  `report_pdf` varchar(45) DEFAULT NULL,
  `report_others` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`acc_year`)
);



create table `post_transaction`(
  
`id` int(11) NOT NULL AUTO_INCREMENT,
`room_no` varchar(45) DEFAULT NULL,
`audit_date` DATE DEFAULT NULL, 
`guest_name` varchar(100) DEFAULT NULL,
`folio_no` varchar(45) DEFAULT NULL,
`trans_date` date DEFAULT NULL,
`acc_head` varchar(100) DEFAULT NULL,
`voucher_no` varchar(45) DEFAULT NULL,
`amount` double DEFAULT NULL,
`narration` varchar(255) DEFAULT NULL,
`bill_no` varchar(45) DEFAULT NULL,
`user_id` int(11) DEFAULT NULL,
PRIMARY KEY (`id`)
);




CREATE TABLE `hotelsoftusers` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_token` varchar(255) DEFAULT NULL,
  `creation_date` date DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `sha_password` blob,
  `user_name` varchar(255) DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL,
  `branch_id` int(11) DEFAULT '0',
  `client_id` int(11) DEFAULT '0',
  `division_id` int(11) DEFAULT '0',
  `session_token` varchar(200) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
);




CREATE TABLE `checkin`(
  `checkin_id` int(11) NOT NULL AUTO_INCREMENT,
  `folio_no` varchar(15) DEFAULT NULL,
  `reservation_no` varchar(15) DEFAULT NULL,
  `guest_name` varchar(45) not null,
  `check_in_date` date ,
  `check_out_date` date ,
  `no_of_persons` INT NOT NULL DEFAULT '1',
  `audit_date` DATE DEFAULT NULL,
  `company_name` varchar(45) DEFAULT NULL,
  `no_of_rooms` INT DEFAULT NULL,
  `total_pax` int(11) DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `tax` double DEFAULT NULL,
  `is_tax_inclusive` tinyint(1) DEFAULT NULL,
  `is_reserved` tinyint(1) DEFAULT NULL,
  `selected_room` varchar(45) NOT NULL  ,
  `total_amt` double DEFAULT NULL,
  `contact_no` varchar(45) NOT NULL,
  `email_id` varchar(45) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `purpose` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `bill_no` varchar(15) DEFAULT NULL,
  `id_proof` longtext,
  `arrival_time` time DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`checkin_id`)
);


CREATE TABLE `advances` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reservation_no` varchar(45) DEFAULT NULL,
  `audit_date` DATE DEFAULT NULL, 
  `adv_date` datetime DEFAULT NULL,               
  `receipt_no` varchar(200) DEFAULT NULL,
  `settlement_mode` varchar(45) DEFAULT NULL,   
  `amount` DOUBLE NOT NULL DEFAULT '0',
  `credit_card_company` varchar(45) DEFAULT NULL,
  `credit_card_no` varchar(45) DEFAULT NULL,     
  `narration` longtext,
  `folio_no` varchar(45) DEFAULT NULL,
  `shift_no` int(11) DEFAULT NULL,
  `shift_date` date DEFAULT NULL,
  `room_no` varchar(45) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `bill_no` varchar(45) DEFAULT NULL, 
  PRIMARY KEY (`id`)             
);



create table reservation(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reservation_no` varchar(15) DEFAULT NULL,
  `from_date` date NOT NULL,
  `to_date` date NOT NULL,
  `audit_date` DATE DEFAULT NULL, 
  `customer_id` int(11) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `no_of_rooms` INT   NOT NULL DEFAULT '1',
  `contact_no` varchar(45) NOT NULL,
  `email_id` varchar(45) DEFAULT NULL,
  `guest_name` varchar(45)  NOT NULL,
  `total_pax` int(11) NOT NULL,
  `rate` double DEFAULT NULL,
  `tax` double DEFAULT NULL,
  `with_tax` varchar(1) DEFAULT 'Y',
  `is_tax_inclusive` tinyint(1) DEFAULT '0',
  `total_amt` double DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `purpose` varchar(255) DEFAULT NULL,
  `status` varchar(45) DEFAULT '0' COMMENT '0 - Booked 1 - Cancelled',
  `selected_room` varchar(45) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `is_checkin_done` tinyint(1) DEFAULT NULL,
  `nationality` int(11) DEFAULT NULL,
  `external_ref_no` varchar(200) DEFAULT NULL,
  `rooms_check_in` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);





CREATE TABLE `hmssystem` (
    `id` varchar(45) NOT NULL,
  `hotelname` longtext NOT NULL,
  `accyear` varchar(200) NOT NULL,
  `auditdate` date NOT NULL,
  `shiftno` int(11) NOT NULL,
  `shiftdate` date NOT NULL,
  `logo` longtext NOT NULL,
  `printkot` varchar(10) NOT NULL,
  `default_file_store` longtext ,
  `bill_title_1` longtext DEFAULT NULL,
  `bill_title_2` longtext DEFAULT NULL,
  `bill_title_3` varchar(200) DEFAULT NULL,
  `receiver_email` varchar(200) DEFAULT NULL,
  `sender_email` varchar(200) DEFAULT NULL,
  `sender_password` varchar(45) DEFAULT NULL,
  `gst_no` varchar(45) DEFAULT NULL,
  `vat_no` varchar(45) DEFAULT NULL,
  `fssai_no` varchar(45) DEFAULT NULL,
  `rest_msg` varchar(200) DEFAULT NULL,
  `sms_sender_number` varchar(45) DEFAULT NULL,
  `sms_alert_number` varchar(200) DEFAULT NULL,
  `sms_api_key` varchar(45) DEFAULT NULL,
  `sms_url` longtext,
  `sms_balance` int(11) DEFAULT '0',
  `bill_copy` int(11) DEFAULT '0',
  `h_reservation_no` int(11) DEFAULT NULL,
  `h_folio_no` int(11) DEFAULT '0',
  `whatsapp_uid` varchar(200) DEFAULT NULL,
  `whatsapp_auth` varchar(200) DEFAULT NULL,
  `legacy_db_path` longtext,
  `is_legacy_pos_on` tinyint(4) DEFAULT NULL,
  `c_ui_base_url` varchar(200) DEFAULT NULL,
  `welcome_message` longtext,
  `pos_url` varchar(200) DEFAULT NULL,
  `tin_no` varchar(200) DEFAULT NULL,
  `reg_form_1` longtext,
  `reg_form_2` longtext,
  `s3_bucket_name` varchar(200) DEFAULT NULL,
  `s3_bucket_key` longtext,
  `total_shift` int(11) DEFAULT NULL,
  `delivery_charges` double DEFAULT NULL,
  `h_bill_no` int(11) DEFAULT NULL,
  `h_receipt_no` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
);



create table fo_bill(

  `bill_no` int(11) NOT NULL AUTO_INCREMENT,
  `folio_no` varchar(15) DEFAULT NULL,
  `audit_date` DATE DEFAULT NULL,
  `bill_date` datetime NOT NULL,
  `print_date` datetime NOT NULL,
  `print_time` time NOT NULL,
  `settlement_type` int(11) DEFAULT NULL,
  `amount` double NOT NULL,
  `room_discount` double DEFAULT NULL,
  `room_discount_amount` double DEFAULT NULL,
  `refund_amount` double DEFAULT NULL,
  `full_discount` double DEFAULT NULL,
  `full_discount_amount` double DEFAULT NULL,
  `receipt_no` varchar(200) DEFAULT NULL,
  `ref_voc_no` varchar(200) DEFAULT NULL,
  `guest_name` varchar(200) DEFAULT NULL,
  `room_no` varchar(45) NOT NULL,
  `total_advance` double DEFAULT '0',
  `amount_payable` double DEFAULT '0',
  `customer_id` int(11) DEFAULT NULL,
  `txn_details` longtext,
  `advance_details` longtext,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`bill_no`)
);

CREATE TABLE `bill_settlement`(
  `settlement_id` int(11) unique NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(45) NOT NULL,
  `checkin_id` int(11) DEFAULT NULL,   
  `settlement_date` datetime DEFAULT NULL,  
  `reservation_no` varchar(45) DEFAULT NULL,
  `settlement_mode` varchar(45) NOT NULL,
  `settlement_amount` double DEFAULT NULL,
  `tips` double DEFAULT NULL,
  PRIMARY KEY (`settlement_id`)
);


CREATE TABLE `room`(
  `id` int(11) unique NOT NULL AUTO_INCREMENT,
  `floor` int(11) DEFAULT NULL,
  `room_no` varchar(8) DEFAULT NULL,
  `no_of_persons` int(11) DEFAULT NULL,
  `picture` varchar(45) DEFAULT NULL,
  `room_discription` varchar(45) DEFAULT NULL,
  `status` ENUM('VR', 'OD', 'OI') NOT NULL,
  `rate` double DEFAULT NULL,
  `current_folio` varchar(45) DEFAULT NULL,
   PRIMARY KEY (`id`)
);


CREATE TABLE `company`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_name` varchar(255) NOT NULL UNIQUE,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `address3` varchar(255) DEFAULT NULL,
  `pincode` varchar(10) DEFAULT NULL,
  `email_id` varchar(50) DEFAULT NULL,
  `sms_tele_no` varchar(20) DEFAULT NULL,
  `telephone_number2` varchar(20) DEFAULT NULL,
  `gst` varchar(50) DEFAULT NULL,
  `vat` varchar(50) DEFAULT NULL,
  `pan` varchar(50) DEFAULT NULL,
  `schedule_head` varchar(50) DEFAULT NULL,
  `sub_schedule_head` varchar(50) DEFAULT NULL,
  `account_type` int(11) DEFAULT NULL,
  `nationality` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


-- Adding Foreign Keys to the tables created above;


----------------------------------------------for company table   ;

alter table company 
  modify account_type int(11) DEFAULT NULL,
  modify schedule_head varchar(50) DEFAULT NULL;

alter table `company`
  add constraint `fk_company_nationality` FOREIGN KEY (`nationality`) REFERENCES `nationality`(`id`);

alter table `company`
  add constraint `fk_company_account_type` FOREIGN KEY (`account_type`) REFERENCES `account_year`(`acc_year`);

-- Add index to post_transaction.acc_head before adding FK
CREATE INDEX idx_post_transaction_acc_head ON post_transaction(acc_head);

alter table `company`
  add constraint `fk_company_schedule_head` FOREIGN KEY (`schedule_head`) REFERENCES `post_transaction`(`acc_head`);


 -------------------------------------- -- for room table ;
CREATE INDEX idx_checkin_folio_no ON checkin(folio_no);

alter table `room`
  add constraint `fk_room_current_folio` FOREIGN KEY (`current_folio`) REFERENCES `checkin`(`folio_no`);


--------------------------------------- for bill_settlement table ;
alter table `bill_settlement`
  add constraint `fk_bill_settlement_bill_no` FOREIGN KEY (`bill_no`) REFERENCES `fo_bill`(`bill_no`);
alter table `bill_settlement`
  add constraint `fk_bill_settlement_settlement_mode` FOREIGN KEY (`settlement_mode`) REFERENCES `fo_bill_settlement_types`(`id`);
alter table `bill_settlement`
  add constraint `fk_bill_settlement_checkin_id` FOREIGN KEY (`checkin_id`) REFERENCES `checkin`(`checkin_id`);
CREATE INDEX idx_reservation_reservation_no ON reservation(reservation_no);
alter table `bill_settlement`
  add constraint `fk_bill_settlement_reservation_no` FOREIGN KEY (`reservation_no`) REFERENCES `reservation`(`reservation_no`);


------------------------------------- for fo_bill table ;
alter table `fo_bill`
  modify `folio_no` varchar(15) DEFAULT NULL,
  modify `settlement_type` int(11) DEFAULT NULL,
  modify `user_id` int(11) DEFAULT NULL,
  modify `customer_id` int(11) DEFAULT NULL;
alter table `fo_bill`
  add constraint `fk_fo_bill_folio_no` FOREIGN KEY (`folio_no`) REFERENCES `checkin`(`folio_no`);
alter table `fo_bill`
  add constraint `fk_fo_bill_settlement_type` FOREIGN KEY (`settlement_type`) REFERENCES `fo_bill_settlement_types`(`id`);
alter table `fo_bill`
  add constraint `fk_fo_bill_user_id` FOREIGN KEY (`user_id`) REFERENCES `hotelsoftusers`(`user_id`);
alter table `fo_bill` 
  add constraint `fk_fo_bill_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `company`(`id`);

------------------------------------- for hmssystem  table ;
alter table `hmssystem`
  modify `h_reservation_no` int(11) DEFAULT NULL,
  modify `h_folio_no` int(11) DEFAULT '0',
  modify `h_bill_no` int(11) DEFAULT NULL,
  modify `h_receipt_no` bigint(20) DEFAULT NULL;

ALTER TABLE `hmssystem`
  ADD CONSTRAINT `fk_hmssystem_h_reservation_no` FOREIGN KEY (`h_reservation_no`) REFERENCES `reservation`(`id`),
  ADD CONSTRAINT `fk_hmssystem_h_folio_no` FOREIGN KEY (`h_folio_no`) REFERENCES `checkin`(`checkin_id`),
  ADD CONSTRAINT `fk_hmssystem_h_bill_no` FOREIGN KEY (`h_bill_no`) REFERENCES `bill_settlement`(`settlement_id`),
  ADD CONSTRAINT `fk_hmssystem_h_receipt_no` FOREIGN KEY (`h_receipt_no`) REFERENCES `advances`(`id`);


-------------------------------------- for reservation table ;
 
alter TABLE `reservation`
  MODIFY `customer_id` int(11) DEFAULT NULL,
  MODIFY `nationality` int(11) DEFAULT NULL,
  MODIFY `user_id` int(11) DEFAULT NULL;
ALTER TABLE `reservation`
  ADD CONSTRAINT `fk_reservation_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `company`(`id`),
  ADD CONSTRAINT `fk_reservation_nationality` FOREIGN KEY (`nationality`) REFERENCES `nationality`(`id`),
  ADD CONSTRAINT `fk_reservation_user_id` FOREIGN KEY (`user_id`) REFERENCES `hotelsoftusers`(`user_id`);


-----------------------for advances table -------------
CREATE INDEX idx_bill_settlement_settlement_mode ON bill_settlement(settlement_mode);

CREATE INDEX idx_bill_settlement_bill_no ON bill_settlement(bill_no);

alter table `advances`
  ADD CONSTRAINT `fk_advances_reservation_no` FOREIGN KEY (`reservation_no`) REFERENCES `reservation`(`reservation_no`),
  ADD CONSTRAINT `fk_advances_settlement_mode` FOREIGN KEY (`settlement_mode`) REFERENCES `bill_settlement`(`settlement_mode`),
  ADD CONSTRAINT `fk_advances_folio_no` FOREIGN KEY (`folio_no`) REFERENCES `checkin`(`folio_no`),
  ADD CONSTRAINT `fk_advances_user_id` FOREIGN KEY (`user_id`) REFERENCES `checkin`(`checkin_id`),
  ADD CONSTRAINT `fk_advances_bill_no` FOREIGN KEY (`bill_no`) REFERENCES `bill_settlement`(`bill_no`);


-----------------------for checkin table -------------
 
alter table `checkin`
  ADD CONSTRAINT `fk_checkin_reservation_no` FOREIGN KEY (`reservation_no`) REFERENCES `reservation`(`reservation_no`),
  ADD CONSTRAINT `fk_checkin_bill_no` FOREIGN KEY (`bill_no`) REFERENCES `bill_settlement`(`bill_no`);


--- --------------for checkout table -------------
ALTER TABLE `checkout`
  ADD CONSTRAINT `fk_checkout_folio_no` FOREIGN KEY (`folio_no`) REFERENCES `checkin`(`folio_no`),
  ADD CONSTRAINT `fk_checkout_bill_no` FOREIGN KEY (`bill_no`) REFERENCES `bill_settlement`(`bill_no`);

-----------------for account year table -----------------
CREATE INDEX idx_advances_receipt_no ON advances(receipt_no);

alter table `account_year`
  ADD CONSTRAINT `fk_account_year_h_folio_no` FOREIGN KEY (`h_folio_no`) REFERENCES `checkin`(`folio_no`),
  ADD CONSTRAINT `fk_account_year_h_reservation_no` FOREIGN KEY (`h_reservation_no`) REFERENCES `reservation`(`reservation_no`),
  ADD CONSTRAINT `fk_account_year_h_bill_no` FOREIGN KEY (`h_bill_no`) REFERENCES `bill_settlement`(`bill_no`),
  ADD CONSTRAINT `fk_account_year_h_receipt_no` FOREIGN KEY (`h_receipt_no`) REFERENCES `advances`(`receipt_no`);


-------------for post_transaction table 
CREATE INDEX idx_hotel_account_head_acc_head_name ON hotel_account_head(acc_head_name);

alter table `post_transaction`
  add constraint `fk_post_transaction_folio_no` FOREIGN KEY (`folio_no`) REFERENCES `checkin`(`folio_no`),
  add constraint `fk_post_transaction_bill_no` FOREIGN KEY (`bill_no`) REFERENCES `bill_settlement`(`bill_no`),
  add constraint `fk_post_transaction_user_id` FOREIGN KEY (`user_id`) REFERENCES `hotelsoftusers`(`user_id`),
  add constraint `fk_post_transaction_acc_head` FOREIGN KEY (`acc_head`) REFERENCES `hotel_account_head`(`acc_head_name`);


----------------- for hotelsoftuser ----------
CREATE INDEX idx_usertype_user_type_name ON userType(user_type_name);

alter table `hotelsoftusers`
ADD CONSTRAINT `fk_hotelsoftusers_client_id` FOREIGN KEY (`client_id`) REFERENCES `company`(`id`),
ADD CONSTRAINT `fk_hotelsoftusers_user_type` FOREIGN KEY (`user_type`) REFERENCES `userType`(`user_type_name`);

   
------------------ for notification_log table -------------
ALTER TABLE `notification_log`
  ADD CONSTRAINT `fk_notification_log_guest_id` FOREIGN KEY (`guest_id`) REFERENCES `checkin`(`checkin_id`);
 
 ------------------------- ADDING INDEXES TO THE TABLES ---------------------------------------


 
-- Index on reservation.reservation_no for faster lookup in reservation-related queries
CREATE INDEX idx_reservation_no ON reservation(reservation_no);

-- Index on checkin.folio_no for efficient check-in and billing operations
CREATE INDEX idx_folio_no ON checkin(folio_no);

-- Index on fo_bill.bill_no for quick bill retrieval and modifications
CREATE INDEX idx_bill_no ON fo_bill(bill_no);

-- Index on room.room_no for faster room status and assignment queries
CREATE INDEX idx_room_no ON room(room_no);

-- Index on advances.reservation_no for linking advances to reservations
CREATE INDEX idx_advances_reservation_no ON advances(reservation_no);

-- Index on advances.folio_no for linking advances to check-ins
CREATE INDEX idx_advances_folio_no ON advances(folio_no);

-- Index on advances.bill_no for linking advances to bills
CREATE INDEX idx_advances_bill_no ON advances(bill_no);

-- Index on post_transaction.folio_no for retrieving guest expenses
CREATE INDEX idx_post_transaction_folio_no ON post_transaction(folio_no);

-- Index on post_transaction.bill_no for bill-related transactions
CREATE INDEX idx_post_transaction_bill_no ON post_transaction(bill_no);

-- Index on checkin.guest_name for guest search functionality
CREATE INDEX idx_checkin_guest_name ON checkin(guest_name);

-- Index on reservation.guest_name for reservation search
CREATE INDEX idx_reservation_guest_name ON reservation(guest_name);

-- Index on reservation.contact_no for searching by mobile number
CREATE INDEX idx_reservation_contact_no ON reservation(contact_no);

-- Index on checkin.contact_no for searching by mobile number
CREATE INDEX idx_checkin_contact_no ON checkin(contact_no);

CREATE INDEX idx_guest_details_reservation_no ON reservation(reservation_no);



----------------------------------------

alter table roomtype
  modify roomtype_name varchar(2000) NOT NULL,
  modify roomtype_description varchar(2000) DEFAULT NULL,
  add column roomtype_rate double DEFAULT NULL;

----------------------------------------

alter table shift
 add column shiftStatus varchar(45) DEFAULT NULL;  --//It stores the status of a shift (for example, "Open", "Closed", "In Progress").

 add column shiftName varchar(200) DEFAULT NULL; --It stores the name of a shift (e.g., "Morning", "Evening", "Night").

---------------------------------

alter table hotel_account_head
  add column acc_head_description varchar(200) DEFAULT NULL; --//It stores a description of the account head for better understanding.

------------------------------

alter table post_transaction
  add COLUMN TRANSACTION_STATUS ENUM('Pending', 'Completed', 'Failed') NOT NULL DEFAULT 'Pending';

--------------------------

  ALTER TABLE hotelsoftusers
    ADD COLUMN user_email varchar(200) NOT NULL; --//It stores the email address of the user for communication purposes.
 ALTER TABLE hotelsoftusers ADD COLUMN contactNo varchar(20) NOT NULL; --//It stores the phone number
ALTER TABLE hotelsoftusers
  ADD COLUMN user_address varchar(255) DEFAULT NULL; --//It stores the address of
ALTER TABLE hotelsoftusers
  ADD COLUMN user_status ENUM('Active', 'Inactive') NOT NULL DEFAULT 'Active'; --//It stores the status of the user account, indicating whether it is active or inactive.
ALTER TABLE hotelsoftusers
  ADD COLUMN user_image longtext DEFAULT NULL; --//It stores the profile picture of the user in a long text format, allowing for larger image data storage.
ALTER TABLE hotelsoftusers
  ADD COLUMN user_description longtext DEFAULT NULL; --//It stores a detailed description of the user, which can include additional information about their role or responsibilities.


ALTER TABLE hotelsoftusers MODIFY client_id INT NULL;
ALTER TABLE hotelsoftusers MODIFY division_id INT NULL;
----------------------------

 alter table Room
  add column roomtype_id int(11) DEFAULT NULL; --//It stores the ID of the room type associated with the room.