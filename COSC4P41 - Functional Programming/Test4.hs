import Data.Char
import Control.Monad.Trans
import Control.Monad.Trans.State

separate1 :: String -> (String,String)
separate1 (x:xs)
	| isDigit x = (x:(fst $ separate1 xs), (snd $ separate1 xs))
	| not $ isDigit x = ((fst $ separate1 xs), x:(snd $ separate1 xs))
separate1 _ = ("","")

separate2 :: String -> (String,String)
separate2 xs = foldr(\x acc->
	if isDigit x then 
		(x:(fst $ acc), snd $ acc)
	else 
		(fst $ acc, x:(snd $ acc))
	) ("","") xs
separate2 _ = ("","")

type Point = (Float,Float)

data Rectangle = Rect1 Point Float Float | Rect2 Point Point deriving Show

toStandardForm :: Rectangle -> Rectangle
toStandardForm (Rect1 p w h) = Rect1 p w h
toStandardForm (Rect2 p q) = Rect1 p (abs (fst q - fst p)) (abs (snd q - snd p))

area :: Rectangle -> Float
area (Rect1 p w h) = w * h
area (Rect2 p q) = area (toStandardForm (Rect2 p q))

instance Eq Rectangle where
	(==) (Rect1 p1 w1 h1) (Rect1 p2 w2 h2) = (p1 == p2 && w1 == w2 && h1 == h2)
	(==) (Rect1 p w h) (Rect2 q1 q2) = (Rect1 p w h) == toStandardForm (Rect2 q1 q2)
	(==) (Rect2 q1 q2) (Rect1 p w h) = (Rect1 p w h) == (Rect2 q1 q2)
	(==) (Rect2 p1 p2) (Rect2 q1 q2) = (p1 == q1 && p2 == q2)
	(==) _ _ = False

lucasNumbers :: [Integer]
lucasNumbers = 2:1:(zipWith (+) lucasNumbers (tail $ lucasNumbers))

lucas :: Int -> Integer
lucas = (lucasNumbers !!)
