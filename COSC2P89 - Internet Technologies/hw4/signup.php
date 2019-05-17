#!/usr/bin/php-cgi

<?php include("./top.html"); ?>
	<form action="./signup-submit.php" method="post">
		<fieldset>
			<legend>New User Signup:</legend>
			<strong>Name:</strong> 
				<input type="text" name="name" size="16" /> <br/>
			<strong>Gender:</strong> 
				<input type="radio" name="gender" value="M" />Male
				<input type="radio" name="gender" value="F" checked="checked" />Female <br/>
			<strong>Age:</strong>
				<input type="text" name="age" size="6" maxlength="2" /> <br/>
			<strong>Personality type:</strong>
				<input type="text" name="pers" size="6" maxlength="4" /> 
				(<a href="http://www.humanmetrics.com/cgi-win/JTypes2.asp">Don't know your type?</a>) <br/>
			<strong>Favorite OS:</strong>
				<select name="OS">
					<option value="Windows">Windows</option>
					<option value="Linux">Linux</option>
					<option value="Mac OS X">Mac OS X</option>
				</select> <br/>
			<strong>Seeking age:</strong>
				<input type="text" name="min" size="6" placeholder="min" maxlength="2" /> to 
				<input type="text" name="max" size="6" placeholder="max" maxlength="2" /> <br/>
			<input type="submit" value="Sign Up" />
		</fieldset>	
	</form><br/>
<?php include("./bottom.html"); ?>