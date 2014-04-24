<!DOCTYPE HTML>
<html>
	
	<head>
		<title>MOOCentral</title>		
		<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
 		<script src="//datatables.net/download/build/nightly/jquery.dataTables.js"></script>
 		<link href="//datatables.net/download/build/nightly/jquery.dataTables.css" rel="stylesheet" type="text/css" />
 		<link rel="stylesheet" type="text/css" href="styles.css">
 		<script>
 			 $(document).ready( function () {
 	 			var table = $('#mooTest').DataTable();
 	 			} );

 			 $(document).ready(function(){
				  $('#login-trigger').click(function(){
				    $(this).next('#login-content').slideToggle();
				    $(this).toggleClass('active');          
				    
				    if ($(this).hasClass('active')) $(this).find('span').html('&#x25B2;')
				      else $(this).find('span').html('&#x25BC;')
				    })
			});
 		</script>
	</head>



	
<!--
	<div align="center">
	 <a href="data.php">Testing Purposes page</a> 
	</div>
-->

<?php
	//$con = mysqli_connect("localhost", "sjsucsor_s2g414s", "abcd#1234", "sjsucsor_160s2g42014s");
	$con = mysqli_connect("localhost", "root", "root", "moocs160");	
	if(mysqli_connect_errno()){
		echo "failed to connect to MySQL: " . mysqli_connect_errno();
	}

	$data = mysqli_query($con, "Select * from course_data join coursedetails where coursedetails.course_id = course_data.id");
 
?>

	<body>
		

		<div style="padding: 10px; border: 1px solid black" >
		
		<div class="signup">
			<nav>
				<ul>
					<li id="login">
					  <a id="login-trigger" href="#">
					    Log in <span></span>
					  </a>
					  <div id="login-content">
					    <form name="login" action="login.php" method="post">
					      <fieldset id="inputs">
					        <input id="username" type="text" name="username" placeholder="Your username" required>   
					        <input id="password" type="password" name="password" placeholder="Password" required>
					      </fieldset>
					      <fieldset id="actions">
					        <input type="submit" id="submit" value="Log in">
					        <label><input type="checkbox" checked="checked"> Keep me signed in</label>
					      </fieldset>
					    </form>
					  </div>                     
					</li>
					<li id="signup">
					  <a href="registration.html">Sign up FREE</a>
					</li>
				</ul>
			</nav>
		</div>

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
							echo "<td><a href=\"" . "page.php?ref=" . $row['course_id'] . "\" target=\"_blank\">" . $row['title'] . "</a></td>";
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





