public EventList convertXMLNode(Node node) {
	// make an event list
	EventList eventList = new EventList();
	// make an eventreader
	EventReader eventListReader = new EventReader();
	// if event found
	if (node.getNodeName().equals("EventList")) {
		// get list of nodes
		List<Node> listNodes = COSC3P91.xml.XMLTools.getChildNodes(node);
		for (Node n : listNodes) {
			// parse node as event and add to eventlist
			eventList.add(eventListReader.convertXMLNode(n));
		}
	}
	return eventList;
}	