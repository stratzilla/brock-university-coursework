
<?php
	/* I wasn't sure how to connect to Sandcastle remotely,
		so I exported the databases to my local server to test
		my code with; I've inputted what I think is correct below
		despite getting timeout errors. You may need to adjust this
		to check my code because as of right now, it doesn't work.
		It did, however, work on my local machine hosting it on my
		local server; the code works, I just don't know how to test
		it on Brock's machines*/
	$SERVER   = "";
	$USERNAME = "";
	$PASSWORD = "";
	$DATABASE = "imdb";
	$db = new PDO("mysql:dbname={$DATABASE}; host={$SERVER}", $USERNAME, $PASSWORD);
	$db->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
	$i = 1; //Used later
	//Grab info from index.php form
	$actor_first = $_GET["firstname"];
	$actor_last = $_GET["lastname"];
	//Concatenate into one var to save space later
	$actor = $actor_first." ".$actor_last;
?>
