#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
<?php
	//Checks if user is logged in
	if(isset($_COOKIE["username"])){
		$user = $_COOKIE["username"];
?>
		Please enter your password below:<br />
		<form action="./unregister-submit.php" method="post">
			<fieldset>
				<legend>Password</legend>
					<input type="password" name="pass" size="16" required="required"/> <br />
				<legend>Confirm Password</legend>
					<input type="password" name="conf" size="16" required="required"/> <br />
				<input onclick="return jspopupunreg();" type="submit" value="Delete Account" />
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