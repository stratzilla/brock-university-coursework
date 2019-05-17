<?php
	//prepare
	$grabdetails = $db->prepare("
		SELECT *
			FROM table
			WHERE column = :var
	");
	//execute
	$grabdetails->execute(array(
		':var' => $_POST["value"]
	));
	//print_r $grabdetails reveals many columns
	echo $grabdetails->fetchColumn(); //grab one column
	echo $grabdetails->fetchColumn(1); //not possible
	//you need to execute multiple times to access multiple columns
	$grabdetails->execute(array(
		':var' => $_POST["value"]
	));
	echo $grabdetails->fetchColumn();	
		$grabdetails->execute(array(
		':var' => $_POST["value"]
	));
	echo $grabdetails->fetchColumn(1);
		$grabdetails->execute(array(
		':var' => $_POST["value"]
	));
	echo $grabdetails->fetchColumn(2);
	...
	...
	
	//or
	
	//plants a "seed" for columns to be accessed
	foreach($grabdetails as $grab) {}
	//allows displaying of information
	echo $grab["column1"];
	echo $grab["column2"];
	...
	...
?>