#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
<?php
	//Checks if user is logged in, displays below is true
	if (isset($_COOKIE["username"])){
		//Grab credentials from cookie
		$username = $_COOKIE["username"];
		//Grab GET'd vars
		$book = mysql_escape_string($_GET["bookid"]);
		$user = mysql_escape_string($_GET["userid"]);
		//Session updated for editing row
		$_SESSION["tempuserid"] = $user;
		$_SESSION["tempbookid"] = $book;
		//Deletes book entry as defined by GET
		$dbr = $db->query("
			SELECT *
				FROM books b
				JOIN users u
				ON b.userid = u.ident
				WHERE b.bookid = '$book'
				AND u.ident = '$user'
		");
?>
		Please enter the new values for this entry.<br /><br />
		<form action="./edit-submit.php" method="post">
			<table>
				<tr>
					<th class="bk">Book Title</th>
					<th class="yr">Year Published</th>
					<th class="au">Author</th>
					<th class="cd">Condition of Book</th>
					<th class="ge">Genre</th>
					<th class="pb">Publisher</th>
				</tr>
				<?php
					//Used a little later
					$condnarr = array('M', 'NM', 'E', 'G', 'P');
					$genarr = array(
								'Horror',
								'Drama',
								'Action',
								'Romance',
								'Fantasy',
								'Science Fiction',
								'Textbook',
								'Crime',
								'Comedy',
								'Childrens',
								'Poetry',
								'Compilation',
								'Thriller'
									);
					foreach($dbr as $var) {
					//Sorry it's kind of ugly below
					//I didn't want lines too long
				?>
						<tr>
							<td>
								<input type="text" 
									name="title"
									maxlength="50" 
									value="<?php echo $var["title"]; ?>" 
									required="required" />
							</td>
							<td>
								<input type="text"
									name="year"
									maxlength="4"
									value="<?php echo $var["year"]; ?>"
									required="required" />
							</td>
							<td>
								<input type="text"
									name="auth"
									maxlength="25"
									value="<?php echo $var["author"]; ?>"
									required="required" />
							</td>
							<td>
								<!--this is a huge mess, sorry-->
								<select name="cond">
									<?php
										foreach($condnarr as $vd){
									?>
											<option value="<?php echo $vd; ?>"
												<?php echo ($var["condition"] == $vd ? ' selected = "selected"' : ''); ?>>
												<?php echo $vd; ?>
											</option>
									<?php
										}
									?>
								</select>
							</td>
							<td>
								<!--also very messy-->
								<select name="gen">
									<?php
										foreach($genarr as $ga){
									?>
											<option value="<?php echo $ga; ?>"
												<?php echo ($var["genre"] == $ga ? ' selected = "selected"' : ''); ?>>
												<?php echo $ga; ?>
											</option>
									<?php
										}
									?>
								</select>
							</td>
							<td>
								<input type="text"
									value="<?php echo $var["publisher"]; ?>"
									name="pub"
									maxlength="25"
									required="required" />
							</td>
						</tr>
				<?php
					}
				?>
			</table>
			<input type="submit" value="Update" />
		</form>
<?php
	}	
	//Displays this if the user is not logged in
	else {
?>
		<p>You are not currently logged in.</p><br />
<?php
	}
?>
<?php include("./common/bottom.php"); ?>
