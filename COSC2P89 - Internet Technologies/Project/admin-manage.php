#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
<?php
	//Checks if user is logged in, displays below is true
	if (isset($_COOKIE["username"])){
		//Grab credentials from cookie
		$username = $_COOKIE["username"];
		$password = $_COOKIE["password"];
		//Checks if user is admin or not
		$rows = $db->query("
			SELECT *
				FROM users
				WHERE username = '$username'
				AND password = '$password'
				AND admin = 'yes'
		");	
		//Display this if the user is not an administrator
		if ($rows->rowCount()==1){
?>
			Welcome to your admin control panel.<br />
			Here you may delete users from the database, or make them an admin.<br /><br />
			<strong>List of Users</strong>
				<table>
					<tr>
						<th>#</th>
						<th>Username</th>
						<th>Total Books</th>
						<th>Admin Status</th>
						<th></th>
						<th></th>
					</tr>
					<?php
						//Prepares for foreach()
						$dbr = $db->query("
							SELECT *
								FROM users
						");
						foreach ($dbr as $var) {
							$view = "./admin-manage-delete.php?username=".htmlspecialchars($var["username"])."";
							$adm = "./admin-manage-rank.php?username=".htmlspecialchars($var["username"])."";
							$nom = $var["username"];
							//Counts total books a user has
							$bc = $db->query("
								SELECT *
									FROM users u
									JOIN books b
									ON b.userid = u.ident
									WHERE u.username = '$nom'
							");
							$count = $bc->rowCount();
					?>
							<tr>
								<td><?php echo $i; ?></td>
								<td><?php echo htmlspecialchars($var["username"]); ?></td>
								<td><?php echo $count; ?></td>
								<td><?php echo htmlspecialchars($var["admin"]); ?></td>
								<td>
									<a href="<?php echo $view; ?>" onclick="return jspopupdel();">Delete</a>
								</td>
								<td>
									<a href="<?php echo $adm; ?>" onclick="return jspopupadm();">Make Admin</a>
								</td>
							</tr>
					<?php
						$i++;
						}
					?>
				</table> <br />
<?php
		}
		else{
?>
			<p>You do not have the credentials to access this page.</p><br />
<?php
		}
	}	
	//Displays this if the user is not logged in
	else {
?>
		<div class="redirect">
			You are not currently logged in.
		</div>
<?php
	}
?>
<?php include("./common/bottom.php"); ?>