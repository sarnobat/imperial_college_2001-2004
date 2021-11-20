--
-- TEST HARNESS FOR THE HASKELL DATABASE EXERCISE
--

module CatAdmin where

import "Database.hs"



--
-- the user-level function
--

admin :: IO ()
admin = interact
        (\input_stream -> intro ++ prompt ++ parse_stream input_stream Empty)
        -- the database is initialized explicitly to the constant Empty,
        -- rather than calling create, so that the menu can appear initially
        -- even for those who have not yet written any of the functions.



--
-- auxiliary support functions
--

intro :: [Char]
intro = "\n"++"           Cattery Administration Tool in Haskell \n\n"  ++
              "                 Version 1.1       \n\n"  ++
              "          TYPE help  for help menu.\n\n\n"


parse_stream :: [Char] -> Database -> [Char]
parse_stream [] d      = "\n"++"cat admin tool logout"
parse_stream input d   = commandline ++ "\n" ++ message ++
				case continue of
				  True  -> prompt ++ parse_stream remains newd
				  False -> []
                         where
                         commandline               = before '\n' input
                         remains                   = after '\n' input
                         command                   = parse_line commandline
                         (continue, message, newd) = processcommand command d


parse_line :: [Char] -> [[Char]]
parse_line [] = []
parse_line (h:t) | isalphanum h = capitalise firstword : parse_line otherwords
                 | otherwise    =  parse_line skip
                   where
                   firstword  = takeWhile isalphanum (h:t)
                   otherwords = dropWhile isalphanum (h:t)
                   skip       = dropWhile notalphanum (h:t)


processcommand :: [[Char]] -> Database -> (Bool, [Char], Database)
processcommand []                d = (True, [], d)
processcommand ("ADD":n:s:t:[])  d | isnum t
					= (True, [], (add n s tval d))
                                   | otherwise
					= (True,
					   "Usage: add name sort tins\n", d)
                                     where tval = numval t
processcommand ("ADD":args)      d = (True, "Usage: add name sort tins\n", d)
processcommand ("DELETE":n:s:[])   d = (True, [], delete (n,s) d)
processcommand ("DELETE":args)   d = (True, "Usage: delete name sort\n", d)
processcommand ("CREATE":args)   d = (True, [], create)
processcommand ("PUT":args) d = (True, (putdatabase d)++"\n", d)
processcommand ("LOOKUP":n:s:[])  d = (True, (do_lookup_cat (n,s) d)++"\n", d)
processcommand ("LOOKUP":args)  d = (True, "Usage: lookup name sort\n", d)
processcommand ("COUNT":args)    d = (True, (do_count d)++"\n", d)
processcommand ("HELP":args)     d = (True, help_menu, d)
processcommand ("QUIT":args)     d = (False, "cat admin tool logout", d)
processcommand (unknown:args)    d = (True, "unrecognised command"++"\n", d)


help_menu :: [Char]
help_menu = "\nHelp Menu\n"++
            "            add name sort tins    - to add a cat\n"++
            "            delete name sort      - to delete a cat\n"++
            "            create                - to restart with a fresh empty database\n"++
                                -- this allows testing of create, which would
                                -- otherwise never be used anywhere!
            "            put                   - to show the database\n"++
            "            lookup name sort      - to look up a cat by name\n"++
            "            count                 - to count cats, sorts, and tins needed\n"++
            "            help                  - to see this menu.\n"++
            "            quit                  - to exit\n\n"



--
-- database-handling code
--

do_lookup_cat :: ([Char],[Char]) -> Database -> [Char]
do_lookup_cat (n,s) d  | t == -1   = n ++ " is not in the database"
                       | otherwise = n ++ " the " ++ s ++ " needs "
                                       ++ show t ++ " tins of cat food weekly"
                         where t = lookup_cat (n,s) d

do_count :: Database -> [Char]
do_count d = "No. of cats = " ++ show n ++ ", number of sorts  = " ++ show s ++
                ", total number of tins needed = " ++ show t
            where (n,s,t) = count d



--
-- low-level stuff.
--

capitalise :: [Char] -> [Char]
capitalise [] = []
capitalise (h:t) = (cap h):(capitalise t)

cap :: Char -> Char
cap ch | islowercase ch = chr ((ord ch) + offset)
       | otherwise      = ch
         where offset = (ord 'A') - (ord 'a')

islowercase :: Char -> Bool
islowercase ch = ((ch >= 'a') && (ch <= 'z'))

isdigit :: Char -> Bool
isdigit ch = ((ch >= '0') && (ch <= '9'))

isnum :: [Char] -> Bool
isnum ('-':t) = isnum t
isnum n = isanumber n

isanumber :: [Char] -> Bool
isanumber [] = False
isanumber (h:[]) = isdigit h
isanumber (h:t)  = (isdigit h) && (isanumber t)

numval :: [Char] -> Int
numval s = numval_aux 0 s
	   where
	   numval_aux acc []    = acc
	   numval_aux acc (h:t) = numval_aux (10*acc+(ord h - ord '0')) t

before :: Char -> [Char] -> [Char]
before ch l  = takeWhile (/= ch) l

isalphanum :: Char -> Bool
isalphanum '.' = True
isalphanum '-' = True
isalphanum ch = (isletter ch) || (isdigit ch)

notalphanum :: Char -> Bool
notalphanum c = not (isalphanum c)

after :: Char -> [Char] -> [Char]
after ch [] = []
after ch l  | drop /= [] = tail drop
            | otherwise  = []
              where drop = dropWhile (/= ch) l

isletter :: Char -> Bool
isletter ch = ((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))

prompt :: [Char]
prompt = "admin> "
