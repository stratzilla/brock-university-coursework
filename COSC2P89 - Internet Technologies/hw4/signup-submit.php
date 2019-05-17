#!/usr/bin/php-cgi

<?php include("./top.html"); ?>
	<?php
		//Turning form contents into variables
		$name = $_POST["name"];
		$gen = $_POST["gender"];
		$age = $_POST["age"];
		$pers = $_POST["pers"];
		$os = $_POST["OS"];
		$agemin = $_POST["min"];
		$agemax = $_POST["max"];
		//Placing variables into string
		$input_string = "$name,$gen,$age,$pers,$os,$agemin,$agemax\r\n";
		//Append the string to the .txt file
		file_put_contents("./singles.txt",$input_string,FILE_APPEND);
	?>
	<p>
		<strong>Thank you!</strong>
	</p>
	<p>
		Welcome to Nerdluv, <?php echo $name ?>!
	</p>
	<p>
		Now <a href="./matches.php">log in to see your matches!</a>
	</p>
<?php include("./bottom.html"); ?>