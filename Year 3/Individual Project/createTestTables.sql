DROP SCHEMA ss CASCADE;
DROP TABLE xtractor_schemas;
CREATE SCHEMA ss;

--For assigning globally unique id
CREATE SEQUENCE ss.id MINVALUE 1;

/** This meta table will not be so constrained as the other meta tables **/

CREATE TABLE ss.META_ps_db (
	id		SERIAL	PRIMARY KEY,
	pid		INT,
	ord		INT,
	time_added	TIMESTAMP,
	filename	VARCHAR(50),
	comment	VARCHAR(200)
);

CREATE VIEW ss.ps_db_COMPLETE AS
	SELECT *
	FROM ss.META_ps_db;

----------------  PART  ---------------------------------

CREATE TABLE ss.part (
	pno	INT	PRIMARY KEY,
	colour	VARCHAR(50),
	price	DECIMAL
);

CREATE TABLE ss.META_part (
	id	SERIAL	PRIMARY KEY,
	ord	INT,
	pno	INT		UNIQUE REFERENCES ss.part(pno)
);
--Notice we must insert 'unique' in the foreign key so that we don't duplicate the meta entry. 
--BUT we need to update the values somehow

CREATE VIEW ss.part_COMPLETE AS

	SELECT ss.META_part.*, ss.part.colour,ss.part.price
	FROM ss.part,ss.META_part
	WHERE ss.part.pno = ss.META_part.pno
;


----------------  SUPPLIER  ---------------------------

CREATE TABLE ss.supplier (
	sno	VARCHAR	PRIMARY KEY,
	town VARCHAR(50)
);

CREATE TABLE ss.META_supplier (
	id	SERIAL	PRIMARY KEY,
	pid	INT		REFERENCES ss.META_ps_db,
	ord	INT,
	sno	VARCHAR(50)	UNIQUE REFERENCES ss.supplier(sno)
);

CREATE VIEW ss.supplier_COMPLETE AS

	SELECT ss.META_supplier.*, ss.supplier.town
	FROM ss.supplier,ss.META_supplier
	WHERE ss.supplier.sno = ss.META_supplier.sno
;

-----------------  NAME  ----------------------------------

CREATE TABLE ss.META_name(
	id	SERIAL	PRIMARY KEY,
	pid	INT		REFERENCES ss.META_supplier(id),
	ord	INT
);

CREATE VIEW ss.name_COMPLETE AS

	SELECT ss.META_name.*
	FROM ss.META_name
;

-----------------  SUP ----------------------------------


CREATE TABLE ss.META_sup (
	id	SERIAL	PRIMARY KEY,
	pid	INT	REFERENCES ss.META_name(id),
	ord	INT
);

CREATE VIEW ss.sup_COMPLETE AS

	SELECT ss.META_sup.*
	FROM ss.META_sup
;

-----------------  SUPPLIES ----------------------------------

CREATE TABLE ss.META_supplies (
	id	SERIAL	PRIMARY KEY,
	pid	INT		REFERENCES ss.META_supplies,
	ord	INT
);

CREATE VIEW ss.supplies_COMPLETE AS

	SELECT ss.META_supplies.*
	FROM ss.META_supplies
;

---------------------------------------------------------
------------------  META TABLES  ------------------------
---------------------------------------------------------

CREATE TABLE ss.META2_tables (
	table_name	VARCHAR(50)	PRIMARY KEY,
	type		VARCHAR(50) -- This should be either data, data-meta or system-meta (i.e. meta2)
);

INSERT INTO ss.META2_tables (table_name,type) VALUES ('META_name','meta');
INSERT INTO ss.META2_tables (table_name,type) VALUES ('META_part','meta');
INSERT INTO ss.META2_tables (table_name,type) VALUES ('META_ps_db','meta');
INSERT INTO ss.META2_tables (table_name,type) VALUES ('META_sup','meta');
INSERT INTO ss.META2_tables (table_name,type) VALUES ('META_supplier','meta');
INSERT INTO ss.META2_tables (table_name,type) VALUES ('META_supplies','meta');
--This is incomplete

CREATE VIEW ss.META2_idsToTables AS (
	(SELECT id,text 'ps_db' AS table_name FROM ss.META_ps_db) UNION 
	(SELECT id,text 'part' AS table_name FROM ss.META_part)	UNION
	(SELECT id,text 'supplier' AS table_name FROM ss.META_supplier)	UNION
	(SELECT id,text 'name' AS table_name FROM ss.META_name)	UNION
	(SELECT id,text 'sup' AS table_name FROM ss.META_sup)	UNION
	(SELECT id,text 'supplies' AS table_name FROM ss.META_supplies)
);

/*CREATE VIEW ss.META2_idsToTables AS (
	(SELECT id,text '1' AS num FROM ss.META_ps_db)	UNION
	(SELECT id,text '1' AS num FROM ss.META_part)

);*/

---------------------------------------------------------

CREATE TABLE ss.META2_key_fields (
	table_name	VARCHAR(50),
	key_attribute	VARCHAR(50),
	type		VARCHAR(50)
);

INSERT INTO ss.META2_key_fields (table_name,key_attribute,type) VALUES ('part','pno','int');
INSERT INTO ss.META2_key_fields (table_name,key_attribute,type) VALUES ('supplier','sno','int');

---------------------------------------------------------

CREATE TABLE ss.META2_contains (
	parent_element	VARCHAR(50),
	child_element	VARCHAR(50),
	PRIMARY KEY (parent_element,child_element)
	
);

INSERT INTO ss.META2_contains (parent_element,child_element) VALUES ('ps_db','part');
INSERT INTO ss.META2_contains (parent_element,child_element) VALUES ('ps_db','supplier');
INSERT INTO ss.META2_contains (parent_element,child_element) VALUES ('supplier','name');
INSERT INTO ss.META2_contains (parent_element,child_element) VALUES ('supplier','supplies');
INSERT INTO ss.META2_contains (parent_element,child_element) VALUES ('name','sup');

---------------------------------------------------------

CREATE TABLE ss.META2_root_element (
	root	VARCHAR(50)	PRIMARY KEY
);

INSERT INTO ss.META2_root_element (root) VALUES ('ps_db');

---------------------------------------------------------

CREATE TABLE XTractor_schemas (
	schema_name VARCHAR(50) PRIMARY KEY,
	comment	VARCHAR(150)
);

--Note that this is not part of the ss schema, because it is at the level above all of them (i.e. the root)
INSERT INTO XTractor_schemas (schema_name,comment) VALUES ('ss','The suppliers and parts database used in PJM\'s paper');


/****************
---------------------------------------------------------

CREATE TABLE ss.<<element name>> (
	????	INT	PRIMARY KEY,
);

CREATE TABLE ss.META_<<element name>> (
	id	SERIAL	PRIMARY KEY,
	pid	INT	REFERENCES ss.META_<<parent element>>,
	ord	INT,
);

CREATE VIEW ss.<<element name>>_COMPLETE AS

	SELECT ss.META_<<element name>>.*, ss.<<element name>>,ss.??????
	FROM ss.<<element name>>,ss.META_<<element name>>
	WHERE ss.<<element name>>.?????? = ss.META_<<element name>>.??????;

******************/

