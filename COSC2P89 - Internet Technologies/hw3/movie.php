#!/usr/bin/php-cgi
<!DOCTYPE html>
<!--

I had to split some lines into multiple lines so it would print legibly; 
sorry for the confusion, but it should still be easy to read.

-->
<html>
	<head>
		<title>Rancid Tomatoes</title>
		<meta charset="utf-8" />
		<link href="movie.css" type="text/css" rel="stylesheet" />
		<link rel="icon" type="image/png" 
		href="http://www.cosc.brocku.ca/Offerings/2P89/Images/rotten.gif" />
	</head>
	<body>
		<?php
			$fre;
			$movie = $_GET["film"];  //Scan user for film title, initialize into $movie
			$directory = "./moviefiles/$movie/";
			$filminfo = explode("\n",file_get_contents("./moviefiles/$movie/info.txt"));
			//Place info.txt contents into a string, then explode into array
			
			if ($filminfo[2]>60) { 
			//Determines if the image beside the overall score is rottenbig or freshbig
				$rottenornot = "./moviefiles/freshbig.png"; 
				//I moved the images to the public_html folder because using absolute URLs caused errors
				$rot = "Fresh"; 
				//For associated text
				}
			else {
				$rottenornot = "./moviefiles/rottenbig.png";
				$rot = "Rotten";
				}				
			
			$overviewimage = "./moviefiles/$movie/overview.png"; 
			//Self-explanatory: overview image
			$prdoverviewinfo = explode("\n",file_get_contents("./moviefiles/$movie/overview.txt"));
			//Similar to line 13, but doing this with overview.txt
			$pre = array("STARRING:","DIRECTOR:","RATING:","THEATRICAL RELEASE:",
			"MOVIE SYNOPSIS:","MPAA RATING:","RELEASE COMPANY:","RUNTIME:","GENRE:","BOX OFFICE:","LINKS:");
			$post = array("","","","","","","","","","","");
			$overviewinfo = str_replace($pre,$post,$prdoverviewinfo); 
			//This removes the superfluous nonsense "DIRECTOR:", "RUNTIME:", etc
			$filecount = count(glob($directory."review*.txt")); 
			//Counts the number of reviews in ./moviefiles/$movie/
			
			if ($filecount>10) { 
			//This is only for the bottom of the page where it says (1 - $upperlimit) of $filecount
				$upperlimit = "10";
				}
			else {
				$upperlimit = $filecount;
				}
				
			for ($i = 1; $i <= $upperlimit; $i++) { 
			//This took me literally four hours to figure out. 
			//Initializes n*4 vars where n is the $upperlimit value; 
			//each var corresponds to a review quote, the reviewer name, etc
				list($review[$i],$verdict[$i],$critic[$i],$publication[$i]) 
				= file("./moviefiles/$movie/review".$i.".txt");
				} 
				
			$halfpoint = $upperlimit/2; 
			//Numeric var to determine how many reviews in each column
		
		?>
		<div class="banner">
			<img class="ban" src="http://www.cs.washington.edu/education/courses/cse190m/12su/homework/2/banner.png" 
			alt="Rancid Tomatoes" />
		</div>
		<h1><?php echo "$filminfo[0] ($filminfo[1])"; ?></h1> 
		<div class="reviewshell">
			<div class="reviewright">
				<img src="<?php echo $overviewimage ?>" alt="general overview" />
				<dl>
					<dt>STARRING</dt>
						<dd><?php echo $overviewinfo[0]; ?></dd>
					<dt>DIRECTOR</dt>
						<dd><?php echo $overviewinfo[1]; ?></dd>
					<dt>RATING</dt>
						<dd><?php echo $overviewinfo[2]; ?></dd>
					<dt>THEATRICAL RELEASE</dt>
						<dd><?php echo $overviewinfo[3]; ?></dd>
					<dt>MOVIE SYNOPSIS</dt>
						<dd><?php echo $overviewinfo[4]; ?></dd>
					<dt>MPAA RATING</dt>
						<dd><?php echo $overviewinfo[5]; ?></dd>
					<dt>RELEASE COMPANY</dt>
						<dd><?php echo $overviewinfo[6]; ?></dd>
					<dt>RUNTIME</dt>
						<dd><?php echo $overviewinfo[7]; ?></dd>
					<dt>GENRE</dt>
						<dd><?php echo $overviewinfo[8]; ?></dd>
					<dt>BOX OFFICE</dt>
						<dd><?php echo $overviewinfo[9]; ?></dd>
				</dl>
			</div>
			<div class="reviewleft">
				<div class="score">
					<img src="<?php echo $rottenornot ?>" alt="<?php echo $rot ?>" />
					<?php echo "$filminfo[2]%"; ?>
				</div>
				<div class=quotel>
					<?php 
						for ($j = 1; $j <= $halfpoint+0.5; $j++) { 
							if ($verdict[$j] == 'ROTTEN') { 
							//Determines if the $verdict string is ROTTEN or FRESH, 
							//and uses the correct image accordingly (doesn't work; see below)
								$imgrating[$j] = "./moviefiles/rotten.gif";
								$fre[$j] = "Rotten";
								}
							else {
								$imgrating[$j] = "./moviefiles/fresh.gif";
								$fre[$j] = "Fresh";
								} 
					?>
							<p class="quotation">
								<img class="floatimage" src="<?php echo $imgrating[$j] ?>" 
								alt="<?php echo $fre[$j] ?>" /> 	
								<!-- This should show which reviews are rotten and which are fresh 
								but for some reason it keeps the 'else' conditions above 
								regardless if it meets the 'if' condition. -->
								<q><?php echo $review[$j] ?></q>
							</p>
							<p class="reviewer">
								<img class="floatimage" 
								src="http://www.cs.washington.edu/education/courses/cse190m/12su/homework/2/critic.gif" 
								alt="Critic" />
								<?php echo $critic[$j] ?> <br />
								<?php echo $publication[$j] ?>
							</p>
					<?php 
							} 
					?>
				</div>
				<div class="quoter">
					<?php 
						for (; $j <= $upperlimit; $j++) { 
							if ($verdict[$j] == 'ROTTEN') { 
							//Determines if the $verdict string is ROTTEN or FRESH, 
							//and uses the correct image accordingly
								$imgrating[$j] = "./moviefiles/rotten.gif";
								$fre[$j] = "Rotten";
								}
							else {
								$imgrating[$j] = "./moviefiles/fresh.gif";
								$fre[$j] = "Fresh";
								} 
					?>
							<p class="quotation">
								<img class="floatimage" src="<?php echo $imgrating[$j] ?>" 
								alt="<?php echo $fre[$j] ?>" /> 	
								<!-- This should show which reviews are rotten and which are fresh 
								but	for some reason it keeps the 'else' conditions above 
								regardless if it meets the 'if' condition. -->
								<q><?php echo $review[$j] ?></q>
							</p>
							<p class="reviewer">
								<img class="floatimage" 
								src="http://www.cs.washington.edu/education/courses/cse190m/12su/homework/2/critic.gif" 
								alt="Critic" />
								<?php echo $critic[$j] ?> <br />
								<?php echo $publication[$j] ?>
							</p>
					<?php 
							} 
					?>
				</div>					
				<div class="reviewbottom">
					<p>(1-<?php echo $upperlimit; ?>) of <?php echo $filecount; ?></p>
				</div>
			</div>
		</div>
		<div class="w3float">
			<a href="https://webster.cs.washington.edu/validate-html.php">
			<img src="http://webster.cs.washington.edu/w3c-html.png" 
			alt="Valid HTML5" /></a> <br />
			<a href="https://webster.cs.washington.edu/validate-css.php">
			<img src="http://webster.cs.washington.edu/w3c-css.png" 
			alt="Valid CSS" /></a>
		</div>
	</body>
</html>