#!/usr/bin/php-cgi
<!DOCTYPE html>

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
					//Grab entry from collection.php
					$usval = mysql_escape_string($_GET["userid"]);
					$bkval = $_GET["bookid"];
					//Deletes book entry as defined by GET
					$dbr = $db->query("
						DELETE b.*
							FROM books b
							JOIN users u
							ON b.userid = u.ident
							WHERE b.bookid = '$bkval'
							AND u.ident = '$usval'
					");
				?>
				<div class="redirect">
					Book entry deleted.
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
