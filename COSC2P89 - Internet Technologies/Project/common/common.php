<?php
	$expire = time()+60*60*24*14; //Cookies expire in two weeks
	$i = 1; //Used in table production
	$SERVER   = "localhost";
	$USERNAME = "";
	$PASSWORD = "";
	$DATABASE = "";
	$db = new PDO("mysql:dbname={$DATABASE}; host={$SERVER}", $USERNAME, $PASSWORD);
	$db->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
?>
