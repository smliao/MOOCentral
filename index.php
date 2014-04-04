<html>

<body>

<?php
	$con = mysqli_connect("localhost", "root", "", "MOOCentral");
	if(mysqli_connect_errno()){
		echo "failed to connect to MySQL: " . mysqli_connect_errno();
	}

	$data = mysqli_query($con, "Select * from coursedata");

	echo "<table border = '1' 
		<tr>
		<th> ID </th>
		<th> TITLE </th>
		<th> short_desc </th>
		<th> long_desc </th>
		<th> course_link </th>
		<th> video_link </th>
		</tr>";

	while($row = mysqli_fetch_array($data)){
		echo "<tr>";
		echo "<td>" . $row['id'] . "</td>";
		echo "<td>" . $row['title'] . "</td>";
		echo "<td>" . $row['short_desc'] . "</td>";
		echo "<td>" . $row['long_desc'] . "</td>";
		echo "<td><a href=\"" . $row['course_link'] .  "\" target=\"_blank\">Course Link</a></td>";
		echo "<td> 
			<iframe title=\"Youtube player\" width=\"480\" height=\"390\"
			src=\"http:" . $row['video_link'] . "\" 
			frameborder =\"0\" allowfullscreen></iframe></td>";

		echo "</tr>";
	}
	echo "</table>";
?>

</body>>

</html>
