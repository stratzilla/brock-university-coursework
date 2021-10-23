merge :: Ord a => [a] -> [a] -> [a]
merge xs [] = xs
merge [] ys = ys
merge (x:xs) (y:ys)
	| y > x = x : merge xs (y:ys)
	| otherwise = y : merge (x:xs) ys

rangeProduct :: Integer -> Integer -> Integer
rangeProduct m n
	| m < n = n
	| otherwise = product [m,(m-1)..n]

factors :: Int -> [Int]
factors x
	| x <= 0 = []
	| otherwise = [y | y <- [1..x], x `mod` y == 0]

perfects :: Int -> [Int]
perfects x = [y | y <- [1..x], (sum (factors y) == 2 * y)]