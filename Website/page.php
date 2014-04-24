<!DOCTYPE HTML>
<html>
	<head>
		<title>Content</title>
		<style>
			#titletags {
					font-family:"Times New Roman", Times, serif;
					font-size: 24px;
					color: blue;

				}
		</style>
	</head>

	<body>
		<?php
			//$con = mysqli_connect("localhost", "sjsucsor_s2g414s", "abcd#1234", "sjsucsor_160s2g42014s");
			$con = mysqli_connect("localhost", "root", "root", "moocs160");	
			if(mysqli_connect_errno()){
				echo "failed to connect to MySQL: " . mysqli_connect_errno();
			}

			$id = htmlspecialchars($_GET["ref"]);
			$data = mysqli_query($con, "Select * from course_data join coursedetails where coursedetails.course_id = course_data.id and course_id = '$id'");

			while($row = mysqli_fetch_array($data)){
				
				echo "<h1><a href=\"" . $row['course_link'] . "\" target=\"_blank\">" . $row['title'] . "</a></h1>";
				
				if(strlen($row['video_link']) > 5){
				echo "
					<iframe title=\"Youtube player\" width=\"480\" height=\"390\"
					src=\"http:" . $row['video_link'] . "\" 
					frameborder =\"0\" allowfullscreen></iframe></td><br/ >";
				}
				
				echo "<image src=\"" . $row['profimage'] . "\" alt=\"missing image\" height=\"200\" width=\"200\">";
				echo  "<div id=\"titletags\">" . "Professor: " . "</div>" . $row['profname'];
				echo  "<div id=\"titletags\">" . "Course Length: " . "</div>" . $row['course_length'];
				echo  "<div id=\"titletags\">" . "Course Description: " . "</div>" . $row['long_desc'];
				echo  "<div id=\"titletags\">" . "Category: " . "</div>" . $row['category'];
				echo  "<div id=\"titletags\">" . "Course Fee: " . "</div>" . $row['course_fee'];
				echo  "<div id=\"titletags\">" . "Language: " . "</div>" . $row['language'];
				echo  "<div id=\"titletags\">" . "Certification: " . "</div>" . $row['certificate'];
				echo  "<div id=\"titletags\">" . "University: " . "</div>" . $row['university'];		
			}
		?>
	</body>
</html>
