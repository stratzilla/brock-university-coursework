<!DOCTYPE html>

<?php session_start(); ?>
<html>
	<head>
		<title>BookCollector - Your One Stop Source for Organization</title>
		<meta charset="utf-8" />
		<link href="./common/books.css" type="text/css" rel="stylesheet" />
		<link href="./common/images/book.png" type="image/png" rel="icon" />
		<!-- All pertinent pages include top.html, so by extension
			they also include this javascript file -->
		<script src="./common/javasfuncts.js"></script>
	</head>
	<!--timer.js-->
	<body onload="starttime();">
		<div class="top">
			<div id="title">
				<a href="./home.php">BookCollector</a>
			</div>
			Your One Stop Source for Organization
			<div id="login">
				<?php
					//Check if user is logged in or not
					if (!isset($_COOKIE["username"])) {
						echo "Welcome, Guest!";
						//Give the option to login if guest
				?>
						(<a href="./login.php">login</a> | <a href="./register.php">register</a>)
				<?php
					}
					else {
						echo "Welcome, ".htmlspecialchars($_COOKIE["username"])."!";
						//Give the option to manage if logged in
				?>
						(<a href="./logout.php">logout</a> | <a href="./administrative.php">control panel</a>)
				<?php
					}
				?>
			</div>
		</div>
		<div class="main">