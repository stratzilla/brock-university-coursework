#!/usr/bin/php-cgi
<?php include("./common/top.php"); include("./common/common.php"); ?>
<?php
	//Grabs which user they clicked on previous page
	$user = htmlspecialchars($_GET["username"]);
	//Sessions it for optional sorting
	$_SESSION["temp"] = $user;
?>
Here is <?php echo $user; ?>'s collection. <br /><br />
<?php
	$dbr = $db->query("
		SELECT *
			FROM users u
			JOIN books b
			ON b.userid = u.ident
			WHERE u.username = '$user'
	");
?>
<?php
	//Includes the table draw complex so I don't need to redo it every time
	include("./common/table.php");
?>
You can sort or index this collection by using the advanced options below. <br /><br />
<!--Sort by category-->
<div class="sortleft">
	<form action="./query-sort.php" method="post">
		<fieldset>
			<legend>Sort by...</legend>
			<strong>Category</strong><br />
				<select name="cate">	
					<option value="title">Book Title</option>
					<option value="year">Year Published</option>
					<option value="author">Author</option>
					<option value="condition">Condition of the Book</option>
					<option value="genre">Genre</option>
					<option value="publisher">Publisher</option>
				</select>
			<strong>Order</strong><br />
				<input type="radio" name="order" value="DESC" checked="checked"/>Descending <br />
				<input type="radio" name="order" value="ASC"/>Ascending <br /><br />
			<span class="shiftright">
				<input type="submit" value="Submit" />
			</span>
		</fieldset>
	</form>
</div>
<!--Search by specific genre-->
<div class="sortright">
	<form action="./query-sort2.php" method="post">
		<fieldset>
			<legend>Search by...</legend>
			<strong>Genre</strong><br />
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
				</select><br /><br />
			<span class="shiftright">
				<input type="submit" value="Submit" />
			</span>
		</fieldset>
	</form>
</div>
<?php include("./common/bottom.php"); ?>