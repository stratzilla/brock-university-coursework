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
			//Grabs submitted data
			$username = mysql_escape_string($_POST["name"]);
			$password = mysql_escape_string($_POST["pass"]);
			$confirmpass = mysql_escape_string($_POST["conf"]);
			//Confirms the two passwords match
			if ($password == $confirmpass) {
				$passhash = md5($password);
				$rows = $db->query("
					INSERT INTO users
					VALUES ('$username', '$passhash', '', 'no');"
				);
				//Reneges the need to login after registration by pre-emptively creating cookie
				setcookie("username", $username, $expire);
				setcookie("password", $passhash, $expire);
		?>
				<div class="redirect">
					You have successfully registered.
				</div>
		<?php
			}
			else {
				//If passwords do not match, redirects to home.php
		?>
				<div class="redirect">
					Your passwords do not match.
				</div>
		<?php
			}
		?>
	</body>
</html>