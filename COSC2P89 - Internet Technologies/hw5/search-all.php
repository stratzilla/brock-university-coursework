#!/usr/bin/php-cgi


<?php include("./common/top.html"); include("./common/common.php"); ?>
	<?php
		//Table joins and etc
		$rows = $db->query("
			SELECT actor_id, movie_id, name, year
				FROM roles r
				JOIN actors a
				ON a.id = r.actor_id
				JOIN movies m
				ON m.id = r.movie_id
				WHERE first_name = '$actor_first'
				AND last_name = '$actor_last'
				ORDER BY year DESC"
		);
		//Determine if it should create a table or not
		if ($rows->rowCount()>0) {
	?>
			<h1>Results for <?php echo $actor; ?></h1>
			<p>List of films in which <?php echo $actor; ?> acted</p>
			<?php
				/*Instead of defining and calling a function
					I included another .php page with my
					table function*/
				include("./common/function.php");
		}
		else {
	?>
			<!-- If actor is not found -->
			<h1>Actor <?php echo $actor; ?> not found.</h1>
	<?php
		}
	?>
<?php include("./common/bottom.html"); ?>