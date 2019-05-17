#!/usr/bin/php-cgi

<?php include("./common/top.html"); include("./common/common.php"); ?>
	<?php
		//Table joins and etc
		$rows = $db->query("
			SELECT name, year
				FROM movies m
				WHERE EXISTS (SELECT *
					FROM roles r1
					JOIN actors a1 
					ON a1.id = r1.actor_id
					WHERE r1.movie_id = m.id
					AND a1.first_name = '$actor_first'
					AND a1.last_name = '$actor_last'
				)
				AND EXISTS (SELECT *
					FROM roles r2
					JOIN actors a2
					ON a2.id = r2.actor_id
					WHERE r2.movie_id = m.id
					AND a2.first_name = 'Kevin'
					AND a2.last_name = 'Bacon'
				)
				ORDER BY year DESC"
		);
		//Determine if it should create a table or not
		if ($rows->rowCount()>0) {
	?>
			<h1>Results for <?php echo $actor; ?></h1>
			<p>Films with <?php echo $actor; ?> and Kevin Bacon</p>
			<?php
				/*Instead of defining and calling a function
					I included another .php page with my
					table function*/
				include("./common/function.php");
		}
		else {
	?>
			<!-- If actor is not found -->
			<h1>Actor <?php echo $actor; ?> has not acted with Kevin Bacon.</h1>
	<?php
		}
	?>
<?php include("./common/bottom.html"); ?>