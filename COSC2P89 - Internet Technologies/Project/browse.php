#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
Please select a user whose collection you would like to view. <br />
<div class="display">
	<?php
		//Pulls up list of users in descending order
		$rows = $db->query("
			SELECT *
				FROM users
				ORDER BY username
		");
		//Counts number of users registered*/
		$usercount = $rows->rowCount();
		//Prints the users in a table below as well as total submissions
	?>
	<table>
		<tr>
			<th>#</th>
			<th>Username</th>
			<th>Total Books</th>
			<th></th>
		</tr>
		<?php
			foreach ($rows as $row) {
				$nom = $row["username"];
				//This is really janky, but it works
				$view = "./query.php?username=".$nom."";
				$dbr = $db->query("
					SELECT *
						FROM users u
						JOIN books b
						ON b.userid = u.ident
						WHERE u.username = '$nom'
				");
				//Counts number of books users have submitted
				$bc = $dbr->rowCount();
		?>
				<tr>
					<td><?php echo $i; ?></td>
					<td><?php echo htmlspecialchars($nom); ?></td>
					<td>
						<?php
							//To display book or books
							if ($bc == 1){
								echo $bc." book";
							}
							else {
								echo $bc." books";
							}
						?>
					</td>
					<td><a href="<?php echo $view; ?> ">View</a></td>
				</tr>
		<?php
			$i++;
			}
		?>
	</table>
</div>
Displaying <?php echo $usercount; ?> users.
<?php include("./common/bottom.php"); ?>