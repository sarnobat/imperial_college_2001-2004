(1)
SELECT title,director FROM films order by director;

(2)
SELECT title,length,made FROM films where origin != 'US';

(3)
SELECT title,director,made,length FROM films WHERE length >'2:00' and made > 
'1997-01-01';

(4)
SELECT name FROM casting WHERE title = 'Psycho' and director = 'Alfred 
Hitchcock';

(5)
SELECT name,part,title FROM casting WHERE title like 'D%';

(6)
SELECT name,part,title FROM casting WHERE name IN (SELECT name FROM actors 
WHERE born < '1900-01-01');

(7)
SELECT name,part,title FROM casting WHERE title IN (SELECT title FROM casting 
WHERE name = 'Bruce Willis') AND name != 'Bruce Willis';

(8)
SELECT COUNT(DISTINCT name) FROM casting where title IN (SELECT title FROM 
films WHERE made < '1990-01-01') AND name NOT IN (SELECT name FROM casting 
where title in (SELECT title FROM films WHERE made >= '1990-01-01'));

(9)
SELECT name,title,director FROM casting c WHERE c.name IN (SELECT name FROM 
casting WHERE name = c.name AND title !=c.title) ORDER BY name;

(10)
SELECT DISTINCT name FROM casting WHERE title IN (SELECT DISTINCT title
FROM casting WHERE name = 'Ian Holm') AND name != 'Ian Holm' OR director = 
'Alfred Hitchcock' ORDER BY name;

