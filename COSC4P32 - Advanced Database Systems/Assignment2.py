def pin(self, page_id):
	# if exists in free frame array, just remove it
	if page_id in self.getFreeFrames():
		self.getFreeFrames().remove(page_id)
		return True
	return False

def unpin(self, page_id):
	# unpin page_id, adding it to free frames
	self.getFreeFrames().append(page_id)
	return False

def victim(self):
	# provided the free frame array is nonempty
	if self.replacerSize() > 0:
		# then the first element would have been least recently accessed
		return self.getFreeFrames().pop(0)
	return None # otherwise the array is empty

def fetchPage(self, page_id):
	# if the page exists in buffer pool, it would have entry in page table
	if page_id in self.getPageTable():
		# find the specific page
		for page in self.getBufferPool():
			if page.page_id == page_id: # found
				# increment its pin counter
				page.incrementPinCount()
				# pin it
				self.getReplacer().pin(page_id)
				# return it immediately
				return page
	# otherwise page doesn't exist in buffer pool but does on disk
	elif page_id in self.getDiskManager().invalid:
		# find how much space is left in the buffer pool
		bufferMaxSize = self.buffer_total_no_of_frames
		bufferCurSize = len(self.getBufferPool())
		difference = bufferMaxSize - bufferCurSize
		# if there is no space
		if difference <= 0:
			# get possible victim page to evict
			victim = self.getReplacer().victim()
			# if no victim found
			if victim is None:
				print('No pages can be evicted to make room for:', page_id)
				return None
			# if you get to this point a victim is found, so evict it
			# find the victim page
			for page in self.getBufferPool():
				if page.page_id == victim:
					# check if its dirty
					if page.isDirty():
						# if so, rewrite to disk
						self.getDiskManager().writePage(page)
						# and remove from buffer
						self.getBufferPool().remove(page)
			# remove from page table
			self.getPageTable().remove(victim)
		# now there is room
		page = self.newPage(page_id)
		return page
	# if it gets here, page doesn't exist in buffer or on disk
	return None


def newPage(self, page_id):
	# attempt to get page from disk
	page = self.getDiskManager().readPage(page_id)
	# if page doesn't exist, shout error and return nothing
	if page is None:
		print('Page doesn\'t exist on disk:', page_id)
		return None
	# otherwise, get page from disk, put in buffer pool
	self.getBufferPool().append(page)
	# increment pin count of page
	page.incrementPinCount()
	# pin it
	self.getReplacer().pin(page_id)
	# add page_id to page table
	self.getPageTable().append(page_id)
	# return the page object
	return page

def deletePage(self, page_id):
	# if page exists in free frame list
	if page_id in self.getReplacer().getFreeFrames():
		# find the page
		for page in self.getBufferPool():
			if page.page_id == page_id:
				# delete from everywhere
				self.getDiskManager().deletePage(page)
				self.getBufferPool().remove(page)
				self.getPageTable().remove(page_id)
	else:
		print('Cannot delete page:', page_id)

def unpinPage(self, page_id, is_dirty):
	for page in self.getBufferPool(): # find the page
		if page.page_id == page_id:
			page.dirty = is_dirty # set dirtiness
			page.decrementPinCount() # decrement pin count
			if page.getPinCount() == 0:
				# if pin count is zero, it can be unpinned
				self.getReplacer().unpin(page_id)        

def flushPage(self, page_id):
	# write page to disk irrespective of status
	self.getDiskManager().write(page_id)
	for page in self.getBufferPool(): # look for the page
		if page.page_id == page_id:
			# set its dirtiness to clean
			page.dirty = False
	return False

def flushAllPages(self):
	# foreach loop over page ids
	for page_id in self.getPageTable():
		# individually flush them in sequence
		self.flushPage(page_id)
	return False
