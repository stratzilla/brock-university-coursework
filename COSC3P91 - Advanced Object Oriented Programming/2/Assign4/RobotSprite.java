public void destroyed() {
	// stop animation
	this.currentAnimation.stop();
	// set new animation
	this.currentAnimation = this.getExplodeAnimation(this.direction);
	// start animation
	this.currentAnimation.start();
	// set new state
	this.state = State.Explode;
}

public void turn90(boolean clockwise) {
	// stop animation
	this.currentAnimation.stop();
	// set new animation
	this.currentAnimation = this.getTurn90Animation(this.direction, 
		clockwise);
	// start animation
	this.currentAnimation.start();
	// change direction
	this.direction = this.direction.rotate90(clockwise);
	// set new state
	this.state = State.Turning;
}

public void turn180() {
	// stop animation
	this.currentAnimation.stop();
	// set new animation
	this.currentAnimation = this.getTurn180Animation(this.direction);
	// start animation
	this.currentAnimation.start();
	// change direction
	this.direction = this.direction.rotate180();
	// set new state
	this.state = State.Turning;
}

public void step(Direction direction) {
	// stop animation
	this.currentAnimation.stop();
	// set new animation
	this.currentAnimation = this.getMoveAnimation(this.direction);
	// start animation
	this.currentAnimation.start();
	// get waypoint
	Point waypoint = this.getMoveWayPoint(this.direction);
	// add to list of waypoints
	this.wayPoints.add(waypoint);
	// set new state
	this.state = State.Moving;
}

public void bump(Direction direction) {
	// stop animation
	this.currentAnimation.stop();
	// set new animation
	this.currentAnimation = this.getMoveAnimation(this.direction);
	// start animation
	this.currentAnimation.start();
	// get waypoints
	List<Point> waypoint = this.getBumpWayPoints(this.direction);
	// add to list of waypoints
	this.wayPoints.addAll(waypoint);
	// set new state
	this.state = State.Moving;
}

public void update(long elapsedTime) {
	// update animation
	this.currentAnimation.update(elapsedTime);
	// perform different update depending on state type
	switch (this.state) {
		case Explode: // if exploding
			if (!this.currentAnimation.isRunning()) { // if done exploding
				this.currentAnimation.reset(); // reset animation
				// get dead animation object and init as current animation
				Animation deadAnim = this.getDeadAnimation(this.direction);
				this.currentAnimation = deadAnim;
				// start the new animation
				this.currentAnimation.start();
				// set new state as dead
				this.state = State.Dead;
			}
			// let player thread know
			synchronized(this) {
				notify();
			}
			break;
		case Turning: // if robot turning
			if (!this.currentAnimation.isRunning()) { // if done turning
				this.currentAnimation.reset(); // reset animation
				// get idle animation object and init as current animation
				Animation idleAnim = this.getIdleAnimation(this.direction);
				this.currentAnimation = idleAnim;
				// start the new animation
				this.currentAnimation.start();
				// set new state as idle
				this.state = State.Idle;
			}
			// let player thread know
			synchronized(this) {
				notify();
			}
			break;
		case Moving:
			if (!this.wayPoints.isEmpty()) {
				// split for readability; euclidean distance metric
				double x2 = this.wayPoints.get(0).getX();
				double y2 = this.wayPoints.get(0).getY();
				double xPart = Math.pow((this.x - x2), 2);
				double yPart = Math.pow((this.y - y2), 2);
				double distance = Math.sqrt(xPart + yPart);
				// if distance is leq than speed*time
				if (distance <= RobotSprite.SPEED * elapsedTime) {
					// get position of next waypoint (removes)
					Point newPosition = this.wayPoints.remove(0);
					// set robot position as waypoint
					this.x = (float)newPosition.getX();
					this.y = (float)newPosition.getY();
				} else {                        
					// if different x-axis, only move on x
					if (this.x < x2) {
						this.x += RobotSprite.SPEED*elapsedTime;
					} else if (this.x > x2) {
						this.x -= RobotSprite.SPEED*elapsedTime;
					// if different y-axis, only move on y
					} else if (this.y < y2) {
						this.y += RobotSprite.SPEED*elapsedTime;
					} else if (this.y > y2) {
						this.y -= RobotSprite.SPEED*elapsedTime;
					}
				}
			} else {
				// set as idle if no more waypoints
				this.currentAnimation.reset();
				Animation idleAnim = this.getIdleAnimation(this.direction);
				this.currentAnimation = idleAnim;
				this.currentAnimation.start();
				this.state = State.Idle;
			}
			// let player thread know
			synchronized(this) {
				notify();
			}
			break;
	}
}