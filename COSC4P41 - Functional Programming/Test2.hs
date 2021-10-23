import Data.List hiding (union)
import Data.Word

type Byte = Word8

class Eq a => Hashable a where
	hashcode :: a -> Byte

instance Hashable Bool where
	hashcode True = 1
	hashcode False = 0

instance Hashable Int where
	hashcode n = toEnum (n `mod` 256)

instance Hashable Char where
	hashcode n = hashcode (fromEnum (n))

instance (Hashable a, Hashable b) => Hashable(a,b) where
	hashcode (x,y) = 31 * hashcode (x) + hashcode (y)

instance (Hashable a, Hashable b, Hashable c) => Hashable(a,b,c) where
	hashcode (x,y,z) = hashcode ((x,y),z)

instance Hashable a => Hashable [a] where
	hashcode [] = 0
	hashcode (x:xs) = 31 * hashcode (xs) + hashcode (x)
    
type HashSet a = Byte -> [a]

exampleSet1 :: HashSet Int
exampleSet1 97 = [353,97]
exampleSet1 49 = [305]
exampleSet1 10 = [266,10]
exampleSet1 _  = []

exampleSet2 :: HashSet Int
exampleSet2 80 = [80]
exampleSet2 49 = [49,305]
exampleSet2 10 = [522]
exampleSet2 _  = []

--elements :: hashSet a -> [a]
--elements s = concatMap ([minBound..maxBound], s)

--elemSet :: Hashable a => a -> HashSet a -> Bool
--elemSet x s = hashcode (x) `elem` elements (s)