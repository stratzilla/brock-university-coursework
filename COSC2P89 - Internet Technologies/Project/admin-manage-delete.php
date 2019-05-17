#!/usr/bin/php-cgi
<!DOCTYPE html>

<html>
	<head>
		<title>BookCollector - Your One Stop Source for Organization</title>
		<meta charset="utf-8" />
		<link href="./common/books.css" type="text/css" rel="stylesheet" />
		<link href="./common/images/book.png" type="image/png" rel="icon" />
		<!-- Redirects to admin page -->
		<meta http-equiv="refresh" content="3;url=./admin-manage.php" />
	</head>
	<body>
	<?php
		//Checks if user is logged in, displays below is true
		if (isset($_COOKIE["username"])){
	?>
			<?php include("./common/common.php"); ?>
			
			<?php
				//Grabs username to be deleted from GET
				$usr = $_GET["username"];
				//Deletes book collection
				$rows = $db->query("
					DELETE b.*
						FROM books b
						JOIN users u
						ON b.userid = u.ident
						WHERE u.username = '$usr'
				");
				//Deletes account from users.sql
				$rows = $db->query("
					DELETE
						FROM users
						WHERE username = '$usr'
				");
			?>		
			<div class="redirect">
				Account deleted. <br />
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