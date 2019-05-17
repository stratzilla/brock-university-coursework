#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
<?php
	//Checks if user is logged in, displays below is true
	if (isset($_COOKIE["username"])){
		//Grab credentials from cookie
		$username = $_COOKIE["username"];
		$password = $_COOKIE["password"];
?>
		<a href="./change.php">Change password</a> <br />
		<a href="./collection.php">Manage your collection</a> <br />
		<a href="./unregister.php">Delete your account</a> <br />
<?php
		$rows = $db->query("
			SELECT *
				FROM users
				WHERE username = '$username'
				AND password = '$password'
				AND admin = 'yes'
		");		
		//Checks if user is admin or not
		if ($rows->rowCount()==1) {
		//Displays this additional option if so
?>
			<a href="./admin-manage.php">Manage other users</a> <br />
<?php
		}
	}
	//Display this if the user is not logged in
	else {
?>
		<p>You are not currently logged in.</p><br />
<?php
	}
?>
<?php include("./common/bottom.php"); ?>