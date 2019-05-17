#!/usr/bin/php-cgi

<?php include("./top.html"); ?>
	<?php
		//Initialize the contents of the url (.php?name=$$$) into a variable $name
		$inputname = $_GET["name"];
		//Finds how many lines in the singles.txt file
		$linecount = count(file("./singles.txt"));
		//Initialize each line into an array
		$matches = file("./singles.txt");
		$matcharray = array();
		//Create multidimensional array for easier indexing
		foreach($matches as $line) {
			array_push($matcharray,explode(",",$line));
		}
		//Retrieves data on the inputted name
		for ($i = 0; $i < $linecount; $i++) {
			if ($matcharray[$i][0] == $inputname) {
				//Grab details on user
				$username = $matcharray[$i][0];
				$usergender = $matcharray[$i][1];
				$userage = $matcharray[$i][2];
				$userpers = $matcharray[$i][3];
				$useros = $matcharray[$i][4];
				$usermin = $matcharray[$i][5];
				$usermax = $matcharray[$i][6];
			}
		}
	?>
	<strong>Matches for <?php echo $username ?></strong>
	<?php
		//I apologize if my if condition is ugly as hell
		for ($j = 0; $j < $linecount; $j++) {
			if (($matcharray[$j][1] != $usergender) &&
				($matcharray[$j][2] >= $usermin) &&
				($matcharray[$j][2] <= $usermax) &&
				//I can't figure out how to split $matcharray[][3] even further
				//I initially thought I could re-use the array_push I did earlier
				//but I only want to make a tri-dimensional array of one array element
				//not a tri-dimensional array of all elements
				//($matcharray[$j][3] == $userpers) &&
				($matcharray[$j][4] == $useros) &&
				($matcharray[$j][5] <= $userage) &&
				($matcharray[$j][6] >= $userage)) {
	?>
				<div class="match">
					<!--There must be something wrong with the .css
					because the name of the match in the <p>
					doesn't appear properly -->
					<p><?php echo $matcharray[$j][0]; ?></p>
					<img src="http://www.cosc.brocku.ca/Offerings/2P89/Images/user.jpg" />
					<ul>
						<li><strong>gender:</strong><?php echo $matcharray[$j][1]; ?></li>
						<li><strong>age:</strong><?php echo $matcharray[$j][2]; ?></li>
						<li><strong>type:</strong><?php echo $matcharray[$j][3]; ?></li>
						<li><strong>OS:</strong><?php echo $matcharray[$j][4]; ?></li>
					</ul>
				</div>
	<?php
			}
		}
	?>
<?php include("./bottom.html"); ?>