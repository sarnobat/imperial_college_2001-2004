DROP TABLE Players;
CREATE TABLE Players (
	PlayerNum integer NOT NULL,
	PlayerName text,
	Playing boolean,
	Password text,
	Email text,
	balance integer
);
DROP TABLE Ships;
CREATE TABLE Ships (
	ShipNum integer NOT NULL,
	name text,
	food integer NOT NULL,
	water integer NOT NULL,
	fuel integer NOT NULL,
	gold integer NOT NULL,
	titanium integer NOT NULL,
	industrial integer NOT NULL,
	mining integer NOT NULL,
	farming integer NOT NULL
);
DROP TABLE ShipState;
CREATE TABLE ShipState (
	ShipNum integer NOT NULL,
	Game integer,
        Owner integer,
	x real,
	y real,
	z real,
	fuelcontent integer,
	mileage integer
);
DROP TABLE shipstorage;
CREATE TABLE shipstorage (
	ShipNum integer NOT NULL,
        Game integer,
	VolumeUsed integer,
	freespace integer
);
DROP TABLE goods;
CREATE TABLE goods (
	PlayerNum integer,
	ResourceName text,
	price integer,
	volume integer,
	forsale integer,
	notforsale real
);
DROP TABLE messages;
CREATE TABLE messages (
	MsgNum integer NOT NULL,
	Game integer,
	Sender integer,
	date timestamp with time zone NOT NULL,
	message text,
	subject text,
	sent boolean
);
DROP TABLE mailbox;
CREATE TABLE mailbox (
	MsgNum integer,
	dest integer
);
DROP TABLE universe;
CREATE TABLE universe (
	Star text NOT NULL,
	StarNum integer,
	x double precision,
	y double precision,
	z double precision
);
DROP TABLE StarState;
CREATE TABLE StarState (
	StarNum integer,
	Game integer,
	owner integer,
	civilisation text
);
DROP TABLE Game;
CREATE TABLE Game (
	Game integer,
	PlayerNum integer,
	Turn integer,
	Playing boolean
);
DROP TABLE planets;
CREATE TABLE planets (
	starnum integer NOT NULL,
	food integer NOT NULL,
	water integer NOT NULL,
	fuel integer NOT NULL,
	gold integer NOT NULL,
	titanium integer NOT NULL,
	industrial real NOT NULL,
	mining real NOT NULL,
	farming real NOT NULL 
);
INSERT INTO StarState (StarNum,civilisation)  VALUES(1			,'industrial');
INSERT INTO StarState (StarNum,civilisation)  VALUES(2			,'mine');	
INSERT INTO StarState (StarNum,civilisation)  VALUES(3			,'mine');
INSERT INTO StarState (StarNum,civilisation)  VALUES(4			,'industrial');
INSERT INTO StarState (StarNum,civilisation)  VALUES(5			,'agriculture');
INSERT INTO StarState (StarNum,civilisation)  VALUES(6			,'agriculture');
INSERT INTO StarState (StarNum,civilisation)  VALUES(7			,'industrial');
INSERT INTO StarState (StarNum,civilisation)  VALUES(8			,'industrial');
INSERT INTO StarState (StarNum,civilisation)  VALUES(9			,'industrial');
INSERT INTO StarState (StarNum,civilisation)  VALUES(10			,'industrial');
INSERT INTO StarState (StarNum,civilisation)  VALUES(11			,'industrial');
INSERT INTO StarState (StarNum,civilisation)  VALUES(12			,'industrial');
INSERT INTO StarState (StarNum,civilisation)  VALUES(13			,'agriculture');
INSERT INTO StarState (StarNum,civilisation)  VALUES(14			,'agriculture');
INSERT INTO StarState (StarNum,civilisation)  VALUES(15			,'agriculture'); 
INSERT INTO StarState (StarNum,civilisation)  VALUES(16			,'agriculture');
INSERT INTO StarState (StarNum,civilisation)  VALUES(17			,'industrial');
INSERT INTO StarState (StarNum,civilisation)  VALUES(18			,'agriculture');
INSERT INTO StarState (StarNum,civilisation)  VALUES(19			,'agriculture');
INSERT INTO StarState (StarNum,civilisation)  VALUES(20			,'agriculture');
INSERT INTO StarState (StarNum,civilisation)  VALUES(21			,'agriculture');
INSERT INTO StarState (StarNum,civilisation)  VALUES(22			,'mine');	
INSERT INTO StarState (StarNum,civilisation)  VALUES(23			,'mine');
INSERT INTO StarState (StarNum,civilisation)  VALUES(24			,'mine');
INSERT INTO StarState (StarNum,civilisation)  VALUES(25			,'mine');
INSERT INTO StarState (StarNum,civilisation)  VALUES(26			,'mine');
INSERT INTO StarState (StarNum,civilisation)  VALUES(27			,'mine');
INSERT INTO StarState (StarNum,civilisation)  VALUES(28			,'mine');
INSERT INTO StarState (StarNum,civilisation)  VALUES(29			,'industrial');	
INSERT INTO StarState (StarNum,civilisation)  VALUES(30			,'mine');


INSERT INTO Universe VALUES('Sun',	1,	1.5e-05,	0,	0);
INSERT INTO Universe VALUES('Procione A',	2,	-0.435614,	4.72827,	-0.909959);
INSERT INTO Universe VALUES('Proxima Cen',	3,	-2.99638,	-2.41866,	0.418553);
INSERT INTO Universe VALUES('Alpha Cen A',	4,	-2.99313,	-0.0254794,	0.724108);
INSERT INTO Universe VALUES('Alpha Cen B',	5,	-2.99323,	-0.00753013,	0.724108);
INSERT INTO Universe VALUES('Barnards Star',	6,	1.39529,	-1.10182,	-0.954628);
INSERT INTO Universe VALUES('Wolf 359',	7,	3.71408,	4.55907,	0.656987);
INSERT INTO Universe VALUES('Lalande 21186',	8,	1.77541,	-1.40199,	-0.853405);
INSERT INTO Universe VALUES('UV Ceti A',	9,	1.78283,	1.68784,	0.957889);
INSERT INTO Universe VALUES('epsilon Eridani',	10,	10.3634,	-1.8011,	-0.144273);
INSERT INTO Universe VALUES('Ross 248',	11,	-9.97803,	-2.54813,	0.11743);
INSERT INTO Universe VALUES('Kapteyns Star',	12,	-5.17882,	3.87855,	-0.856113);
INSERT INTO Universe VALUES('Sirus A',	13,	-4.90141,	-4.29051,	0.660915);
INSERT INTO Universe VALUES('Ross 128',14,	4.82972,	-8.27294,	0.461779);
INSERT INTO Universe VALUES('61 Cygni B',	15,	-6.84395,	4.52167,	0.682289);
INSERT INTO Universe VALUES('61 Cygni A',	16,	-6.82123,	4.55586,	0.682289);
INSERT INTO Universe VALUES('Groombridge 34 A',	17,	10.0499,	4.98478,	0.0177019);
INSERT INTO Universe VALUES('BD +59h 1915 B',	18,	6.65213,	8.35274,	0.314815);
INSERT INTO Universe VALUES('BD +5h 1668',	19,	1.10191,	-4.84876,	-0.91406);
INSERT INTO Universe VALUES('Lacaille9352',	20,	-5.1689,	-4.13569,	0.814855);
INSERT INTO Universe VALUES('Procione B',	21,	-0.3883,	4.73239,	-0.909959);
INSERT INTO Universe VALUES('BD +59h 1915 A', 22,	6.56819,	8.41891,	0.314815);
INSERT INTO Universe VALUES('YZ Ceti',	23,	-4.35995,	6.28886,	0.772035);
INSERT INTO Universe VALUES('G 51-15',	24,	-0.0441328,	2.8475,	0.970491);
INSERT INTO Universe VALUES('Epsilon Indi',	25,-6.47096,	-9.16011,	0.0785854);
INSERT INTO Universe VALUES('UV Ceti B',	26,1.97978,	-1.45181,	0.957889);
INSERT INTO Universe VALUES('Sirus B',	27,4.76193,	4.4448,	0.660915);
INSERT INTO Universe VALUES('Ross 154',	28,-0.602726,	0.324076,	0.997413);
INSERT INTO Universe VALUES('Gamma Ceti',	29,	-7.26231,	-8.56047,	-0.147424);
INSERT INTO Universe VALUES('Luyten789-6',	30,	-9.39881,	-1.98358,	-0.503775);