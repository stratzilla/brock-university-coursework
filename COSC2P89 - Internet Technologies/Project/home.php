#!/usr/bin/php-cgi
<?php include("./common/top.php"); ?>
<?php
	if (isset($_COOKIE["username"])) {
?>
		<p>Welcome back, <?php echo htmlspecialchars($_COOKIE["username"]); ?>.</p>
		<p>You can edit your collection by clicking the admin link to the top-right, or browse other collections below</p>
<?php
	}
	else{
?>
		<p>BookCollector is a simple to use website designed to organize your book collection.</p>
		<p>Feel free to signup or login if your have an account.</p>
		<p>You can also browse current collections by clicking the link below.</p>
<?php
	}
?>
<div class="browse">
	<a href="./browse.php">Click here to browse collections</a>
</div>
<?php include("./common/bottom.php"); ?>