#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
Here is the sorted collection. <br /><br />
<?php
	//Grab what the user wants from previous page
	$user = $_SESSION["temp"];
	$sort = $_POST["cate"];
	$order = $_POST["order"];
	//If the sort criteria is condition, the query needs to be different
	if ($sort == 'condition'){
		$dbr = $db->query("
			SELECT *
				FROM books b
				JOIN users u
				ON b.userid = u.ident
				WHERE u.username = '$user'
				-- Because it would sort it alphabetically and not by grade
				-- Use a case complex
				ORDER BY CASE b.condition
					WHEN 'M' THEN 1
					WHEN 'NM' THEN 2
					WHEN 'E' THEN 3
					WHEN 'G' THEN 4
					WHEN 'P' THEN 5
			END $order
		");
	}
	else{
	//If sorted by other criteria
		$dbr = $db->query("
			SELECT *
				FROM books b
				JOIN users u
				ON b.userid = u.ident
				WHERE u.username = '$user'
				ORDER BY b.$sort $order
		");
	}
	//External table
	include("./common/table.php");
?>
<?php include("./common/bottom.php"); ?>