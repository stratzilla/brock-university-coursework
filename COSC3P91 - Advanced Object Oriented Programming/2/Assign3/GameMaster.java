public GameMaster(String[] names, Port[] ports) {
	this.ports = ports;
	robots = new Robot[names.length];
	Factory factory = Factory.load("factory.xml");
	for (int i=0; i<names.length; i++) {
		robots[i] = new Robot(names[i],factory.getStartConfigByID(i),true);
	}
	board = new Board(factory,robots);
	/* for (Player p : players) {
		p.receiveBoard(board);
	} */
	for (Port p : this.ports) {
		// send board to each player
		p.send(this.board.toXMLString());
	}
	cardFactory = new CardFactory();
}

public void run() {
	final int numberPlayers = robots.length;
	CardList[] pCardList  = new CardList[numberPlayers];
	for(int i=0; i<numberPlayers; i++) {
		pCardList[i] = new CardList();
	}
	CardList phaseList = new CardList();
	EventList events = new EventList();
	List<Integer> effectExecutionOrder = new ArrayList<Integer>(numberPlayers);
	for (int i=0; i<numberPlayers; i++) {
		effectExecutionOrder.add(i);
	}
	int phase;
	while (!events.containsVictory()) {
		for (int i=0; i<numberPlayers; i++) {
			pCardList[i].clear();
			for (int j=0; j<7; j++) {
				pCardList[i].add(cardFactory.createCard(i));
			}
			//pCardList[i] = players[i].selectCards(pCardList[i]);
			// send card list to each player
			this.ports[i].send(pCardList[i].toXMLString());
		}
		for (int i = 0; i < numberPlayers; i++) {
			// receive selected cards from each player
			pCardList[i] = CardList.read(
				new StringReader(this.ports[i].receive()));
		}
		phase = 0;
		events.clear();
		while (phase < 5 && !events.containsVictory()) {
			phase++;
			phaseList.clear();
			for (int i=0; i<numberPlayers; i++) {
				phaseList.add(pCardList[i].get(phase-1));
			}
			Collections.sort(phaseList);
			for (Card card : phaseList) {
				card.execute(events,board);
			}
			Collections.sort(effectExecutionOrder);
			Collections.shuffle(effectExecutionOrder);
			for(int playerID : effectExecutionOrder) {
				board.effect(events,playerID,phase);
			}
			if (!events.containsVictory()) {
				events.add(new EventRunDecorations(phase));
			}               
		}
		if (!events.containsVictory()) {
			board.revitalize(events);
		}
		/* for(Player p : players) {
			p.receiveEvents(events);
		} */
		for (Port p : this.ports) {
			// send events to players
			p.send(events.toXMLString());
		}
	}
}