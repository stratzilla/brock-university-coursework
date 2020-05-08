private static final RoboRaceSoundManager INSTANCE = 
	new RoboRaceSoundManager();

private static Sound bumpSound;
private static Sound explodeSound;
private static Sound fanfareSound;
private static Sound drivshortSound;
private static Sound drivlongSound;
private static Sound pusherSound;
private static Sound crusherSound;

// these are set to -1 because it might be possible to play a sound
// with no delay between. We want to catch that. It's at least assured
// that it's impossible to play one sound after the other but have
// time signatures be negative (we don't have time travel)
private long bumpTimer = -1;
private long explodeTimer = -1;
private long fanfareTimer = -1;
private long drivshortTimer = -1;
private long drivlongTimer = -1;
private long pusherTimer = -1;
private long crusherTimer = -1;

private RoboRaceSoundManager() {
	super(new AudioFormat(8000,8,1,false,false),4);
	super.setSoundPath("./Sounds&Midi/");
	// load every sound into memory
	bumpSound = this.getSound("bump.wav");
	explodeSound = this.getSound("explosion.wav");
	fanfareSound = this.getSound("fanfare.wav");
	drivshortSound = this.getSound("drivingShort.wav");
	drivlongSound = this.getSound("drivingLong.wav");
	pusherSound = this.getSound("pusher.wav");
	crusherSound = this.getSound("crusher.wav");
}

public static synchronized RoboRaceSoundManager getInstance() {
	// return instance of the singleton
	return INSTANCE;
}

public static void playBump() {
	// if timer not set
	if (INSTANCE.bumpTimer == -1) {
		// set timer to current time
		INSTANCE.bumpTimer = System.currentTimeMillis();
		// play sound
		INSTANCE.play(bumpSound);
	} else { // if timer is set
		// if there's 400ms or more between last play and now
		if (System.currentTimeMillis() - INSTANCE.bumpTimer >= 400) {
			// reset timer to initial condition
			INSTANCE.bumpTimer = -1;
			// and play the sound
			INSTANCE.play(bumpSound);
		}
	}        
}

public static void playExplode() {
	// if timer not set
	if (INSTANCE.explodeTimer == -1) {
		// set timer to current time
		INSTANCE.explodeTimer = System.currentTimeMillis();
		// play sound
		INSTANCE.play(explodeSound);
	} else { // if timer is set
		// if there's 400ms or more between last play and now
		if (System.currentTimeMillis() - INSTANCE.explodeTimer >= 400) {
			// reset timer to initial condition
			INSTANCE.explodeTimer = -1;
			// and play the sound
			INSTANCE.play(explodeSound);
		}
	}     
}

public static void playFanfare() {
	// if timer not set
	if (INSTANCE.fanfareTimer == -1) {
		// set timer to current time
		INSTANCE.fanfareTimer = System.currentTimeMillis();
		// play sound
		INSTANCE.play(fanfareSound);
	} else { // if timer is set
		// if there's 400ms or more between last play and now
		if (System.currentTimeMillis() - INSTANCE.fanfareTimer >= 400) {
			// reset timer to initial condition
			INSTANCE.fanfareTimer = -1;
			// and play the sound
			INSTANCE.play(fanfareSound);
		}
	}     
}

public static void playDrivingShort() {
	// if timer not set
	if (INSTANCE.drivshortTimer == -1) {
		// set timer to current time
		INSTANCE.drivshortTimer = System.currentTimeMillis();
		// play sound
		INSTANCE.play(drivshortSound);
	} else { // if timer is set
		// if there's 400ms or more between last play and now
		if (System.currentTimeMillis() - INSTANCE.drivshortTimer >= 400) {
			// reset timer to initial condition
			INSTANCE.drivshortTimer = -1;
			// and play the sound
			INSTANCE.play(drivshortSound);
		}
	}     
}

public static void playDrivingLong() {
	// if timer not set
	if (INSTANCE.drivlongTimer == -1) {
		// set timer to current time
		INSTANCE.drivlongTimer = System.currentTimeMillis();
		// play sound
		INSTANCE.play(drivlongSound);
	} else { // if timer is set
		// if there's 400ms or more between last play and now
		if (System.currentTimeMillis() - INSTANCE.drivlongTimer >= 400) {
			// reset timer to initial condition
			INSTANCE.drivlongTimer = -1;
			// and play the sound
			INSTANCE.play(drivlongSound);
		}
	}     
}

public static void playPusher() {
	// if timer not set
	if (INSTANCE.pusherTimer == -1) {
		// set timer to current time
		INSTANCE.pusherTimer = System.currentTimeMillis();
		// play sound
		INSTANCE.play(pusherSound);
	} else { // if timer is set
		// if there's 400ms or more between last play and now
		if (System.currentTimeMillis() - INSTANCE.pusherTimer >= 400) {
			// reset timer to initial condition
			INSTANCE.pusherTimer = -1;
			// and play the sound
			INSTANCE.play(pusherSound);
		}
	}     
}

public static void playCrusher() {
	// if timer not set
	if (INSTANCE.crusherTimer == -1) {
		// set timer to current time
		INSTANCE.crusherTimer = System.currentTimeMillis();
		// play sound
		INSTANCE.play(crusherSound);
	} else { // if timer is set
		// if there's 400ms or more between last play and now
		if (System.currentTimeMillis() - INSTANCE.crusherTimer >= 400) {
			// reset timer to initial condition
			INSTANCE.crusherTimer = -1;
			// and play the sound
			INSTANCE.play(crusherSound);
		}
	}     
}