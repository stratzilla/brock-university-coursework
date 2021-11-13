data Stack a = Stack [a] deriving Show

empty :: Stack a
empty = Stack []

isEmpty (Stack xs) = null xs

push :: Stack a -> a -> Stack a
push (Stack xs) x = Stack(x:xs)

pop :: Stack a -> Maybe (a, Stack a)
pop (Stack (x:xs)) = Just (x, Stack xs)
pop (Stack []) = Nothing

peek :: Stack a -> Maybe a
peek (Stack (x:xs)) = Just x
peek (Stack []) = Nothing

pop2 :: Stack a -> Maybe ((a, a), Stack a)
pop2 (Stack (x1:x2:xs)) = Just ((x1, x2), Stack xs)
pop2 (Stack [x1]) = Nothing
pop2 (Stack []) = Nothing

data SMInst = PUSH Int | ADD | MINUS | MULT | DIV deriving Show

type SMProg = [SMInst]

exampleProg1 :: SMProg
exampleProg1 = [PUSH 3, PUSH 4, PUSH 5, ADD, MULT]

exampleProg2 :: SMProg
exampleProg2 = [PUSH 3, PUSH 4, ADD, DIV]

evalSMInst :: Stack Int -> SMInst -> Maybe (Stack Int)
evalSMInst (Stack xs) (PUSH n) = Just (push (Stack xs) n)
evalSMInst (Stack xs) (ADD) = case pop2 (Stack xs) of
	Nothing -> Nothing
	Just ((x1, x2), xs) -> Just (push xs (x1+x2))
evalSMInst (Stack xs) (MINUS) = case pop2 (Stack xs) of
	Nothing -> Nothing
	Just ((x1, x2), xs) -> Just (push xs (x1-x2))
evalSMInst (Stack xs) (MULT) = case pop2 (Stack xs) of
	Nothing -> Nothing
	Just ((x1, x2), xs) -> Just (push xs (x1*x2))
evalSMInst (Stack xs) (DIV) = case pop2 (Stack xs) of
	Nothing -> Nothing
	Just ((x1, x2), xs) -> Just (push xs (x1 `div` x2))

evalSMProg :: Stack Int -> SMProg -> Maybe (Stack Int)
evalSMProg (Stack xs) p = Nothing
