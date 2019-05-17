#!/usr/bin/php-cgi
<!DOCTYPE html>

<html>
	<head>
		<title>BookCollector - Your One Stop Source for Organization</title>
		<meta charset="utf-8" />
		<link href="./common/books.css" type="text/css" rel="stylesheet" />
		<link href="./common/images/book.png" type="image/png" rel="icon" />
		<!-- Redirects to homepage -->
		<meta http-equiv="refresh" content="3;url=./home.php" />
	</head>
	<body>
		<?php include("./common/common.php"); ?>
		<?php
			//Grabs username and password from previous form submit
			$us = mysql_escape_string($_POST["name"]);
			$temppa = mysql_escape_string($_POST["pass"]);
			$pa = md5($temppa);
			$rows = $db->query("
				SELECT *
					FROM users
					WHERE username = '$us'
					AND password = '$pa'
			");		
			//Checks if user exists
			if ($rows->rowCount()==1) {
		?>
				<div class="redirect">
					You have successfully logged in.
				</div>
		<?php
				//Creates cookie from login details if credentials are correct
				setcookie("username", $us, $expire);
				setcookie("password", $pa, $expire);
			}
			else {
		?>
				<div class="redirect">
					Invaliad login.
				</div>
		<?php
			}
		?>
	</body>
</html>