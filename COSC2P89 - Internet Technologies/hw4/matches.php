#!/usr/bin/php-cgi

<?php include("./top.html"); ?>
	<form action="./matches-submit.php" method="get">
		<fieldset>
			<legend>Returning User:</legend>
			<strong>Name:</strong>
				<input type="text" name="name" size="16" /> <br/>
			<input type="submit" value="View My Matches" />
		</fieldset>
	</form><br/>
<?php include("./bottom.html"); ?>