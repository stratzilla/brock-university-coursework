#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
Here is the sorted collection. <br /><br />
<?php
	//Grab what the user wants from previous page
	$user = $_SESSION["temp"];
	$genre = $_POST["genre"];
	//Queries, etc
		$dbr = $db->query("
			SELECT *
				FROM books b
				JOIN users u
				ON b.userid = u.ident
				WHERE u.username = '$user'
				AND b.genre = '$genre'
		");
	include("./common/table.php");
?>
<?php include("./common/bottom.php"); ?>