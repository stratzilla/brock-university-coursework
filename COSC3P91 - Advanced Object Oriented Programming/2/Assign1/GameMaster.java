public GameMaster(String[] names, Player[] players) {
	this.players = players; // store players
	Factory factory = Factory.load("factory.xml"); // load config
	this.robots = new Robot[players.length]; // initialize robots
	// for each robot, initialize start config and name
	for (int i = 0; i < robots.length; i++) {
		// create new robot
		Robot r = new Robot(names[i], factory.getStartConfigByID(i), true);
		// initialize into robot array
		robots[i] = r;
		// this is done to keep w/in 80 line constraint
	}
	this.board = new Board(factory, robots); // construct board on F & R
	for (Player player : this.players) {
		// give board to each player
		player.receiveBoard(this.board);
	}
	// construct card factory
	this.cardFactory = new CardFactory();
}

public void run() {
	// create a card list for each player
	CardList[] playerCards = new CardList[this.players.length];
	for (int i = 0; i < playerCards.length; i++) {
		playerCards[i] = new CardList();
	}
	// create an event list
	EventList eventList = new EventList();
	// victory condition flag
	boolean victory = false;
	while (!victory) { // while victory not reached
		// empty player hands and redraw 7 new cards
		for (int i = 0; i < playerCards.length; i++) {
			playerCards[i].clear();
			for (int j = 0; j < 7; j++) {
				Card drawnCard = this.cardFactory.createCard(i);
				playerCards[i].add(drawnCard);
			}
		}
		// allow players to select cards
		for (int i = 0; i < this.players.length; i++) {
			// get the player chosen cards
			CardList choice = this.players[i].selectCards(playerCards[i]);
			// set player card list as these choices
			playerCards[i] = choice;
		}
		int phaseCount = 0; // count how many phases
		eventList.clear(); // clear event list
		// do while phases < 5 and no victory conditions met
		while (phaseCount < 5 && !victory) {
			CardList playedCards = new CardList();
			for (int i = 0; i < this.players.length; i++) {
				// get ith card per player
				playedCards.add(playerCards[i].get(phaseCount));
			}
			// sort the cards by priority
			Collections.sort(playedCards);
			// execute each card in sequence
			for (Card c : playedCards) {
				c.execute(eventList, this.board);
			}
			// get a random order
			int[] order = new int[this.players.length];
			for (int i = 0; i < order.length; i++) {
				order[i] = i;
			}
			// shuffle ordering
			Collections.shuffle(Arrays.asList(order));
			// execute board state in random order
			for (int i = 0; i < order.length; i++) {
				board.effect(eventList, order[i], phaseCount);
			}
			if (!eventList.containsVictory()) {
				// if victory not found, add eventrundecoration
				eventList.add(new EventRunDecorations(phaseCount));
			} else {
				// otherwise denote as victory
				victory = true;
			}
			phaseCount++;
		}
		// revive dead robots
		this.board.revitalize(eventList);
		// give eventlist to each player
		for (Player player : this.players) {
			player.receiveEvents(eventList);
		}
	}
}