#!/usr/bin/php-cgi
<!DOCTYPE html>

<html>
	<head>
		<title>BookCollector - Your One Stop Source for Organization</title>
		<meta charset="utf-8" />
		<link href="./common/books.css" type="text/css" rel="stylesheet" />
		<link href="./common/images/book.png" type="image/png" rel="icon" />
		<!-- Redirects to user control panel -->
		<meta http-equiv="refresh" content="3;url=./administrative.php" />
	</head>
	<body>
		<?php
			//Checks if user is logged in, displays below is true
			if (isset($_COOKIE["username"])){
		?>
				<?php include("./common/common.php"); ?>
				<?php
					//Confirming things
					$usr = $_COOKIE["username"];
					$oldpassword = mysql_escape_string($_POST["oldpass"]);
					$oldpassconf = mysql_escape_string($_COOKIE["password"]);
					$newpassword = mysql_escape_string($_POST["newpass"]);
					$confpassword = mysql_escape_string($_POST["passconf"]);
					if (md5($oldpassword) == $oldpassconf){
						if ($newpassword == $confpassword) {
							$submitpass = md5($newpassword);
							$rows = $db->query("
								UPDATE users
									SET password = '$submitpass'
									WHERE username = '$usr'
							");
				?>
						<div class="redirect">
							You have successfully changed your password.
						</div>
				<?php
						}
						else {
				?>
							<div class="redirect">
								Your new passwords do not match.
							</div>
				<?php
						}
					}
					else {
				?>
					<div class="redirect">
						You seem to have mistyped your old password.
					</div>
				<?php
					}
				?>
		<?php
			}
			//Display this if the user is not logged in
			else {
		?>
				<p>You are not currently logged in.</p><br />
		<?php
			}
		?>
	</body>
</html>