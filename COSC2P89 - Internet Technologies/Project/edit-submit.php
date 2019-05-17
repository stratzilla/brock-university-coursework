#!/usr/bin/php-cgi
<!DOCTYPE html>

<?php session_start(); ?>
<html>
	<head>
		<title>BookCollector - Your One Stop Source for Organization</title>
		<meta charset="utf-8" />
		<link href="./common/books.css" type="text/css" rel="stylesheet" />
		<link href="./common/images/book.png" type="image/png" rel="icon" />
		<!-- Redirects to collection page -->
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
					$auth = mysql_escape_string($_POST["auth"]);
					$cond = $_POST["cond"];
					$gen = $_POST["gen"];
					$pub = mysql_escape_string($_POST["pub"]);
					//Grab session data
					$usval = $_SESSION["tempuserid"];
					$bkval = $_SESSION["tempbookid"];
					//Update the table row
					$rows = $db->query("
						UPDATE books b
							SET b.title = '$title',
								b.year = '$year',
								b.author = '$auth',
								b.condition = '$cond',
								b.genre = '$gen',
								b.publisher = '$pub'
							WHERE b.userid = '$usval'
								AND b.bookid = '$bkval'
					");
				?>
				<div class="redirect">
					Book entry has been updated.
				</div>
		<?php
			}	
			//Displays this if the user is not logged in
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