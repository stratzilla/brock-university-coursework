#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
<?php
	//Checks if user is logged in, displays below is true
	if (isset($_COOKIE["username"])){
		//Grab credentials from cookie
		$username = $_COOKIE["username"];
		$password = $_COOKIE["password"];
?>

		Here you may change your password.<br />
		
		<form action="./change-submit.php" method="post">
			<fieldset>
				<legend>Old Password</legend>
					<input type="password" name="oldpass" size="16" required="required"/> <br />
				<legend>New Password</legend>
					<input type="password" name="newpass" size="16" required="required"/> <br />
				<legend>Confirm Password</legend>
					<input type="password" name="passconf" size="16" required="required"/> <br />
				<input type="submit" value="Change Password" />
			</fieldset>
		</form>

<?php
	}
	//Display this if the user is not logged in
	else {
?>
		<p>You are not currently logged in.</p><br />
<?php
	}
?>

<?php include("./common/bottom.php"); ?>