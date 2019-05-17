<table>
	<tr>
		<th class="num">#</th>
		<th class="bk">Book Title</th>
		<th class="yr">Year Published</th>
		<th class="au">Author</th>
		<th class="cd">Condition of Book</th>
		<th class="ge">Genre</th>
		<th class="pb">Publisher</th>
	</tr>
	<?php
		foreach ($dbr as $var) {
	?>
			<tr>
				<td><?php echo $i; ?></td>
				<td><?php echo htmlspecialchars($var["title"]); ?></td>
				<td><?php echo htmlspecialchars($var["year"]); ?></td>
				<td><?php echo htmlspecialchars($var["author"]); ?></td>
				<td><?php echo htmlspecialchars($var["condition"]); ?></td>
				<td><?php echo htmlspecialchars($var["genre"]); ?></td>
				<td><?php echo htmlspecialchars($var["publisher"]); ?></td>
			</tr>
	<?php
		$i++;
		}
	?>
</table> <br />