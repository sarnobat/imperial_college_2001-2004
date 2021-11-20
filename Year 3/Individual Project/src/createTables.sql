DROP SCHEMA ss CASCADE;
DROP TABLE xtractor_schemas;
CREATE SCHEMA ss;

CREATE TABLE ss.SYSTEM_element_locations (
	element_name	VARCHAR(50) NOT NULL UNIQUE,
	table_name	VARCHAR(50) NOT NULL,
	column_name	VARCHAR(50)

);CREATE SEQUENCE ss.id MINVALUE 1;
INSERT INTO ss.SYSTEM_element_locations(element_name,table_name) VALUES ('ps_db','ps_db');

--------------------	ps_db	--------------------
CREATE TABLE ss.ps_db(
);

CREATE TABLE ss.META_ps_db(
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL,
	ord	INT	NOT NULL
);
INSERT INTO ss.SYSTEM_element_locations(element_name,table_name) VALUES ('supplier','supplier');

--------------------	supplier	--------------------
CREATE TABLE ss.supplier(
	sno	VARCHAR(50),
	town	VARCHAR(50),
	PRIMARY KEY (sno)
);
INSERT INTO ss.SYSTEM_element_locations(element_name,table_name,column_name) VALUES ('supplies','supplies','supplies');
CREATE TABLE ss.supplies (
	sno	VARCHAR(50)	REFERENCES ss.supplier(sno),
	supplies	INT,
	PRIMARY KEY(sno,supplies)
);
CREATE TABLE ss.META_supplies (
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL	REFERENCES ss.META_supplies(id),
	ord	INT,
	sno	VARCHAR(50),
	supplies	INT,
	FOREIGN KEY (sno,supplies) REFERENCES ss.supplies (sno,supplies)
);

CREATE TABLE ss.META_supplier(
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL	REFERENCES ss.META_ps_db(id),
	ord	INT	NOT NULL,
	sno	VARCHAR(50)	UNIQUE REFERENCES ss.supplier(sno)
);
INSERT INTO ss.SYSTEM_element_locations(element_name,table_name) VALUES ('name','name');

--------------------	name	--------------------
CREATE TABLE ss.name(
);

CREATE TABLE ss.META_name(
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL	REFERENCES ss.META_supplier(id),
	ord	INT	NOT NULL
);


INSERT INTO ss.SYSTEM_element_locations(element_name,table_name) VALUES ('part','part');

--------------------	part	--------------------
CREATE TABLE ss.part(
	colour	VARCHAR(50),
	pno	INT,
	price	FLOAT,
	PRIMARY KEY (pno)
);

CREATE TABLE ss.META_part(
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL	REFERENCES ss.META_ps_db(id),
	ord	INT	NOT NULL,
	pno	INT	UNIQUE REFERENCES ss.part(pno)
);






INSERT INTO ss.SYSTEM_element_locations(element_name,table_name,column_name) VALUES ('sup','name_choice','sup');
--------------------	name_choice	--------------------

CREATE TABLE ss.name_choice (
	sup	VARCHAR(50)
);

--------------------	ps_db_choice	--------------------

CREATE TABLE ss.ps_db_choice (
	sno	VARCHAR(50),
	FOREIGN KEY (sno) REFERENCES ss.supplier(sno),
	pno	INT,
	FOREIGN KEY (pno) REFERENCES ss.part(pno),
	CHECK ((sno IS NULL) OR (pno IS NULL))
);

ALTER TABLE ss.name ADD COLUMN name_pkey INT;
ALTER TABLE ss.name ALTER COLUMN name_pkey SET NOT NULL;ALTER TABLE ss.name ADD PRIMARY KEY (name_pkey);
CREATE SEQUENCE ss.name_pkey_seq MINVALUE 1;ALTER TABLE ss.META_name ADD COLUMN name_pkey INT;
ALTER TABLE ss.META_name ALTER COLUMN name_pkey SET NOT NULL;ALTER TABLE ss.META_name ADD CONSTRAINT name_fkey FOREIGN KEY (name_pkey) REFERENCES ss.name(name_pkey) MATCH FULL;
ALTER TABLE ss.name_choice ADD COLUMN name_pkey INT;
ALTER TABLE ss.name_choice ADD CONSTRAINT name_fkey FOREIGN KEY (name_pkey) REFERENCES ss.name(name_pkey) MATCH FULL;


ALTER TABLE ss.ps_db ADD COLUMN ps_db_pkey INT;
ALTER TABLE ss.ps_db ALTER COLUMN ps_db_pkey SET NOT NULL;ALTER TABLE ss.ps_db ADD PRIMARY KEY (ps_db_pkey);
CREATE SEQUENCE ss.ps_db_pkey_seq MINVALUE 1;ALTER TABLE ss.META_ps_db ADD COLUMN ps_db_pkey INT;
ALTER TABLE ss.META_ps_db ALTER COLUMN ps_db_pkey SET NOT NULL;ALTER TABLE ss.META_ps_db ADD CONSTRAINT ps_db_fkey FOREIGN KEY (ps_db_pkey) REFERENCES ss.ps_db(ps_db_pkey) MATCH FULL;
ALTER TABLE ss.ps_db_choice ADD COLUMN ps_db_pkey INT;
ALTER TABLE ss.ps_db_choice ADD CONSTRAINT ps_db_fkey FOREIGN KEY (ps_db_pkey) REFERENCES ss.ps_db(ps_db_pkey) MATCH FULL;


ALTER TABLE ss.name ADD COLUMN sno VARCHAR(50);
ALTER TABLE ss.name ADD CONSTRAINT supplier_fkey FOREIGN KEY (sno) REFERENCES ss.supplier(sno) MATCH FULL;


ALTER TABLE ss.supplies ADD CONSTRAINT supplies_fkey FOREIGN KEY (supplies) REFERENCES ss.part(pno);


--------------------
------ VIEWS -------
--------------------
CREATE VIEW ss.complete_name AS (
	SELECT ss.meta_name.pid,ss.meta_name.id,ss.name.sno,ss.meta_name.name_pkey,ss.meta_name.ord
	FROM ss.meta_name
	INNER JOIN ss.name
	ON ss.name.name_pkey=ss.meta_name.name_pkey
);

CREATE VIEW ss.complete_part AS (
	SELECT ss.part.colour,ss.meta_part.pid,ss.meta_part.id,ss.part.price,ss.meta_part.pno,ss.meta_part.ord
	FROM ss.meta_part
	INNER JOIN ss.part
	ON ss.part.pno=ss.meta_part.pno
);

CREATE VIEW ss.complete_ps_db AS (
	SELECT ss.meta_ps_db.pid,ss.meta_ps_db.id,ss.meta_ps_db.ps_db_pkey,ss.meta_ps_db.ord
	FROM ss.meta_ps_db
	INNER JOIN ss.ps_db
	ON ss.ps_db.ps_db_pkey=ss.meta_ps_db.ps_db_pkey
);

CREATE VIEW ss.complete_supplier AS (
	SELECT ss.meta_supplier.pid,ss.meta_supplier.id,ss.meta_supplier.sno,ss.supplier.town,ss.meta_supplier.ord
	FROM ss.meta_supplier
	INNER JOIN ss.supplier
	ON ss.supplier.sno=ss.meta_supplier.sno
);

CREATE VIEW ss.complete_supplies AS (
	SELECT ss.meta_supplies.supplies,ss.meta_supplies.pid,ss.meta_supplies.id,ss.meta_supplies.sno,ss.meta_supplies.ord
	FROM ss.meta_supplies
	INNER JOIN ss.supplies
	ON ss.supplies.sno=ss.meta_supplies.sno AND ss.supplies.supplies=ss.meta_supplies.supplies
);

;