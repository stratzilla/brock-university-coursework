SET DELIMITER ;;

/**********************************
 *
 * Create Table Code
 *
 **********************************/

-- shop table
CREATE TABLE shop (
	shop_id INT NOT NULL AUTO_INCREMENT,
	shop_name VARCHAR(20) NOT NULL,
	super_id INT NOT NULL, -- supervisor id
	super_name VARCHAR(32) NOT NULL,
	PRIMARY KEY(shop_id)
);;

-- tradesperson table
CREATE TABLE tradesperson (
	emp_id INT NOT NULL AUTO_INCREMENT, -- employee id
	first_name VARCHAR(32) NOT NULL,
	last_name VARCHAR(32) NOT NULL,
	address VARCHAR(64) NOT NULL,
	phone BIGINT UNSIGNED NOT NULL,
	cell BIGINT UNSIGNED DEFAULT NULL,
	email VARCHAR(32) NOT NULL,
	hire_date DATE NOT NULL,
	term_date DATE DEFAULT NULL,
	shop_id INT NOT NULL,
	PRIMARY KEY(emp_id),
	FOREIGN KEY(shop_id) REFERENCES shop(shop_id)
		ON DELETE RESTRICT ON UPDATE CASCADE
);;

-- supplier table
CREATE TABLE supplier (
	sup_id INT AUTO_INCREMENT, -- supplier id
	descr VARCHAR(32) NOT NULL, -- description
	contact VARCHAR(32) NOT NULL,
	city VARCHAR(15) NOT NULL,
	address VARCHAR(64) NOT NULL,
	phone BIGINT UNSIGNED NOT NULL,
	fax BIGINT UNSIGNED NOT NULL,
	PRIMARY KEY(sup_id)	
);;

-- inventory table
CREATE TABLE inventory (
	inv_id VARCHAR(15) NOT NULL, -- inventory id
	type_of ENUM("exp", "ser") NOT NULL, -- for isa
	descr VARCHAR(32) NOT NULL, -- description
	price DECIMAL(6,2) NOT NULL,
	sup_id INT NOT NULL, -- supplier id
	sup_pt INT NOT NULL, -- supplier part
	PRIMARY KEY(inv_id),
	FOREIGN KEY(sup_id) REFERENCES supplier(sup_id)
		ON DELETE RESTRICT ON UPDATE CASCADE
);;

-- expendable inventory (ISA)
CREATE TABLE inventory_exp (
	inv_id VARCHAR(15) NOT NULL, -- inventory id
	backup_id VARCHAR(15) DEFAULT '', -- backup inventory part
	PRIMARY KEY(inv_id),
	FOREIGN KEY(inv_id) REFERENCES inventory(inv_id)
		ON DELETE CASCADE ON UPDATE CASCADE
);;

-- serialized inventory (ISA)
CREATE TABLE inventory_ser (
	inv_id VARCHAR(15) NOT NULL, -- inventory id
	serial VARCHAR(16) NOT NULL,
	PRIMARY KEY(inv_id),
	FOREIGN KEY(inv_id) REFERENCES inventory(inv_id)
		ON DELETE CASCADE ON UPDATE CASCADE
);;

-- warehouse table
CREATE TABLE warehouse (
	bin_id VARCHAR(4) NOT NULL,
	inv_id VARCHAR(15) NOT NULL, -- inventory id
	qty INT DEFAULT 0,
	min_level INT NOT NULL,
	reorder INT NOT NULL,
	CHECK(qty > 0), -- ensure qty never goes to 0
	PRIMARY KEY(bin_id),
	UNIQUE KEY(inv_id),
	FOREIGN KEY(inv_id) REFERENCES inventory(inv_id)
		ON DELETE CASCADE ON UPDATE CASCADE
);;

-- inventory release
CREATE TABLE inventory_release (
	rel_id INT NOT NULL AUTO_INCREMENT, -- release id
	emp_id INT NOT NULL, -- employee id
	-- I didn't add shop_id because it's implied by emp_id
	datec DATE NOT NULL,
	bin_id VARCHAR(4) NOT NULL,
	qty INT NOT NULL,
	PRIMARY KEY(rel_id),
	FOREIGN KEY(emp_id) REFERENCES tradesperson(emp_id)
		ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY(bin_id) REFERENCES warehouse(bin_id)
		ON DELETE RESTRICT ON UPDATE CASCADE
);;

-- purchase order table
CREATE TABLE purchase (
	po_id INT NOT NULL AUTO_INCREMENT, -- purchase order id
	shop_id INT NOT NULL,
	datec DATE NOT NULL,
	sup_id INT NOT NULL, -- supplier id
	status ENUM("open", "on order", "received") NOT NULL,
	PRIMARY KEY(po_id),
	FOREIGN KEY(shop_id) REFERENCES shop(shop_id)
		ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY(sup_id) REFERENCES supplier(sup_id)
		ON DELETE RESTRICT ON UPDATE CASCADE
);;

/**********************************
 *
 * Procedures
 *
 **********************************/

-- procedure for default insertion into inventory
CREATE PROCEDURE insertInventory(tinv_id VARCHAR(15), ttype_of ENUM("exp", "ser"),
								tdescr VARCHAR(32),	tprice DECIMAL(6,2),
								tsup_id INT, tsup_pt INT,
								tbackup_id VARCHAR(15),	tserial INT)
	BEGIN
		-- check if item exists already
		DECLARE temp VARCHAR(15) DEFAULT (
			SELECT inv_id
			FROM inventory
			WHERE descr = tdescr
		);
		-- if item exists already
		IF temp IS NOT NULL THEN
			-- increment warehouse qty
			UPDATE warehouse
			SET qty = qty + 1
			WHERE inv_id = temp;
		ELSE
			-- otherwise insert new inventory item
			INSERT INTO inventory
			VALUES(tinv_id, ttype_of, tdescr, tprice, tsup_id, tsup_pt);
			IF ttype_of = "exp" THEN -- if expendable item
				INSERT INTO inventory_exp
				VALUES(tinv_id, tbackup_id);
			ELSE -- otherwise is serialized item
				INSERT INTO inventory_ser
				VALUES(tinv_id, tserial);
			END IF;
		END IF;
	END;;

/**********************************
 *
 * Triggers
 *
 **********************************/

-- trigger to prevent fired employee fro doing release
-- also checks if enough qty is on hand
CREATE TRIGGER inventoryReleaseBefore BEFORE INSERT ON inventory_release
	FOR EACH ROW
	BEGIN
		-- check for fired employee
		SET NEW.emp_id = IF((
			SELECT term_date
			FROM tradesperson
			WHERE emp_id = NEW.emp_id)
			= '0000-00-00', -- if not fired, date will be this
		-- stop insert by setting emp_id to null if fired
		NEW.emp_id, NULL);
		-- check for sufficient qty
		SET NEW.qty = IF((
			SELECT qty
			FROM warehouse
			WHERE bin_id = NEW.bin_id)
			>= NEW.qty, -- if enough
		-- stop insert by setting qty to null if not enough
		NEW.qty, NULL);
	END;;
	
-- trigger to reduce qty from bins after release
-- automatically makes PO if needed	
CREATE TRIGGER inventoryReleaseAfter AFTER INSERT ON inventory_release
	FOR EACH ROW
	BEGIN
		DECLARE qtyNow INT DEFAULT 0;
		DECLARE need INT DEFAULT 0;
		DECLARE supp INT DEFAULT 0;
		DECLARE shope INT DEFAULT 0;
		UPDATE warehouse
			SET qty = qty - NEW.qty
			WHERE bin_id = NEW.bin_id;
		SET qtyNow = (
			SELECT qty
			FROM warehouse
			WHERE bin_id = NEW.bin_id
		);			
		SET need = (
			SELECT min_level
			FROM warehouse
			WHERE bin_id = NEW.bin_id
		);
		SET supp = (
			SELECT s.sup_id
			FROM supplier s
				INNER JOIN inventory i
				ON i.sup_id = s.sup_id
					INNER JOIN warehouse w
					ON w.inv_id = i.inv_id
			WHERE w.bin_id = NEW.bin_id
		);
		SET shope = (
			SELECT sh.shop_id
			FROM shop sh
				INNER JOIN tradesperson t
				ON sh.shop_id = t.shop_id
			WHERE t.emp_id = NEW.emp_id
		);
		-- check if new PO needs to be made
		IF qtyNow < need THEN
			INSERT INTO purchase
			VALUES('', shope, NEW.datec, supp, "open");
		END IF;
	END;;

-- trigger to restock after PO received
CREATE TRIGGER purchaseOrder BEFORE UPDATE ON purchase
	FOR EACH ROW
	BEGIN
		-- if PO on order
		IF NEW.status = "received" THEN
			-- restock
			UPDATE warehouse w
				SET w.qty = w.qty + w.reorder
				WHERE w.inv_id IN (
					SELECT i.inv_id
					FROM inventory i
						INNER JOIN supplier s
						ON s.sup_id = i.sup_id
							INNER JOIN purchase p
							ON p.sup_id = s.sup_id
					WHERE i.sup_id = NEW.sup_id);		
		END IF;
	END;;
	
-- trigger to remove backup inventory
CREATE TRIGGER removeBackup AFTER DELETE ON inventory
	-- when inventory is removed, make sure
	-- that the tuples with that item as backup
	-- are adjusted so they don't reference
	-- something that doesn't exist
	FOR EACH ROW
	-- but only for exp items
	IF OLD.type_of = "exp" THEN
		UPDATE inventory_exp
			SET backup_id = '' -- set blank
			WHERE backup_id = OLD.inv_id;
	END IF;;

-- trigger to update backup inventory
CREATE TRIGGER updateBackup AFTER UPDATE ON inventory
	-- likewise, if an inventory item changes its id
	-- then any backup referencing it must also be changed
	FOR EACH ROW
	-- but only for exp items
	IF OLD.type_of = "exp" THEN
		UPDATE inventory_exp
			SET backup_id = NEW.inv_id
			WHERE backup_id = OLD.inv_id;
	END IF;;
	
/**********************************
 *
 * Table Views for Reports
 *
 **********************************/

-- view to see inventory, qty, and value
CREATE VIEW inventory_report AS
	SELECT i.inv_id AS "SKU", i.descr AS "Item Name",
	i.price AS "Item Price", w.qty AS "On Hand",
	(i.price*w.qty) AS "Total Value"
		FROM inventory i
			LEFT JOIN warehouse w
			ON i.inv_id = w.inv_id
		ORDER BY "SKU" ASC;;

-- view to see inventory sold on a day basis
CREATE VIEW inventory_sold AS
	SELECT r.datec AS "Date", i.descr AS "Item Name",
	CONCAT(t.first_name, ' ', t.last_name) AS "Sold To"
		FROM inventory_release r
			INNER JOIN warehouse w
			ON w.bin_id = r.bin_id
				INNER JOIN inventory i
				ON w.inv_id = i.inv_id
				INNER JOIN tradesperson t
				ON t.emp_id = r.emp_id
		ORDER BY "Sold To" ASC;;
		
-- view to see faxable report
CREATE VIEW purchase_fax AS
	SELECT p.po_id AS "PO No.", s.descr AS "Supplier",
	s.fax AS "Fax To"
		FROM purchase p
			INNER JOIN supplier s
			ON p.sup_id = s.sup_id
		ORDER BY "PO No." ASC;;

-- view to see items on order		
CREATE VIEW ordered_items AS
	SELECT i.inv_id AS "SKU", i.descr AS "Part Name",
	s.descr AS "Supplier"
		FROM inventory i
			INNER JOIN supplier s
			ON s.sup_id = i.sup_id
				INNER JOIN purchase p
				ON p.sup_id = s.sup_id
		WHERE p.status = "on order"
		ORDER BY "Supplier" ASC, "SKU" ASC;;
		
-- view to see release receipt
CREATE VIEW release_receipt AS
	SELECT r.rel_id AS "Release No.", r.emp_id AS "Employee",
	r.datec AS "Date Created", r.bin_id AS "Bin",
	r.qty AS "Qty.", (r.qty * i.price) AS "Subtotal",
	ROUND((i.price * 0.13),2) AS "Taxes (13%)", 
	ROUND((i.price * 1.13 * r.qty),2) AS "Total"
		FROM inventory_release r
			INNER JOIN warehouse w
			ON w.bin_id = r.bin_id
				INNER JOIN inventory i
				ON w.inv_id = i.inv_id
		ORDER BY "Release No." ASC;;

-- view to see needs for resupply
CREATE VIEW resupply_report AS
	SELECT i.inv_id AS "SKU", i.descr AS "Item Name",
	(w.min_level - w.qty) AS "Need"
		FROM inventory i
			INNER JOIN warehouse w
			ON i.inv_id = w.inv_id
		WHERE w.qty < w.min_level
		ORDER BY "SKU" ASC;;
			
/**********************************
 *
 * Initial Insertions
 *
 **********************************/

CREATE PROCEDURE initial_population()
	BEGIN
	DECLARE i INT DEFAULT 0;	
	INSERT INTO shop VALUES
		('', 'carpenters', '2834', 'Patrick Vresky'),
		('', 'locksmiths', '3719', 'Quinn Voelkner'),
		('', 'millwrights', '8901', 'Robert Scott'),
		('', 'electricians', '2883', 'James Bell'),
		('', 'welders', '1092', 'Jimmy Hendrix'),
		('', 'groundskeepers', '9920', 'Felix Potvin'),
		('', 'custodians', '9001', 'Vlad Wojcik');
	INSERT INTO tradesperson VALUES
		('', 'Bill', 'Paxton', '123 Rainbow Road', '5199001234', '', 'some@email.com', '2017-04-16', '', 1),
		('', 'Viandra', 'Arbon', '12 Eugene Street', '2005911332', '', 'fake@brocku.ca', '2017-02-12', '', 2),
		('', 'Hannah', 'Timberwolf', '59 St. Clair Rd.', '7621606925', '', 'a@b.ca', '2017-05-11', '2017-05-12', 4),
		('', 'Tommy', 'Ross', '100 First Place', '6682876521', '4165029399', 'john@cogeco.com', '2017-01-01', '', 1),
		('', 'Jimmy', 'Smith', '52 Second Place', '7171745449', '9053450912', 'jimmy@johns.ca', '2017-01-01', '', 5);
	-- create some suppliers
	INSERT INTO supplier VALUES
		('', 'Home Hardware', 'John Smith', 'Toronto', '12 Fake Street', '4167839972', '4162389222'),
		('', 'Home Depot', 'Jane Doe', 'Hamilton', '121 Unicorn Road', '9056721192', '9058299912'),
		('', 'Canadian Tire', 'Patricia Horsehead', 'Toronto', '44 Yonge Street', '4169534122', '4164110921'),
		('', 'Bobs Buildit', 'Bob Frankson', 'St. Catharines', '1 King Street', '2899298042', '2897470999');
	-- initial inventory on hand
	CALL insertInventory('AA9284', 'exp', 'toilet paper', '5.99', '3', '1', '', NULL);
	CALL insertInventory('AA9282', 'exp', '60w light bulb', '5.99', '4', '3', '', NULL);
	CALL insertInventory('AA9250', 'exp', '30w light bulb', '4.99', '3', '3', 'AA9282', NULL);
	CALL insertInventory('AA1221', 'exp', 'masking tape', '2.99', '2', '5', '', NULL);
	CALL insertInventory('AA2833', 'exp', '9v battery', '0.99', '1', '2', '', NULL);
	CALL insertInventory('AA2931', 'exp', 'extension cable', '1.99', '1', '2', '', NULL);
	CALL insertInventory('AB3833', 'ser', 'shopvac', '199.99', '1', '2', '', '90210');
	CALL insertInventory('AB1991', 'ser', 'hvac filter', '14.99', '4', '9', '', '48291');
	-- create bins for each inventory item
	INSERT INTO warehouse VALUES
		('AB34', 'AA9284', 1, 6, 5),
		('DC10', 'AA9282', 1, 8, 5),
		('BC90', 'AA9250', 1, 7, 6),
		('GH31', 'AA1221', 1, 4, 2),
		('BB21', 'AA2833', 1, 3, 4),
		('AF84', 'AA2931', 1, 7, 6),
		('DE10', 'AB3833', 1, 2, 1),
		('GA66', 'AB1991', 1, 2, 2);
	-- add more inventory
	WHILE i < 5 DO
		CALL insertInventory('AA9284', 'exp', 'toilet paper', '5.99', '3', '1', '', NULL);
		SET i = i + 1;
	END WHILE;
	SET i = 0;
	WHILE i < 6 DO
		CALL insertInventory('AA9282', 'exp', '60w light bulb', '5.99', '3', '3', '', NULL);
		SET i = i + 1;
	END WHILE;
	SET i = 0;
	WHILE i < 2 DO
		CALL insertInventory('AA9250', 'exp', '30w light bulb', '4.99', '3', '3', 'AA9282', NULL);
		SET i = i + 1;
	END WHILE;
	SET i = 0;
	WHILE i < 4 DO
		CALL insertInventory('AA1221', 'exp', 'masking tape', '2.99', '2', '5', '', NULL);
		SET i = i + 1;
	END WHILE;
	SET i = 0;
	WHILE i < 7 DO
		CALL insertInventory('AA2833', 'exp', '9v battery', '0.99', '1', '2', '', NULL);
		SET i = i + 1;
	END WHILE;
	SET i = 0;
	WHILE i < 9 DO
		CALL insertInventory('AA2931', 'exp', 'extension cable', '1.99', '1', '2', '', NULL);
		SET i = i + 1;
	END WHILE;
	SET i = 0;
	WHILE i < 3 DO
		CALL insertInventory('AB3833', 'ser', 'shopvac', '199.99', '1', '2', '', '90210');
		SET i = i + 1;
	END WHILE;
	SET i = 0;
	WHILE i < 3 DO
		CALL insertInventory('AB1991', 'ser', 'hvac filter', '14.99', '4', '9', '', '48291');
		SET i = i + 1;
	END WHILE;
	-- add an inventory release
	INSERT INTO inventory_release VALUES
		('', 2, '2017-04-16', 'GA66', '3');
	END;;
	
CALL initial_population();;

DROP PROCEDURE initial_population;;

SET DELIMITER ;
