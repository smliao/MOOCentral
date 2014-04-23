<!DOCTYPE HTML>
<html>
	
	<head>
		<title>MOOCentral</title>
		
		<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
 		<script src="//datatables.net/download/build/nightly/jquery.dataTables.js"></script>
 		<link href="//datatables.net/download/build/nightly/jquery.dataTables.css" rel="stylesheet" type="text/css" />
 		<script>
 			 $(document).ready( function () {
 	 			var table = $('#mooTest').DataTable();
 	 			} );
 			</script>
	</head>



	
<!--
	<div align="center">
	 <a href="data.php">Testing Purposes page</a> 
	</div>
-->

<?php
	$con = mysqli_connect("localhost", "sjsucsor_s2g414s", "abcd#1234", "sjsucsor_160s2g42014s");
	//$con = mysqli_connect("localhost", "root", "", "moocs160");	
	if(mysqli_connect_errno()){
		echo "failed to connect to MySQL: " . mysqli_connect_errno();
	}

	$data = mysqli_query($con, "Select * from course_data join coursedetails where coursedetails.course_id = course_data.id");
 
?>

	<body>
		

		<div style="padding: 10px; border: 1px solid black" >
			<h1> MOOCentral </h1>
			<table id="mooTest" class="dataTable" cellpadding="0" cellspacing="0" border="0" class="display">
				<thead>
					<tr>
						<th> TITLE </th>
						<th> start_date </th>
						<th> course_length </th>
						<th> course_image </th>
						<th> site </th>
						<th> profname </th>
						<th> profimage </th>
					</tr>
				</thead>
				<tbody>
					<?php
						while($row = mysqli_fetch_array($data)){
							echo "<tr>";
						//	echo "<td>" . $row['id'] . "</td>";
							echo "<td><a href=\"" . $row['course_link'] . "\" target=\"_blank\">" . $row['title'] . "</a></td>";
						//	echo "<td>" . $row['short_desc'] . "</td>";
							// echo "<td>" . $row['long_desc'] . "</td>";
						//	echo "<td><a href=\"" . $row['course_link'] .  "\" target=\"_blank\">Course Link</a></td>";

							/* removed video for faster loading
							echo "<td> 
								<iframe title=\"Youtube player\" width=\"480\" height=\"390\"
								src=\"http:" . $row['video_link'] . "\" 
								frameborder =\"0\" allowfullscreen></iframe></td>";
							*/

							echo "<td>" . $row['start_date'] . "</td>";
							echo "<td>" . $row['course_length'] . "</td>";
							echo "<td><image src=\"" . $row['course_image'] . "\" alt=\"missing course image\" height=\"100\" width=\"100\"></td>";
						//	echo "<td>" . $row['category'] . "</td>";
							echo "<td>" . $row['site'] . "</td>";
							echo "<td>" . $row['profname'] . "</td>";
							echo "<td><image src=\"" . $row['profimage'] . "\" alt=\"missing image\" height=\"100\" width=\"100\"></td>";

							echo "</tr>";
						}

					?>

				</tbody>
			</table>
	</body>
	</html>





