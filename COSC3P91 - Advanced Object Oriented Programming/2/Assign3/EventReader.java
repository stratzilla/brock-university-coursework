public Event convertXMLNode(Node node) {      
	String nodeName = node.getNodeName();
	Event newEvent = null;
	if (nodeName.equals("EventBump")) {
		int playerID = COSC3P91.xml.XMLTools.getIntAttribute(node,
			"playerID");
		Direction d = (Direction)getEnumAttribute(Direction.class, node, 
			"direction");
		newEvent = new EventBump(playerID, d);
	} else if (nodeName.equals("EventCardEnd")) {
		newEvent = new EventCardEnd();
	} else if (nodeName.equals("EventCardStart")) {
		List<Node> listNodes = COSC3P91.xml.XMLTools.getChildNodes(node);  
		CardReader cardParser = new CardReader();
		Card card = cardParser.convertXMLNode(listNodes.get(0));
		newEvent = new EventCardStart(card);
	} else if (nodeName.equals("EventDestroyed")) {
		int playerID = COSC3P91.xml.XMLTools.getIntAttribute(node,
			"playerID");
		newEvent = new EventDestroyed(playerID);
	} else if (nodeName.equals("EventEffectEnd")) {
		newEvent = new EventEffectEnd();
	} else if (nodeName.equals("EventEffectStart")) {
		int playerID = COSC3P91.xml.XMLTools.getIntAttribute(node,
			"playerID");
		int x = COSC3P91.xml.XMLTools.getIntAttribute(node, "x");
		int y = COSC3P91.xml.XMLTools.getIntAttribute(node, "y");
		boolean isDec = COSC3P91.xml.XMLTools.getBoolAttribute(node, 
			"isDecorationEffect");
		newEvent = new EventEffectStart(x, y, playerID, isDec);
	} else if (nodeName.equals("EventRevitalize")) {
		int playerID = COSC3P91.xml.XMLTools.getIntAttribute(node,
			"playerID");
		int startID = COSC3P91.xml.XMLTools.getIntAttribute(node,
			"startID");
		newEvent = new EventRevitalize(playerID, startID);
	} else if (nodeName.equals("EventRunDecorations")) {
		int phase = COSC3P91.xml.XMLTools.getIntAttribute(node, "phase");
		newEvent = new EventRunDecorations(phase);
	} else if (nodeName.equals("EventStep")) {
		Direction d = (Direction)getEnumAttribute(Direction.class, node, 
			"direction");
		int playerID = COSC3P91.xml.XMLTools.getIntAttribute(node,
			"playerID");
		newEvent = new EventStep(playerID, d);
	} else if (nodeName.equals("EventTurn180")) {
		int playerID = COSC3P91.xml.XMLTools.getIntAttribute(node,
			"playerID");
		newEvent = new EventTurn180(playerID);
	} else if (nodeName.equals("EventTurn90")) {
		int playerID = COSC3P91.xml.XMLTools.getIntAttribute(node,
			"playerID");
		boolean clockwise = COSC3P91.xml.XMLTools.getBoolAttribute(node, 
			"clockwise");
		newEvent = new EventTurn90(playerID, clockwise);
	} else if (nodeName.equals("EventVictory")) {
		int playerID = COSC3P91.xml.XMLTools.getIntAttribute(node,
			"playerID");
		newEvent = new EventVictory(playerID);
	} else if (nodeName.equals("EventWait")) {
		newEvent = new EventWait();
	}
	return newEvent;
}