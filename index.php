<html>
	<head>
	</head>

<body>
	<div align="center">
	<form>
		<input name="keyword" type="text" placeholder="Search for a course" />
		<input type="submit" value="Query" />
	</form>
	</div>

<?php
	$con = mysqli_connect("localhost", "sjsucsor_s2g414s", "abcd#1234", "sjsucsor_160s2g42014s");
	//$con = mysqli_connect("localhost", "root", "", "MOOCentral");	
	if(mysqli_connect_errno()){
		echo "failed to connect to MySQL: " . mysqli_connect_errno();
	}

	$data = mysqli_query($con, "Select * from coursedata");

	echo "<table border = '1' 
		<tr>
		<th> ID </th>
		<th> TITLE </th>
		<th> short_desc </th>
		<th> course_link </th>
		<th> start_date </th>
		<th> course_length </th>
		<th> course_image </th>
		<th> category </th>
		<th> site </th>
		<th> profname </th>
		<th> profimage </th>
		</tr>";

	while($row = mysqli_fetch_array($data)){
		echo "<tr>";
		echo "<td>" . $row['id'] . "</td>";
		echo "<td>" . $row['title'] . "</td>";
		echo "<td>" . $row['short_desc'] . "</td>";
		// echo "<td>" . $row['long_desc'] . "</td>";
		echo "<td><a href=\"" . $row['course_link'] .  "\" target=\"_blank\">Course Link</a></td>";
		
		/* removed video for faster loading
		echo "<td> 
			<iframe title=\"Youtube player\" width=\"480\" height=\"390\"
			src=\"http:" . $row['video_link'] . "\" 
			frameborder =\"0\" allowfullscreen></iframe></td>";
		*/

		echo "<td>" . $row['start_date'] . "</td>";
		echo "<td>" . $row['course_length'] . "</td>";
		echo "<td><image src=\"" . $row['course_image'] . "\" alt=\"profimage\" height=\"100\" width=\"100\"></td>";
		echo "<td>" . $row['category'] . "</td>";
		echo "<td>" . $row['site'] . "</td>";
		echo "<td>" . $row['profname'] . "</td>";
		echo "<td><image src=\"" . $row['profimage'] . "\" alt=\"profimage\" height=\"100\" width=\"100\"></td>";

		echo "</tr>";
	}
	echo "</table>";
?>



</body>>

</html>
