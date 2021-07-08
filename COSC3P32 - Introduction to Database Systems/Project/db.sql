-- erase old tables and contents
DROP TABLE IF EXISTS Drivers, Vehicles, Donor_Contacts, Tickets,
	Garages, Inspections, Plates, Farmer, Dealer, VehicleCategories,
	Models, LicenseClasses, Licenses CASCADE;

-- erase old functions
DROP FUNCTION IF EXISTS ensureOfAgeProcedure(), ensureCategoryExists(),
	ensurePlateNotExpired(), ensureOwnership(), checkVehicleCharacteristics(),
	checkDonorCount(), checkInspectionChangeOwner(), checkInspectionDate(),
	disableOverlapFarm(), disableOverlapDeal(), tabulateDemerit(integer),
	vehicleAssociation(integer, char), determineExpired(integer, char) CASCADE;

-- erase old triggers
DROP TRIGGER IF EXISTS ensureOfAge ON Drivers CASCADE;
DROP TRIGGER IF EXISTS ensureCategory ON Vehicles CASCADE;
DROP TRIGGER IF EXISTS ensurePlate ON Plates CASCADE;
DROP TRIGGER IF EXISTS ensureOwn ON Plates CASCADE;
DROP TRIGGER IF EXISTS checkVehicle ON Models CASCADE;
DROP TRIGGER IF EXISTS checkDonor ON Donor_Contacts CASCADE;
DROP TRIGGER IF EXISTS checkInspectionOwner ON Vehicles CASCADE;
DROP TRIGGER IF EXISTS checkInspection ON Inspections CASCADE;
DROP TRIGGER IF EXISTS overlapFarm ON Farmer CASCADE;
DROP TRIGGER IF EXISTS overlapDeal ON Dealer CASCADE;

-- table creation
CREATE TABLE Drivers (
	DID SERIAL,
	DName CHAR(40) NOT NULL,
	DOB DATE NOT NULL,
	PRIMARY KEY(DID)
);

CREATE TABLE LicenseClasses (
	CID SERIAL,
	Class CHAR(1) NOT NULL,
	Level SMALLINT NOT NULL,
	PRIMARY KEY(CID),
	CONSTRAINT max_level
		CHECK (Level > 0),
		CHECK (Level <= 9)
);

CREATE TABLE VehicleCategories (
	CID SERIAL,
	Class INTEGER REFERENCES LicenseClasses(CID)
		ON DELETE SET NULL
		NOT NULL,
	MaxWeight INTEGER NOT NULL,
	MaxLength INTEGER NOT NULL,
	NumAxles INTEGER NOT NULL,
	NumWheels INTEGER NOT NULL,
	Description TEXT,
	PRIMARY KEY(CID)
);

CREATE TABLE Models (
	MID SERIAL,
	CID INTEGER REFERENCES VehicleCategories(CID)
		ON DELETE SET NULL
		NOT NULL,
	Weight INTEGER NOT NULL,
	Length INTEGER NOT NULL,
	Axles INTEGER NOT NULL,
	Wheels INTEGER NOT NULL,
	Make CHAR(20) NOT NULL,
	Manuf CHAR(25) NOT NULL,
	Year INTEGER NOT NULL,
	PRIMARY KEY(MID),
	CONSTRAINT valid_year
		CHECK (Year >= 1900)
);

CREATE TABLE Vehicles (
	VIN SERIAL,
	DID INTEGER REFERENCES Drivers(DID)
		ON DELETE SET NULL
		NOT NULL,
	MID INTEGER REFERENCES Models(MID)
		ON DELETE SET NULL
		NOT NULL,
	PRIMARY KEY(VIN)
);

CREATE TABLE Donor_Contacts (
	DID INTEGER REFERENCES Drivers(DID)
		ON DELETE CASCADE
		NOT NULL,
	Name CHAR(40) NOT NULL,
	Phone BIGINT NOT NULL,
	PRIMARY KEY(DID, Phone, Name),
	CONSTRAINT valid_phone
		CHECK (Phone >= 1000000000),
		CHECK (Phone <= 9999999999)
);

CREATE TABLE Tickets (
	TID SERIAL,
	DID INTEGER REFERENCES Drivers(DID)
		ON DELETE SET NULL
		NOT NULL,
	Points INTEGER NOT NULL,
	Description TEXT,
	Date_Offense DATE NOT NULL,
	PRIMARY KEY(TID)
);

CREATE TABLE Garages (
	GID SERIAL,
	GPhone BIGINT NOT NULL,
	GName CHAR(20) NOT NULL,
	PRIMARY KEY(GID),
	CONSTRAINT valid_phone
		CHECK (GPhone >= 1000000000),
		CHECK (GPhone <= 9999999999)
);

CREATE TABLE Inspections (
	InsID SERIAL,
	DateInspection DATE NOT NULL,
	GID INTEGER REFERENCES Garages(GID)
		ON DELETE SET NULL
		NOT NULL,
	VIN INTEGER REFERENCES Vehicles(VIN)
		ON DELETE SET NULL
		NOT NULL,
	PRIMARY KEY(InsID)
);

CREATE TABLE Plates (
	PlateNum CHAR(10) NOT NULL,
	Expiry DATE NOT NULL,
	DID INTEGER REFERENCES Drivers(DID)
		ON DELETE SET NULL
		NOT NULL,
	VIN INTEGER REFERENCES Vehicles(VIN)
		ON DELETE SET NULL
		NOT NULL,
	PRIMARY KEY(PlateNum),
	UNIQUE(PlateNum),
	UNIQUE(VIN)
);

CREATE TABLE Farmer (
	FID INTEGER NOT NULL,
	PlateNum CHAR(10) REFERENCES Plates(PlateNum)
		ON DELETE CASCADE
		NOT NULL,
	PRIMARY KEY(PlateNum)
);

CREATE TABLE Dealer (
	BName CHAR(20) NOT NULL,
	BPhone BIGINT NOT NULL,
	PlateNum CHAR(10) REFERENCES Plates(PlateNum)
		ON DELETE CASCADE
		NOT NULL,
	PRIMARY KEY(PlateNum),
	CONSTRAINT valid_phone
		CHECK (BPhone >= 1000000000),
		CHECK (BPhone <= 9999999999)
);

CREATE TABLE Licenses (
	LID SERIAL,
	DID INTEGER REFERENCES Drivers(DID)
		ON DELETE CASCADE
		NOT NULL,
	CID INTEGER REFERENCES LicenseClasses(CID)
		ON DELETE SET NULL
		NOT NULL,
	Expiry DATE NOT NULL,
	PRIMARY KEY(LID)
);

INSERT INTO Drivers (DID, DName, DOB) VALUES
	(DEFAULT, 'Rob Scott', '1994-06-26'),
	(DEFAULT, 'Tim Ross', '1998-07-21'),
	(DEFAULT, 'Cale Fairchild', '1996-12-16'),
	(DEFAULT, 'Max Power', '1999-04-18'),
	(DEFAULT, 'Homer Simpson', '1992-05-01'),
	(DEFAULT, 'Alice Beaufort', '1992-02-02'),
	(DEFAULT, 'Aziz Nasir', '1991-02-25'),
	(DEFAULT, 'Erica Decker', '1994-01-30'),
	(DEFAULT, 'Matthew Laddy', '1995-05-30'),
	(DEFAULT, 'Lisa Sammy', '1998-11-28'),
	(DEFAULT, 'Sebastian Wood', '2001-02-07'),
	(DEFAULT, 'Steve Smith', '2000-5-23'),
	(DEFAULT, 'Royce Lando', '2001-02-07'),
	(DEFAULT, 'Lebron Smith', '1992-01-20');

INSERT INTO Donor_Contacts (DID, Name, Phone) VALUES
	(1, 'Emily Huron', 2895409203), (1, 'Tyler Muir', 9053594402),
	(2, 'Gino Giorno', 4168920012), (3, 'Adam Bolst', 2899309203),
	(3, 'Minny Grana', 4169380029), (4, 'Timmy Oulo', 9059930293),
	(5, 'Brad Hiller', 9053594455), (5, 'Bruce Bano', 4164329971),
	(6, 'Joel Decker', 2896469233), (7, 'Chris Toph', 2893320183),
	(7, 'Tim Oligive', 4166980222), (8, 'Adam Swane', 4169288302),
	(8, 'Brett Banne', 4165350679), (9, 'Don Draper', 9059930211),
	(9, 'Tomas Feket', 9054920321), (10, 'Tony Thip', 4165839299);

INSERT INTO Tickets (TID, DID, Points, Description, Date_Offense) VALUES
	(DEFAULT, 2, 5, 'Ran a red light', '2021-01-01'),
	(DEFAULT, 3, 3, 'Fail to stop', '2020-11-21'),
	(DEFAULT, 6, 10, 'Driving under influence', '2019-05-04'),
	(DEFAULT, 5, 5, 'Ran a red light', '2020-12-08'),
	(DEFAULT, 7, 2, 'Fail to signal', '2019-04-15'),
	(DEFAULT, 8, 1, 'Broken taillight', '2020-07-26'),
	(DEFAULT, 1, 1, 'Broken taillight', '2015-04-23'),
	(DEFAULT, 2, 2, 'Fail to signal', '2014-02-12');

INSERT INTO Garages (GID, GPhone, GName) VALUES
	(DEFAULT, 5149380029, 'Bobs Automotive'), (DEFAULT, 4163920039, 'Dekkers'),
	(DEFAULT, 2899298880, 'Canadian Tire'), (DEFAULT, 4169930293, 'Speedy'),
	(DEFAULT, 9053990329, 'Mikes Autobody');

INSERT INTO LicenseClasses (CID, Class, Level) VALUES
	(DEFAULT, 'G', 1), (DEFAULT, 'G', 2), (DEFAULT, 'M', 1), (DEFAULT, 'M', 2),
	(DEFAULT, 'C', 1), (DEFAULT, 'C', 2), (DEFAULT, 'C', 3), (DEFAULT, 'T', 1),
	(DEFAULT, 'S', 1), (DEFAULT, 'S', 2);

INSERT INTO Licenses (LID, DID, CID, Expiry) VALUES
	(DEFAULT, 1, 1, '2022-12-31'), (DEFAULT, 1, 2, '2022-11-30'),
	(DEFAULT, 2, 3, '2023-10-21'), (DEFAULT, 2, 4, '2023-09-11'),
	(DEFAULT, 3, 1, '2021-05-17'), (DEFAULT, 3, 2, '2021-05-12'),
	(DEFAULT, 3, 3, '2023-06-11'), (DEFAULT, 4, 5, '2024-01-03'),
	(DEFAULT, 5, 8, '2024-12-30'), (DEFAULT, 6, 1, '2022-02-05'),
	(DEFAULT, 6, 2, '2021-10-14'), (DEFAULT, 6, 9, '2023-03-01'),
	(DEFAULT, 6, 10, '2022-02-16'), (DEFAULT, 7, 1, '2024-05-22'),
	(DEFAULT, 7, 3, '2020-05-26'), (DEFAULT, 8, 5, '2020-09-27'),
	(DEFAULT, 8, 6, '2021-06-25'), (DEFAULT, 10, 3, '2023-11-28'),
	(DEFAULT, 10, 4, '2025-07-22'), (DEFAULT, 10, 9, '2024-12-16'),
	(DEFAULT, 11,2, '2025-07-22'), (DEFAULT, 12, 2, '2024-12-16'),
	(DEFAULT, 13,1, '2025-07-22'), (DEFAULT, 14, 1, '2024-12-16');

INSERT INTO VehicleCategories (CID, Class, MaxWeight, MaxLength,
	NumAxles, NumWheels, Description) VALUES
		(DEFAULT, 1, 1500, 800, 2, 4, 'Normal cars'),
		(DEFAULT, 2, 1800, 800, 2, 4, 'Normal cars'),
		(DEFAULT, 3, 700, 500, 2, 2, 'Motorcycles'),
		(DEFAULT, 4, 900, 500, 2, 2, 'Motorcycles'),
		(DEFAULT, 5, 2000, 1300, 4, 8, 'Buses'),
		(DEFAULT, 6, 1500, 800, 2, 4, 'Taxis'),
		(DEFAULT, 7, 3500, 1300, 4, 16, 'Double decker buses'),
		(DEFAULT, 8, 3000, 1000, 2, 4, 'Normal trucks'),
		(DEFAULT, 9, 1000, 800, 2, 4, 'Super cars'),
		(DEFAULT, 10, 1100, 900, 2, 4, 'Exotic cars'),
		(DEFAULT, 8, 3500, 1800, 4, 16, 'Very heavy trucks'),
		(DEFAULT, 8, 2500, 1800, 4, 8, 'Heavy trucks');

INSERT INTO Models (MID, CID, Weight, Length, Axles, Wheels, Make, 
	Manuf, Year) VALUES
		(DEFAULT, 1, 1495, 790, 2, 4, 'Civic', 'Honda', 1999),
		(DEFAULT, 1, 1445, 780, 2, 4, 'Civic', 'Honda', 2020),
		(DEFAULT, 1, 1485, 770, 2, 4, 'Civic', 'Honda', 1997),
		(DEFAULT, 1, 1454, 790, 2, 4, 'Camry', 'Toyota', 2005),
		(DEFAULT, 1, 1495, 780, 2, 4, 'Camry', 'Toyota', 2021),
		(DEFAULT, 2, 1755, 730, 2, 4, 'Volt', 'Chevy', 1997),
		(DEFAULT, 2, 1634, 755, 2, 4, 'Model M', 'Tesla', 2018),
		(DEFAULT, 2, 1700, 755, 2, 4, 'Model S', 'Tesla', 2015),
		(DEFAULT, 1, 1490, 760, 2, 4, 'CR-V', 'Honda', 2007),
		(DEFAULT, 1, 1490, 790, 2, 4, 'Accord', 'Honda', 1995),
		(DEFAULT, 1, 1410, 780, 2, 4, 'Accord', 'Honda', 1988),
		(DEFAULT, 1, 1425, 770, 2, 4, 'Scion', 'Toyota', 2004),
		(DEFAULT, 4, 800, 420, 2, 2, 'V4S', 'Ducati', 2002),
		(DEFAULT, 4, 750, 412, 2, 2, 'S1000RR', 'BMW', 2020),
		(DEFAULT, 3, 655, 500, 2, 2, 'Fireblade', 'Honda', 1995),
		(DEFAULT, 3, 685, 420, 2, 2, 'Ninja', 'Kawasaki', 1985),
		(DEFAULT, 9, 950, 780, 2, 4, 'Urus', 'Lamborghini', 2002),
		(DEFAULT, 9, 995, 770, 2, 4, 'Huracan', 'Lamborghini', 2000),
		(DEFAULT, 10, 1088, 890, 2, 4, '765', 'McClaren', 2020),
		(DEFAULT, 10, 1095, 780, 2, 4, '458', 'Ferrari', 2021),
		(DEFAULT, 2, 1485, 770, 2, 4, 'Elise', 'Lotus', 2005),
		(DEFAULT, 2, 1495, 790, 2, 4, 'Mini', 'Cooper', 2008);

INSERT INTO Vehicles (VIN, DID, MID) VALUES
	(DEFAULT, 1, 2), (DEFAULT, 1, 3), (DEFAULT, 1, 7), (DEFAULT, 1, 4),
	(DEFAULT, 2, 22), (DEFAULT, 2, 21), (DEFAULT, 2, 4), (DEFAULT, 3, 5),
	(DEFAULT, 3, 11), (DEFAULT, 4, 12), (DEFAULT, 5, 8), (DEFAULT, 5, 7),
	(DEFAULT, 5, 16), (DEFAULT, 5, 20), (DEFAULT, 5, 2), (DEFAULT, 6, 17),
	(DEFAULT, 6, 1), (DEFAULT, 7, 21), (DEFAULT, 7, 5), (DEFAULT, 7, 9),
	(DEFAULT, 7, 5), (DEFAULT, 7, 1), (DEFAULT, 8, 7), (DEFAULT, 9, 7),
	(DEFAULT, 9, 6), (DEFAULT, 9, 3), (DEFAULT, 9, 9), (DEFAULT, 9, 3),
	(DEFAULT, 10, 8), (DEFAULT, 10, 7), (DEFAULT, 10, 19), (DEFAULT, 10, 14),
	(DEFAULT, 11, 14), (DEFAULT, 12, 14), (DEFAULT, 13, 1), (DEFAULT, 14, 1);

INSERT INTO Plates (PlateNum, Expiry, DID, VIN) VALUES
	('ACEC 840', '2022-01-23', 1, 1), ('BGIR 911', '2023-05-14', 1, 2),
	('AGLD 342', '2021-09-29', 1, 3), ('LSPE 382', '2023-05-12', 1, 4),
	('BCLD 112', '2020-12-30', 2, 5), ('TLGF 593', '2022-02-11', 2, 6),
	('RLDS 532', '2021-09-21', 2, 7), ('COSC 332', '2023-09-09', 3, 8),
	('GLDA 523', '2023-05-03', 3, 9), ('ALPT 382', '2022-05-29', 4, 10),
	('RTPE 848', '2022-04-06', 5, 11), ('TTPB 191', '2020-09-21', 5, 12),
	('FLDS 918', '2022-02-15', 5, 13), ('TPBB 000', '2022-02-26', 5, 14),
	('FLCL 001', '2022-08-18', 5, 15), ('LAPQ 043', '2022-08-22', 6, 16),
	('AALC 002', '2023-11-19', 6, 17), ('TOEL 420', '2022-05-19', 7, 18),
	('TFPS 948', '2022-11-02', 7, 19), ('GLDK 091', '2021-12-17', 7, 20),
	('ALQE 984', '2022-05-01', 7, 21), ('KLPA 443', '2022-11-29', 7, 22),
	('ZZTP 505', '2021-10-05', 8, 23), ('RLPA 938', '2022-09-19', 9, 24),
	('PSLD 404', '2022-10-15', 9, 25), ('UUDP 595', '2020-12-11', 9, 26),
	('TIEO 102', '2020-11-25', 9, 27), ('ELDP 689', '2022-02-10', 9, 28),
	('TTTG 493', '2020-06-30', 10, 29), ('YLDP 992', '2023-03-01', 10, 30),
	('TCLD 949', '2022-03-14', 10, 31), ('JJHL 005', '2022-06-08', 10, 32),
	('HGOP 749', '2022-03-14', 11, 33), ('JGHL 635', '2022-06-08', 12, 34),
	('HEEE 612', '1998-01-11', 13, 35), ('DADD 535', '1899-08-04', 14, 36);

INSERT INTO Farmer (FID, PlateNum) VALUES
	(1, 'ACEC 840'), (1, 'TCLD 949'), (2, 'JJHL 005'), (1, 'GLDK 091'),
	(3, 'LAPQ 043'), (1, 'KLPA 443'), (3, 'TFPS 948');

INSERT INTO Dealer (BName, BPhone, PlateNum) VALUES
	('Toms Honda', '9053490019', 'YLDP 992'),
	('Brockville Auto', '9052299930', 'COSC 332'), 
	('Chris Autobody', '4150039201', 'ZZTP 505'), 
	('Mikes Exotics', '2899920909', 'ALPT 382'), 
	('Toms Honda', '9053490019', 'GLDA 523');

INSERT INTO Inspections (InsID, DateInspection, GID, VIN) VALUES
	(DEFAULT, '2021-05-21', 1, 1), (DEFAULT, '2021-06-25', 1, 17),
	(DEFAULT, '2021-01-19', 2, 2), (DEFAULT, '2021-05-28', 5, 18),
	(DEFAULT, '2021-02-12', 3, 3), (DEFAULT, '2021-03-22', 3, 19),
	(DEFAULT, '2021-02-04', 5, 4), (DEFAULT, '2021-02-21', 2, 20),
	(DEFAULT, '2021-01-29', 4, 5), (DEFAULT, '2021-02-12', 5, 21),
	(DEFAULT, '2021-04-11', 3, 6), (DEFAULT, '2021-03-16', 3, 22),
	(DEFAULT, '2021-05-14', 5, 7), (DEFAULT, '2021-01-29', 4, 23),
	(DEFAULT, '2021-06-17', 5, 8), (DEFAULT, '2021-02-21', 4, 24),
	(DEFAULT, '2021-03-22', 2, 9), (DEFAULT, '2021-03-05', 1, 25),
	(DEFAULT, '2021-04-09', 1, 10), (DEFAULT, '2021-05-02', 2, 26),
	(DEFAULT, '2021-05-30', 2, 11), (DEFAULT, '2021-04-04', 2, 27),
	(DEFAULT, '2021-06-20', 5, 12), (DEFAULT, '2021-04-08', 2, 28),
	(DEFAULT, '2021-06-30', 4, 13), (DEFAULT, '2021-01-21', 4, 29),
	(DEFAULT, '2021-01-21', 3, 14), (DEFAULT, '2021-02-19', 5, 30),
	(DEFAULT, '2021-01-26', 3, 15), (DEFAULT, '2021-04-22', 2, 31),
	(DEFAULT, '2021-02-19', 2, 16), (DEFAULT, '2021-03-31', 1, 32);

-- non-trigger functions
CREATE FUNCTION determineExpired(integer, char) RETURNS boolean AS $$
	BEGIN
		IF EXISTS (
			SELECT 1
			FROM LicenseClasses lc, Licenses l
			WHERE l.LID = $1
			AND lc.CID = l.CID
			AND lc.Class = $2
			AND l.Expiry > CURRENT_DATE
		) THEN
			RETURN FALSE;
		END IF;
		RETURN TRUE;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION vehicleAssociation(integer, char) RETURNS boolean AS $$
	BEGIN
		UPDATE Vehicles v
		SET v.VIN = $1
		WHERE v.PlateNum = $2;
		EXCEPTION WHEN OTHERS THEN
			RETURN FALSE;
		RETURN TRUE;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION tabulateDemerit(integer) RETURNS integer AS $$
	DECLARE didd INTEGER;
	DECLARE demerit INTEGER;
	BEGIN
		didd := (
			SELECT DID
			FROM Licenses l
			WHERE $1 = l.LID
		);
		demerit := (
			SELECT SUM(points)
			FROM Tickets t
			WHERE didd = t.DID
		);
		RETURN demerit;
	END;
$$ LANGUAGE plpgsql;

-- functions for triggers
CREATE FUNCTION ensureOfAgeProcedure() RETURNS trigger AS $$
	BEGIN
		IF NEW.DOB > (CURRENT_DATE - INTERVAL '16 years') THEN
			RAISE EXCEPTION 'Cannot add driver younger than 16yo.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION ensureCategoryExists() RETURNS trigger AS $$
	BEGIN
		IF NOT EXISTS (
			SELECT 1
			FROM VehicleCategories
			WHERE CID = NEW.CID
		) THEN
			RAISE EXCEPTION 'This vehicle category does not exist.';
		END IF;
		IF NOT EXISTS (
			SELECT 1
			FROM Licenses l, LicenseClasses lc, VehicleCategories vc
			WHERE l.CID = lc.CID
			AND vc.Class = lc.CID
		) THEN
			RAISE EXCEPTION 'This vehicle category has no licenses.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION ensureOwnership() RETURNS trigger AS $$
	BEGIN
		IF NOT EXISTS (
			SELECT 1
			FROM Vehicles v
			WHERE NEW.VIN = v.VIN
			AND v.DID = NEW.DID
		) THEN
			RAISE EXCEPTION 'This driver does not own this vehicle.';
		END IF;
		IF NOT EXISTS (
			SELECT 1
			FROM Licenses l, Vehicles v, Models m, VehicleCategories vc,
				LicenseClasses lc, Drivers d
			WHERE NEW.VIN = v.VIN
			AND v.MID = m.MID
			AND m.CID = vc.CID
			AND vc.Class = lc.CID
			AND lc.CID = l.CID
			AND l.DID = d.DID
		) THEN
			RAISE EXCEPTION 'This driver does not meet license requirements.';
		END IF;
		IF NOT EXISTS (
			SELECT 1
			FROM Licenses l, Vehicles v, Models m, VehicleCategories vc,
				LicenseClasses lc, Drivers d
			WHERE NEW.VIN = v.VIN
			AND v.MID = m.MID
			AND m.CID = vc.CID
			AND vc.Class = lc.CID
			AND lc.CID = l.CID
			AND l.DID = d.DID
			AND l.Expiry > CURRENT_DATE
		) THEN
			RAISE EXCEPTION 'This drivers license is expired.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION ensurePlateNotExpired() RETURNS trigger AS $$
	BEGIN
		IF NEW.Expiry < CURRENT_DATE THEN
			RAISE EXCEPTION 'Cannot associate plate to vehicle if expired.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION checkVehicleCharacteristics() RETURNS trigger AS $$
	BEGIN
		IF NOT EXISTS (
			SELECT 1
			FROM VehicleCategories vc
			WHERE NEW.Weight <= vc.MaxWeight
			AND NEW.Length <= vc.MaxLength
			AND NEW.Axles = vc.NumAxles
			AND NEW.Wheels = vc.NumWheels
			AND NEW.CID = vc.CID
		) THEN
			RAISE EXCEPTION 'This vehicle cannot be assigned to this category.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION checkDonorCount() RETURNS trigger AS $$
	DECLARE cnt SMALLINT;
	BEGIN
		cnt := (
			SELECT COUNT(*)
			FROM Donor_Contacts
			WHERE DID = NEW.DID
		);
		IF cnt >= 2 THEN
			RAISE EXCEPTION 'This driver cannot have more than two contacts.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION checkInspectionChangeOwner() RETURNS trigger AS $$
	BEGIN
		IF NOT EXISTS (
			SELECT 1
			FROM Inspections
			WHERE VIN = NEW.VIN
			AND DateInspection < CURRENT_DATE - (INTERVAL '6 months')
		) THEN
			RAISE EXCEPTION 'Cannot change ownership until inspected.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION checkInspectionDate() RETURNS trigger AS $$
	BEGIN
		IF NEW.DateInspection < CURRENT_DATE - (INTERVAL '6 months') THEN
			RAISE EXCEPTION 'Inspections must be less than six months old.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION disableOverlapFarm() RETURNS trigger AS $$
	BEGIN
		IF EXISTS (
			SELECT 1
			FROM Dealer
			WHERE PlateNum = NEW.PlateNum
		) THEN
			RAISE EXCEPTION 'Farmer plates cannot also be dealer plates.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION disableOverlapDeal() RETURNS trigger AS $$
	BEGIN
		IF EXISTS (
			SELECT 1
			FROM Farmer
			WHERE PlateNum = NEW.PlateNum
		) THEN
			RAISE EXCEPTION 'Dealer plates cannot also be farmer plates.';
		END IF;
		RETURN NEW;
	END;
$$ LANGUAGE plpgsql;

-- triggers
CREATE TRIGGER ensureOfAge BEFORE INSERT OR UPDATE ON Drivers
	FOR EACH ROW EXECUTE PROCEDURE ensureOfAgeProcedure();

CREATE TRIGGER ensureCategory BEFORE INSERT OR UPDATE ON Models
	FOR EACH ROW EXECUTE PROCEDURE ensureCategoryExists();

CREATE TRIGGER ensureOwn BEFORE INSERT OR UPDATE ON Plates
	FOR EACH ROW EXECUTE PROCEDURE ensureOwnership();

CREATE TRIGGER ensurePlate BEFORE INSERT OR UPDATE ON Plates
	FOR EACH ROW EXECUTE PROCEDURE ensurePlateNotExpired();

CREATE TRIGGER checkVehicle BEFORE INSERT OR UPDATE ON Models
	FOR EACH ROW EXECUTE PROCEDURE checkVehicleCharacteristics();

CREATE TRIGGER checkDonor BEFORE INSERT OR UPDATE ON Donor_Contacts
	FOR EACH ROW EXECUTE PROCEDURE checkDonorCount();

CREATE TRIGGER checkInspectionChange BEFORE UPDATE ON Vehicles
	FOR EACH ROW EXECUTE PROCEDURE checkInspectionChangeOwner();

CREATE TRIGGER checkInspection BEFORE INSERT OR UPDATE ON Inspections
	FOR EACH ROW EXECUTE PROCEDURE checkInspectionDate();

CREATE TRIGGER overlapFarm BEFORE INSERT OR UPDATE ON Farmer
	FOR EACH ROW EXECUTE PROCEDURE disableOverlapFarm();

CREATE TRIGGER overlapDeal BEFORE INSERT OR UPDATE ON Dealer
	FOR EACH ROW EXECUTE PROCEDURE disableOverlapDeal();

-- views
CREATE VIEW PVDrivers AS
	SELECT d.DName AS Driver_Name, v.VIN, m.Manuf AS Model,
		m.Make, p.PlateNum AS License_Plate
	FROM Vehicles v, Drivers d, Models m, Plates p
	WHERE v.MID = m.MID
	AND v.DID = d.DID
	AND p.VIN = v.VIN;

CREATE VIEW InvalidVehicleDriver AS
	SELECT d.DName, v.VIN, m.Manuf AS Model, m.Make,
		lc.Class AS License_Class, lc.Level AS License_Level
	FROM Vehicles v, Drivers d, Models m, LicenseClasses lc
	WHERE v.DID = d.DID
	AND v.MID = m.MID
	AND lc.CID = m.CID
	AND lc.CID NOT IN (
		SELECT l2.CID
		FROM Licenses l2
		WHERE l2.DID = d.DID
	);

CREATE VIEW ExpiredPlates AS
	SELECT pvd.*, p.Expiry
	FROM PVDrivers pvd, Plates p
	WHERE pvd.VIN = p.VIN
	AND p.Expiry < CURRENT_DATE;
