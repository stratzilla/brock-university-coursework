#!/usr/bin/php-cgi
<!DOCTYPE html>

<html>
	<head>
		<title>BookCollector - Your One Stop Source for Organization</title>
		<meta charset="utf-8" />
		<link href="./common/books.css" type="text/css" rel="stylesheet" />
		<link href="./common/images/book.png" type="image/png" rel="icon" />
		<!-- Redirects to collection -->
		<meta http-equiv="refresh" content="3;url=./collection.php" />
	</head>
	<body>
		<?php
			//Checks if user is logged in, displays below is true
			if (isset($_COOKIE["username"])){
		?>
				<?php include("./common/common.php"); ?>
				<?php
					//Grab user from cookie
					$user = $_COOKIE["username"];
					//Grab posted vars from collection.php
					//SQL escape all strings
					$title = mysql_escape_string($_POST["title"]);
					$year = mysql_escape_string($_POST["year"]);
					$auth = mysql_escape_string($_POST["author"]);
					$cond = mysql_escape_string($_POST["condition"]);
					$gen = mysql_escape_string($_POST["genre"]);
					$pub = mysql_escape_string($_POST["publisher"]);
					//Grab user id from users.sql
					$quser = $db->query("
						SELECT ident
							FROM users
							WHERE username = '$user'
					");
					//Initialize .sql query under colum ident to PHP var
					foreach ($quser as $c){
						$usr = $c["ident"];
					}			
					//Append to table
					$rows = $db->query("
						INSERT INTO books
							VALUES ('$title', '$year', '$auth', '$cond', '$gen', '$pub', '$usr', '');"
					);
				?>
				<div class="redirect">
					Book entry added.
				</div>
		<?php
			}
			//Display this if the user is not logged in
			else {
		?>
				<div class="redirect">
					You are not currently logged in.
				</div>
		<?php
			}
		?>			
	</body>
</html>