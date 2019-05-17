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
		<?php
			//Checks if user is logged in, displays below is true
			if (isset($_COOKIE["username"])){
		?>
				<?php
					//Deletes the cookie
					setcookie("username","",time()-60000);
				?>
				<div class="redirect">
					You have successfully logged out.
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
