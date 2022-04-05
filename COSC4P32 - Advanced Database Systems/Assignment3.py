def fetchNext(self): # SequentialScanExecutor
	pred = self.plan.getPredicate()
	sche = self.plan.getOutputSchema()
	curr = self.next_tuple # set some placeholder to iterate shortly
	while curr is not None: # while not EOF
		# if predicate evaluates to True, return that row
		# when no predicate, need to allow return of tuples without restriction
		if pred is None or pred.evaluatePredicate(sche, curr):
			# change internal next tuple to reflect place last left off
			self.next_tuple = self.dbFileIterator.getNext()
			return curr # return currently iterated tuple
		# iterate to next tuple
		curr = self.dbFileIterator.getNext()
	self.reset() # set seek to 0
	return None # if it gets here, was EOF

def fetchNext(self): # NestedLoopJoinExecutor
	pred = self.plan.getPredicate()
	sche_lhs = self.left_executor.plan.getOutputSchema()
	sche_rhs = self.right_executor.plan.getOutputSchema()
	curr_lhs = self.left_tuple # keep track of currently iterated LHS tuple
	curr_rhs = self.right_tuple # likewise for right-hand
	while curr_lhs is not None: # while LHS is not EOF
		while curr_rhs is not None: # while RHS is not EOF
			# if predicate eval to True, return concatenation of LHS+RHS rows
			if pred.evaluateJoin(sche_lhs, curr_lhs, sche_rhs, curr_rhs):
				# change internal next RHS tuple to reflect place last left off
				self.left_tuple = curr_lhs
				self.right_tuple = self.right_executor.getNext()
				return curr_lhs+curr_rhs # return the joined row
			# iterate to next RHS tuple for inner loop
			curr_rhs = self.right_executor.getNext()
		# reset RHS tuple to restart inner loop on next outer loop call
		self.resetRightTuple()
		curr_rhs = self.right_tuple # and reset current to continue
		# iterate to next LHS tuple for outer loop
		curr_lhs = self.left_executor.getNext()
	self.left_executor.reset() # set LHS seek to 0
	self.right_executor.reset() # likewise for RHS
	return None # if it gets here, was EOF for LHS and RHS

def fetchGenerator(self):
    # treat map as generator and yield rows
    for row in self.group_map:
        yield row

def fetchNext(self): # AggregatorExecutor
    # on first fetch, generate the whole map first
    if not self.group_map: # if the map has not yet been added to
        self.fetchGen = self.fetchGenerator() # initialize the generator
        group_by = self.plan.getGroupByAt() # get name of group-by column
        agg_at = self.plan.getAggregateAt() # get name of aggregate column
		exec_schema = self.child_executor.plan.getOutputSchema() # get schema
		group_idx = exec_schema.getColunmIdx(group_by) # get index for group col
		agg_idx = exec_schema.getColunmIdx(agg_at) # same for aggregate col
        curr = self.child_executor.fetchNext() # keep track of current tuple
        while curr is not None: # while not EOF
            # add to map
            self.insert(curr[group_idx], curr[agg_idx])
            curr = self.child_executor.fetchNext() # go to next
    # use a try/catch just in case of error since can't tell if gen is empty
    try:
        val = next(self.fetchGen) # get the next yielded row from generator
    except StopIteration: # if null
        return None # you've reached EOF
    return (val, self.group_map[val]) # otherwise return the row

def fetchNext(self):
    group_by = self.plan.getGroupByAt() # get name of group-by column
    agg_at = self.plan.getAggregateAt() # get name of aggregate column
	exec_schema = self.child_executor.plan.getOutputSchema() # get schema
    group_idx = exec_schema.getColunmIdx(group_by) # get index for group col
    agg_idx = exec_schema.getColunmIdx(agg_at) # same for aggregate col
    self.child_executor.reset() # reset the executor to seek 0
    target = self.child_executor.fetchNext() # first tuple target to group by
    if target is None: # if EOF, return None to stop executor
        return None
    '''
    The aggregate result is iteratively built up, but we don't want to
    waste time retrying tuples already added and aggregated. To this
    end, we want to iterate the target tuple until we find one that
    doesn't already exist in the aggregate map.

    Once we have a target, we look at tuples AFTER the target, because
    rows before the target would have been already aggregated. Technically
    the algorithm should speed up the longer it goes on because it will pass
    over tuples that share the same group index.
    '''
    while target[group_idx] in self.group_map and target is not None:
        target = self.child_executor.fetchNext()
    # if target is not in the map, it's safe to assume it hasn't been seen
    if target[group_idx] not in self.group_map:
        # so add it to the map
        self.insert(target[group_idx], target[agg_idx])
        # then iterate over tuples succeeding the target
        curr = self.child_executor.fetchNext()
        while curr is not None: # while not EOF
            # if match to target, increment its aggregate
            if curr[group_idx] == target[group_idx]:
                self.insert(target[group_idx], curr[agg_idx])
            curr = self.child_executor.fetchNext() # go to next
        # at the end of this, return the aggregate data for that group
        return (target[group_idx], self.group_map[target[group_idx]])
