
<?php
	//Table function
?>
<table>
	<!-- Table heading -->
	<tr>
		<th class="num">#</th>
		<th class="title">Title</th>
		<th class="year">Year</th>
	</tr>	
		<?php
			//Loop to print the table
			foreach ($rows as $row) {
				?>
				<tr>
					<td><?php echo $i; ?></td>
					<td><?php echo $row["name"]; ?></td>
					<td><?php echo $row["year"]; ?></td>
				</tr>
				<?php
					$i++;
			}
		?>
</table>
<br />
