quicksort :: [Int] -> [Int]
quicksort []	= []
quicksort (h:t) = quicksort l1 ++ [h] ++ quicksort l2
	w" re 	l1 = [x|x<-t,x<h]
		l2 = [x|x<-t,x>=h]
