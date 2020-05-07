public CardList selectCards(CardList list) {
	// store list parameter in member field cards
	this.cards = list;
	// denote that player can now select cards
	this.selecting = true;
	// enable OK button
	okButton.setEnabled(true);
	// repaint frame
	this.repaint();
	// allow thread to wait for selection
	synchronized(this) {
		// synchronized block
		while (this.selecting) {
			try {
				// wait
				wait();
				// until while loop breaks
			} catch (InterruptedException ex) {
				Logger.getLogger(CardPane.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	return this.cards;
}

public void stopSelection() {
	if (this.selectedCards.size() != 5) { // if lt/gt five cards selected
		// remind user to select five cards
		String dialogTitle = "Cards Missing!";
		String dialogText = "You must select five cards to continue.";
		GameDialogs.showMessageDialog(dialogTitle, dialogText, this.parent);
	} else { // if five cards selected
		// cards become selected card
		this.cards = this.selectedCards;
		// reset selected cards
		this.selectedCards = new CardList();
		// note selection process is over
		this.selecting = false;
		// disable OK button
		okButton.setEnabled(false);
		synchronized(this) {
			// notify player thread
			notify();
		}
		this.repaint();
	}
}

public void paintComponent(Graphics g) {
	// fill space behind card
	g.fillRect(0, 0, this.getSize().width, 120);
	for (int i = 0; i < 7; i++) { // for each potential card
		// if no card in slot or less than selected cards max
		if (this.cards.isEmpty() || this.cards.size() <= i) {
			// draw blank card
			g.drawImage(this.noCard, (i*92), 0, null);
		} else { // if card in slot
			// draw card image
			g.drawImage(this.cards.get(i).getImage(), (i*92), 0, null);
			// if card selected
			if (this.selectedCards.indexOf(this.cards.get(i)) != -1) {
				// draw roman numeral
				int idx = this.selectedCards.indexOf(this.cards.get(i));
				g.drawImage(this.selectImages[idx], (i*92)+30, 50, null);
			}
		}
	}
}

public void mousePressed(MouseEvent e) {
	if (this.selecting) { // only process moust evt if selection process
		if (e.getButton() == MouseEvent.BUTTON1) { // if left click
			int numCard = e.getX() / 92; // get card from x-position
			if ((numCard >= 0 && numCard < 7) // if cards0-6
			&&  (this.selectedCards.size() <= 5)) { // and selected not full
				// if card not found already in selected
				Card selectedCard = this.cards.get(numCard);
				if (this.selectedCards.indexOf(selectedCard)== -1) {
					// if not at maximum, we can add
					if (this.selectedCards.size() != 5) {
						// add it to selected
						this.selectedCards.add(selectedCard);
					} else { // at maximum, tell user
						String dialogTitle = "Card Maximum Reached!";
						String dialogText = "You cannot select any more.";
						GameDialogs.showMessageDialog(dialogTitle, 
							dialogText, this.parent);
					}
				} else { // if card already in selected
					// remove it from selected
					this.selectedCards.remove(selectedCard);
				}
			}
		}
	}
	this.repaint();
}