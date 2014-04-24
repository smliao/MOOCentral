<html>
	<head>
	</head>

<body>

	<h1> MOOCentral </h1>
	<div align="center">
	<form>
		<input name="keyword" type="text" placeholder="Search for a course" />
		<input type="submit" value="Query" />
	</form>

	<a href="data.php">Testing Purposes page</a>
	</div>

<?php
	//$con = mysqli_connect("localhost", "sjsucsor_s2g414s", "abcd#1234", "sjsucsor_160s2g42014s");
	$con = mysqli_connect("localhost", "root", "root", "moocs160");	
	if(mysqli_connect_errno()){
		echo "failed to connect to MySQL: " . mysqli_connect_errno();
	}

	$data = mysqli_query($con, "Select * from course_data join coursedetails where coursedetails.course_id = course_data.id");

	echo "<table border = '1' 
		<tr>
		<th> TITLE </th>
		<th> short_desc </th>
		<th> long_desc</th>
		<th> video </th>
		<th> course_image</th>
		<th> category </th>
		<th> course_fee</th>
		<th>  language </th>
		<th>  certificate </th>
		<th>  university </th>
		<th>  time_scraped </th>
		</tr>";

	while($row = mysqli_fetch_array($data)){
		echo "<tr>";
	//	echo "<td>" . $row['id'] . "</td>";
		echo "<td>" . $row['title'] . "</td>";
		echo "<td>" . $row['short_desc'] . "</td>";
		echo "<td>" . $row['long_desc'] . "</td>";
	//	echo "<td><a href=\"" . $row['course_link'] .  "\" target=\"_blank\">Course Link</a></td>";
		/*
		echo "<td> 
			<iframe title=\"Youtube player\" width=\"480\" height=\"390\"
			src=\"http:" . $row['video_link'] . "\" 
			frameborder =\"0\" allowfullscreen></iframe></td>";
		*/
		echo "<td> <a href=\"" .$row['video_link'] . "\" target=\"_blank\"> Link to youtube </a></td>";

	//	echo "<td>" . $row['start_date'] . "</td>";
	//	echo "<td>" . $row['course_length'] . "</td>";
		echo "<td><a href=\"" . $row['course_link'] . "\" target=\"_blank\"><image src=\"" . $row['course_image'] . "\" alt=\"missing course image\" height=\"100\" width=\"100\"></a></td>";

		echo "<td>" . $row['category'] . "</td>";
		echo "<td>" . $row['course_fee'] . "</td>";
		echo "<td>" . $row['language'] . "</td>";
		echo "<td>" . $row['certificate'] . "</td>";
		echo "<td>" . $row['university'] . "</td>";
		echo "<td>" . $row['time_scraped'] . "</td>";
	//	echo "<td>" . $row['site'] . "</td>";
	//	echo "<td>" . $row['profname'] . "</td>";
	//	echo "<td><image src=\"" . $row['profimage'] . "\" alt=\"missing image\" height=\"100\" width=\"100\"></td>";

		echo "</tr>";
	}
	echo "</table>";
?>



</body>

</html>
