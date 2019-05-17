#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
<?php
	//Checks if user is logged in
	if(isset($_COOKIE["username"])){
		$user = $_COOKIE["username"];
		//Displays the below if logged in
?>
		Here is your collection. <br /><br />
		<?php
			$dbr = $db->query("
				SELECT *
					FROM users u
					JOIN books b
					ON b.userid = u.ident
					WHERE u.username = '$user'
			");
		?>
		<table>
			<tr>
				<th class="num">#</th>
				<th class="bk">Book Title</th>
				<th class="yr">Year Published</th>
				<th class="au">Author</th>
				<th class="cd">Condition of Book</th>
				<th class="ge">Genre</th>
				<th class="pb">Publisher</th>
				<th class="del"></th>
				<th class="del"></th>
			</tr>
			<?php
				foreach ($dbr as $var) {
				$edilink = "manage-edi.php?userid=".$var["userid"]."&bookid=".$var["bookid"];
				$dellink = "manage-del.php?userid=".$var["userid"]."&bookid=".$var["bookid"];
			?>
					<tr>
						<td><?php echo $i; ?></td>
						<td><?php echo htmlspecialchars($var["title"]); ?></td>
						<td><?php echo htmlspecialchars($var["year"]); ?></td>
						<td><?php echo htmlspecialchars($var["author"]); ?></td>
						<td><?php echo htmlspecialchars($var["condition"]); ?></td>
						<td><?php echo htmlspecialchars($var["genre"]); ?></td>
						<td><?php echo htmlspecialchars($var["publisher"]); ?></td>
						<td><a href="<?php echo $edilink; ?>">Edit</a></td>
						<td><a href="<?php echo $dellink; ?>" onclick="return jspopuprem();">Delete</a></td>
					</tr>
			<?php
				$i++;
				}
			?>
		</table><br />
		You can add new entries with the form below.<br /><br />
		<form action="./collection-submit.php" method="post">
			<table>
				<tr>
					<th class="bk">Book Title</th>
					<th class="yr">Year Published</th>
					<th class="au">Author</th>
					<th class="cd">Condition of Book</th>
					<th class="ge">Genre</th>
					<th class="pb">Publisher</th>
				</tr>
				<tr>
					<td><input type="text" name="title" maxlength="50" required="required" /></td>
					<td><input type="text" name="year" maxlength="4" required="required" /></td>
					<td><input type="text" name="author" maxlength="25" required="required" /></td>
					<td>
						<select name="condition">
							<option selected="selected">M</option>
							<option>NM</option>
							<option>E</option>
							<option>G</option>
							<option>P</option>
						</select>
					</td>
					<td>
						<select name="genre">
							<option>Horror</option>
							<option>Drama</option>
							<option>Action</option>
							<option>Romance</option>
							<option>Fantasy</option>
							<option>Science Fiction</option>
							<option>Textbook</option>
							<option>Crime</option>
							<option>Comedy</option>
							<option>Childrens</option>
							<option>Poetry</option>
							<option>Compilation</option>
							<option>Thriller</option>
						</select>
					</td>
					<td><input type="text" name="publisher" maxlength="25" required="required"/></td>
				</tr>
			</table>
			<input type="submit" value="Create" />
		</form>
		<br />
		Condition Guide: M = Mint, NM = Near Mint, E = Excellent, G = Good, P = Poor
<?php
	}
	//Display this if the user is not logged in
	else {
?>
		<p>You are not currently logged in.</p><br />
<?php
	}
?>
<?php include("./common/bottom.php"); ?>
