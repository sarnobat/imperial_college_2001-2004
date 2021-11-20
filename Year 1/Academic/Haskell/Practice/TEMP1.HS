secs2hms :: Int -> (Int,Int,Int)
secs2hms s = (hours,mins,secs)
	where hours = div s (60^2)
	      mins  = div (s-hours*60^2) 60
	      secs  = s -mins*60-hours*60*60

lastdig :: Int -> Int
lastdig x = mod x 10
