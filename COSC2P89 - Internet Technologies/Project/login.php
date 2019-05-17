#!/usr/bin/php-cgi
<?php include("./common/common.php"); include("./common/top.php"); ?>
Please login. <br /> <br />
<form action="./login-submit.php" method="post">
	<fieldset>
		<legend>Username</legend>
			<input type="text" name="name" size="16" required="required"/> <br />
		<legend>Password</legend>
			<input type="password" name="pass" size="16" required="required"/> <br />
		<input type="submit" value="Login" />
	</fieldset>
</form>
Don't have an account? Why not <a href="register.php">register</a> for one?
<?php include("./common/bottom.php"); ?>