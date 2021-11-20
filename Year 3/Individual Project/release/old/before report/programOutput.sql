DROP SCHEMA ss CASCADE;
DELETE FROM XTractor_schemas WHERE name='ss';
INSERT INTO XTractor_schemas(name) VALUES ('ss');
CREATE SCHEMA ss;

CREATE TABLE ss.CATALOG_elements (

	element	VARCHAR(50),
	parent_element	VARCHAR(50),
	alias	VARCHAR(50),
	type	VARCHAR(50),
	PRIMARY KEY(element)
);CREATE TABLE ss.CATALOG_attributes (

	attribute	VARCHAR(50),
	element	VARCHAR(50),
	alias	VARCHAR(50),
	PRIMARY KEY(attribute)
);CREATE TABLE ss.CATALOG_generalizations (
	general_table	VARCHAR(50),
	specialization_table	VARCHAR(50));
CREATE VIEW ss.CATALOG_complex_elements AS (
	SELECT element
	FROM ss.CATALOG_elements
	WHERE type='complex' OR type='mixed'
);
CREATE VIEW ss.CATALOG_simple_elements AS (
	SELECT element
	FROM ss.CATALOG_elements
	WHERE type='simple'
);
CREATE VIEW ss.CATALOG_mixed_elements AS (
	SELECT element
	FROM ss.CATALOG_elements
	WHERE type='mixed'
);
INSERT INTO ss.CATALOG_elements(element,parent_element,alias,type) VALUES ('ps_db',NULL,NULL,'complex');CREATE SEQUENCE ss.id MINVALUE 1;

--------------------	ps_db	--------------------
CREATE TABLE ss.ps_db(
);
CREATE TABLE ss.META_ps_db(
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL,
	ord	INT	NOT NULL
);

--------------------	manufacturer	--------------------
CREATE TABLE ss.manufacturer(
	sno	VARCHAR(50),
	town	VARCHAR(50),
	PRIMARY KEY (sno)
);INSERT INTO ss.CATALOG_attributes(attribute,element,alias) VALUES ('sno','manufacturer',NULL);INSERT INTO ss.CATALOG_attributes(attribute,element,alias) VALUES ('town','manufacturer',NULL);
CREATE TABLE ss.META_manufacturer(
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL	REFERENCES ss.META_ps_db(id),
	ord	INT	NOT NULL,
	sno	VARCHAR(50)	REFERENCES ss.manufacturer(sno)
);
CREATE TABLE ss.supplies (
	sno	VARCHAR(50)	REFERENCES ss.manufacturer(sno),
	supplies	INT,
	PRIMARY KEY(sno,supplies)
);
CREATE TABLE ss.META_supplies (
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL	REFERENCES ss.META_manufacturer(id),
	ord	INT,
	sno	VARCHAR(50),
	supplies	INT,
	FOREIGN KEY (sno,supplies) REFERENCES ss.supplies (sno,supplies)
);INSERT INTO ss.CATALOG_elements(element,parent_element,alias,type) VALUES ('supplies','manufacturer',NULL,'simple');

--------------------	name	--------------------
CREATE TABLE ss.name(
	name	VARCHAR(50)
);
CREATE TABLE ss.META_name(
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL	REFERENCES ss.META_manufacturer(id),
	ord	INT	NOT NULL
);



--------------------	part	--------------------
CREATE TABLE ss.part(
	colour	VARCHAR(50),
	pno	INT,
	price	FLOAT,
	PRIMARY KEY (pno)
);INSERT INTO ss.CATALOG_attributes(attribute,element,alias) VALUES ('colour','part',NULL);INSERT INTO ss.CATALOG_attributes(attribute,element,alias) VALUES ('pno','part',NULL);INSERT INTO ss.CATALOG_attributes(attribute,element,alias) VALUES ('price','part',NULL);
CREATE TABLE ss.META_part(
	id	SERIAL	PRIMARY KEY,
	pid	INT	NOT NULL	REFERENCES ss.META_ps_db(id),
	ord	INT	NOT NULL,
	pno	INT	REFERENCES ss.part(pno)
);






--------------------	name_choice	--------------------

CREATE TABLE ss.name_choice (
	sup	VARCHAR(50)
);INSERT INTO ss.CATALOG_generalizations(general_table,specialization_table) VALUES ('name_choice','sup');


--------------------	ps_db_choice	--------------------

CREATE TABLE ss.ps_db_choice (
	sno	VARCHAR(50),
	FOREIGN KEY (sno) REFERENCES ss.manufacturer(sno),
	pno	INT,
	FOREIGN KEY (pno) REFERENCES ss.part(pno),
	CHECK ((sno IS NULL) OR (pno IS NULL))
);INSERT INTO ss.CATALOG_generalizations(general_table,specialization_table) VALUES ('ps_db_choice','manufacturer');
INSERT INTO ss.CATALOG_generalizations(general_table,specialization_table) VALUES ('ps_db_choice','part');


INSERT INTO ss.CATALOG_elements(element,parent_element,alias,type) VALUES ('sup','name',NULL,'simple');ALTER TABLE ss.name ADD COLUMN name_pkey INT;
ALTER TABLE ss.name ALTER COLUMN name_pkey SET NOT NULL;
ALTER TABLE ss.name ADD PRIMARY KEY (name_pkey);
CREATE SEQUENCE ss.name_pkey_seq MINVALUE 1;
ALTER TABLE ss.META_name ADD COLUMN name_pkey INT;
ALTER TABLE ss.META_name ALTER COLUMN name_pkey SET NOT NULL;
ALTER TABLE ss.META_name ADD CONSTRAINT name_fkey FOREIGN KEY (name_pkey) REFERENCES ss.name(name_pkey) MATCH FULL;
ALTER TABLE ss.name_choice ADD COLUMN name_pkey INT;
ALTER TABLE ss.name_choice ADD CONSTRAINT name_fkey FOREIGN KEY (name_pkey) REFERENCES ss.name(name_pkey) MATCH FULL;


INSERT INTO ss.CATALOG_elements(element,parent_element,alias,type) VALUES ('supplier','ps_db',NULL,'complex');INSERT INTO ss.CATALOG_elements(element,parent_element,alias,type) VALUES ('part','ps_db',NULL,'complex');ALTER TABLE ss.ps_db ADD COLUMN ps_db_pkey INT;
ALTER TABLE ss.ps_db ALTER COLUMN ps_db_pkey SET NOT NULL;
ALTER TABLE ss.ps_db ADD PRIMARY KEY (ps_db_pkey);
CREATE SEQUENCE ss.ps_db_pkey_seq MINVALUE 1;
ALTER TABLE ss.META_ps_db ADD COLUMN ps_db_pkey INT;
ALTER TABLE ss.META_ps_db ALTER COLUMN ps_db_pkey SET NOT NULL;
ALTER TABLE ss.META_ps_db ADD CONSTRAINT ps_db_fkey FOREIGN KEY (ps_db_pkey) REFERENCES ss.ps_db(ps_db_pkey) MATCH FULL;
ALTER TABLE ss.ps_db_choice ADD COLUMN ps_db_pkey INT;
ALTER TABLE ss.ps_db_choice ADD CONSTRAINT ps_db_fkey FOREIGN KEY (ps_db_pkey) REFERENCES ss.ps_db(ps_db_pkey) MATCH FULL;


INSERT INTO ss.CATALOG_elements(element,parent_element,alias,type) VALUES ('name','manufacturer',NULL,'mixed');ALTER TABLE ss.name ADD COLUMN sno VARCHAR(50);
ALTER TABLE ss.name ADD CONSTRAINT manufacturer_fkey FOREIGN KEY (sno) REFERENCES ss.manufacturer(sno) MATCH FULL;


ALTER TABLE ss.supplies ADD CONSTRAINT supplies_fkey FOREIGN KEY (supplies) REFERENCES ss.part(pno);


UPDATE ss.CATALOG_elements SET alias='manufacturer' WHERE element='supplier';
--------------------
------ VIEWS -------
--------------------
CREATE VIEW ss.COMPLETE_manufacturer AS (
	SELECT ss.meta_manufacturer.sno,ss.meta_manufacturer.ord,ss.manufacturer.town,ss.meta_manufacturer.pid,ss.meta_manufacturer.id
	FROM ss.meta_manufacturer
	INNER JOIN ss.manufacturer
	ON ss.manufacturer.sno=ss.meta_manufacturer.sno
);

CREATE VIEW ss.COMPLETE_name AS (
	SELECT ss.name.sno,ss.meta_name.name_pkey,ss.meta_name.ord,ss.name.name,ss.meta_name.pid,ss.meta_name.id
	FROM ss.meta_name
	INNER JOIN ss.name
	ON ss.name.name_pkey=ss.meta_name.name_pkey
);

CREATE VIEW ss.COMPLETE_part AS (
	SELECT ss.meta_part.ord,ss.part.price,ss.part.colour,ss.meta_part.pno,ss.meta_part.pid,ss.meta_part.id
	FROM ss.meta_part
	INNER JOIN ss.part
	ON ss.part.pno=ss.meta_part.pno
);

CREATE VIEW ss.COMPLETE_ps_db AS (
	SELECT ss.meta_ps_db.ord,ss.meta_ps_db.ps_db_pkey,ss.meta_ps_db.pid,ss.meta_ps_db.id
	FROM ss.meta_ps_db
	INNER JOIN ss.ps_db
	ON ss.ps_db.ps_db_pkey=ss.meta_ps_db.ps_db_pkey
);

CREATE VIEW ss.COMPLETE_supplies AS (
	SELECT ss.meta_supplies.sno,ss.meta_supplies.ord,ss.meta_supplies.supplies,ss.meta_supplies.pid,ss.meta_supplies.id
	FROM ss.meta_supplies
	INNER JOIN ss.supplies
	ON ss.supplies.sno=ss.meta_supplies.sno AND ss.supplies.supplies=ss.meta_supplies.supplies
);

CREATE VIEW ss.CATALOG_ids_to_tables AS (
	(SELECT id,text 'manufacturer' AS table_name FROM ss.meta_manufacturer) UNION
	(SELECT id,text 'name' AS table_name FROM ss.meta_name) UNION
	(SELECT id,text 'part' AS table_name FROM ss.meta_part) UNION
	(SELECT id,text 'ps_db' AS table_name FROM ss.meta_ps_db) UNION
	(SELECT id,text 'supplies' AS table_name FROM ss.meta_supplies) 
);