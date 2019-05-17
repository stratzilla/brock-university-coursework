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
				<?php include("./common/common.php"); ?>		
				<?php
					//Grabs username and password from previous form submit
					$usr = $_COOKIE["username"];
					$pas = mysql_escape_string($_POST["pass"]);
					$pat = mysql_escape_string($_POST["conf"]);
					//Confirms if they are identical
					if ($pas != $pat) {
				?>
						<div class="redirect">
							Your passwords did not match.
						</div>
				<?php
					}
					else {
						//Checks if user exists/password is correct
						$pash = md5($pas);
						$rows = $db->query("
							SELECT *
								FROM users
								WHERE username = '$usr'
								AND password = '$pash'
						");
						//Confirms
						if ($rows->rowCount()==1) {
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
									AND password = '$pash'
							");
				?>
							<div class="redirect">
								Account deleted.
							</div>
				<?php
							//Deletes the cookie
							setcookie("username","",time()-60000);
						}
						//If password was incorrect
						else {
				?>
							<div class="redirect">
								Incorrect password.
							</div>
				<?php
						}
					}
				?>
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