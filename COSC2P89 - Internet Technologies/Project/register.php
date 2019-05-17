#!/usr/bin/php-cgi
<?php include("./common/top.php"); ?>
Please enter your desired username and password below. <br /> <br />
<form action="./register-submit.php" method="post">
	<fieldset>
		<legend>Username</legend>
			<input type="text" name="name" size="16" required="required"/> <br />
		<legend>Password</legend>
			<input type="password" name="pass" size="16" required="required"/> <br />
		<legend>Confirm Password</legend>
			<input type="password" name="conf" size="16" required="required"/> <br />
		<input type="submit" value="Register" />
	</fieldset>
</form>
<?php include("./common/bottom.php"); ?>