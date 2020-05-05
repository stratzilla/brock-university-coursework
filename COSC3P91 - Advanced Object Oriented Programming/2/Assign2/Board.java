package roborace.server;

public class Board implements XMLObject {
	
    public void step(EventList events, int playerID, Direction direction) {
        // create empty event list
        EventList destroyedEvents = new EventList();
        // recurse for each event in the step
        stepRecursive(events, destroyedEvents, playerID, direction);
        // add eventwait to events
        events.add(new EventWait());
        // if destroyed events isn't empty
        if (!destroyedEvents.isEmpty()) {
            // add destroyed events to events
            events.addAll(destroyedEvents);
            // and an additional eventwait
            events.add(new EventWait());
        }
    }

    private void stepRecursive(EventList events, EventList destroyedEvents, int playerID, Direction direction) {
        // get robot corresponding to moving player
        Robot activePlayer = this.robots[playerID];
        // get activeplayer position
        Point currentPosition = activePlayer.getPosition();
        // if no wall in the way
        if (!this.factory.hasWall(activePlayer.getPosition(), direction)) {
            // used to find new poisition momentarily
            Point newPosition;
            // temporarily set to current position; will be overwritten
            int newX = currentPosition.x;
            int newY = currentPosition.y;
            switch(direction) {
                case North: // if moving north
                    newY -= 1;
                    break;
                case South: // south
                    newY += 1;
                    break;
                case East: // east
                    newX += 1;
                    break;
                case West: // west
                    newX -= 1;                
                    break;
            }
            // set as new position as found in switch complex above
            newPosition = new Point(newX, newY);
            // find boundaries of the game
            int boundXMin = 0, boundXMax = this.factory.getXSize() - 1;
            int boundYMin = 0, boundYMax = this.factory.getYSize() - 1;
            // if new position would leave boundaries
            if ((newX < boundXMin || newY < boundYMin)
            ||  (newX > boundXMax || newY > boundYMax)
            ||  (this.factory.isPitAt(newPosition))) {
                // set new poisition
                activePlayer.setPosition(newPosition);
                // and destroy the robot
                activePlayer.destroyed();
                // add event step and event destroyed
                events.add(new EventStep(playerID, direction));
                destroyedEvents.add(new EventDestroyed(playerID));
            } else {
                // get robot id at position
                int robotID = robotIDAt(newPosition);
                if (robotID == -1) { // if no robot
                    // place active robot at position
                    activePlayer.setPosition(newPosition);
                    // add event to list
                    events.add(new EventStep(playerID, direction));
                } else { // if robot
                    // recurse using that robot's ID
                    stepRecursive(events, destroyedEvents, robotID, direction);
                    // now recheck if free of robots
                    int newRobotID = robotIDAt(newPosition);
                    if (newRobotID == -1) { // if there is still robot
                        // add bump event
                        events.add(new EventBump(playerID, direction));
                    } else { // otherwise
                        // relocate active robot
                        activePlayer.setPosition(newPosition);
                        events.add(new EventStep(playerID, direction));
                    }
                }
            }
        } else {
            // if there is an obstructing wall, add EventBump to events
            events.add(new EventBump(playerID, direction));
        }
    }
    
}