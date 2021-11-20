
module Plot where 
 
import Array 
import GraphicsUtils 
 
type PlotLine = ( ( Float, Float ), ( Float, Float ) ) 
 
borderSize :: Int 
borderSize = 50 
 
imageSize :: Int 
imageSize = 500 
 
windowSize :: Int 
windowSize = imageSize + borderSize  
 
-- 
-- drawLines - draws in Yellow with width 1.  To build nice plants students 
-- will need to generalise this (optional task for competition contenders) 
-- 
drawLines :: [ PlotLine ] -> IO() 
drawLines ls 
  = drawLines' ls p s 
  where 
    ( p, s ) = computeScale ls 
 
drawLines' :: [ PlotLine ] -> ( Float, Float) -> Float -> IO() 
drawLines' ls ( minx, miny ) width 
  = runGraphics ( do 
      w <- openWindow "ICLab" ( windowSize, windowSize ) 
      lines w ls 
      getKey w 
      closeWindow w 
      ) 
  where 
    lines :: Window -> [ PlotLine ] -> IO() 
    lines w [] 
      = return() 
    lines w ( ( p, p' ) : ls ) 
      = do drawInWindow w ( colLine ( scale p ) ( scale p' ) ) 
           lines w ls 
    pen = mkPen Solid 1 ( colorTable ! Yellow )  
    colLine x y 
      = pen ( \p -> withPen p ( line x y ) ) 
    scale:: (Float, Float) -> (Int, Int ) 
    scale ( x, y ) = ( b2 + round ( ( x - minx ) * r ),  
                       windowSize - ( b2 + round ( ( y - miny ) * r ) ) ) 
    r = fromInt imageSize / width 
    b2 = borderSize `div` 2 
     
 
-- 
-- computeScale finds the 'lower left' corner and width of the axis-aligned 
-- bounding box in 2D spac. It caters for disconnected regions.  
-- Note that if you walk the list of lines all at once you get stack overflow for 
-- quite modest lists. This version breaks the lists into sublists of length 100 
-- and patches up afterwards - a real mess, but a problem with the implementation 
-- of foldl 
-- 
computeScale :: [ PlotLine ] -> ( ( Float, Float ), Float ) 
computeScale ls  
  = ( ( minx, miny ), max ( maxx - minx ) ( maxy - miny ) ) 
  where 
    ( minx, maxx, miny, maxy ) =  
       ( minimum ( map first xs ), maximum ( map second xs ), 
         minimum ( map third xs ), maximum ( map fourth xs ) ) 
 
    xs = map ( foldr min4 ( inf, -inf, inf, -inf ) ) ( split ls 100 ) 
 
    inf :: Float 
    inf = 100000 
  
    min4 ( ( x, y ), ( x', y' ) ) ( a, b, c, d ) 
      = ( min a ( min x x' ),  
          max b ( max x x' ),  
          min c ( min y y' ),  
          max d ( max y y' ) )  
 
    first ( a, b, c, d )  = a 
    second ( a, b, c, d ) = b 
    third ( a, b, c, d )  = c 
    fourth ( a, b, c, d ) = d 
 
    split :: [ a ] -> Int -> [ [ a ] ] 
    split [] n 
      = [] 
    split xs n 
      = a : split b n 
      where ( a, b ) = splitAt n xs 
