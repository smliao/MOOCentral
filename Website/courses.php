<?php
	session_start();
?>
<!DOCTYPE HTML>
<!--
	Telephasic 1.1 by HTML5 UP
	html5up.net | @n33co
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>MOOCentral Courses</title>		
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,600" rel="stylesheet" type="text/css" />
		<!--[if lte IE 8]><script src="js/html5shiv.js"></script><![endif]-->
		<script src="js/jquery.min.js"></script>
		<script src="js/jquery.dropotron.min.js"></script>
		<script src="js/skel.min.js"></script>
		<script src="js/skel-panels.min.js"></script>
		<script src="js/init.js"></script>
 		<script src="//datatables.net/download/build/nightly/jquery.dataTables.js"></script>
 		<link href="//datatables.net/download/build/nightly/jquery.dataTables.css" rel="stylesheet" type="text/css" />
		<noscript>
			<link rel="stylesheet" href="css/skel-noscript.css" />
			<link rel="stylesheet" href="css/style.css" />
			<link rel="stylesheet" href="css/style-n1.css" />
		</noscript>
		<script>
 			
 			// $(document).ready( function () {
 	 		//	var table = $('#mooTest').DataTable();
 	 		//	} );
			
 			 $(document).ready( function () {
	 			 $('#mooTest').DataTable( {
					"serverSide": true,
					"ajax": "serverside.php",
				    } );
 			} );

 			 $(document).ready(function(){
				  $('#login-trigger').click(function(){
				    $(this).next('#login-content').slideToggle();
				    $(this).toggleClass('active');          

				    if ($(this).hasClass('active')) $(this).find('span').html('&#x25B2;')
				      else $(this).find('span').html('&#x25BC;')
				    })
			});
			
			function rate(id, added)
			{
	   			$.ajax({
					type: 'POST',
	   				url: 'rating.php',
					data: { course_id: id, ponone: added },
					success : function(response) {
						if (response == "guest") {
							alert("You must log in before rating courses.");
						} else if (response == "success") {
	   						$('.rate_count_' + id).html(parseInt($('.rate_count_' + id).html(), 10) + added);
	   					}
					},
					error : function(jqXHR, textStatus, errorThrown){
						alert("Request Failed. Please try again later.");
					}
				});
			}
			
			function addToFavorites(id, opt)
			{
	   			$.ajax({
					type: 'POST',
	   				url: 'favorite.php',
					data: { course_id: id, option: opt },
					success : function(response) {
						if (response == "guest") {
							alert("You must log in before adding courses to favorites.");
						} else if (response == "added") {
	   					} else if (response == "removed") {
	   					} else {
						}
					},
					error : function(jqXHR, textStatus, errorThrown){
						alert("Request Failed. Please try again later.");
					}
				});
			}
 		</script>
	</head>
	
	<?php
		include('connector.php');

		//$data = mysqli_query($con, "Select * from course_data join coursedetails where coursedetails.course_id = course_data.id");
		$data = mysqli_query($con, "select * from rating inner join (Select course_image, title, start_date, course_length, site, profname, profimage, course_id from course_data inner join coursedetails where coursedetails.course_id = course_data.id group by long_desc) j where rating.course_id = j.course_id");
	?>

	<body class="no-sidebar" style="background: #fff">

		<!-- Header Wrapper -->
			<div id="header-wrapper">
						
				<!-- Header -->
					<div id="header" class="container">
						
						<!-- Logo -->
							<h1 id="logo"><a href="index.php">MOOCentral</a></h1>
						
						<!-- Nav -->
							<nav id="nav">
								<ul>
									<li><a href="courses.php">Courses</a></li>
									<li class="break">
										<?php if(!isset($_SESSION['sess_user_id'])) { ?>
										<a href="login.php">Login</a>
										<?php } else { ?>
										<a href=""><?php echo $_SESSION['sess_username'] ?></a>
										<ul>
											<li><a href="profile.php">Profile</a></li>
											<li><a href="profile.php#portfolio">Favorites</a></li>
											<li><a href="logout.php">Logout</a></li>
										</ul>
										<?php } ?>
									</li>
								</ul>
							</nav>

					</div>
			</div>

		<!-- Main Wrapper -->
			<div class="wrapper" style="margin:0 auto; width:98%; text-align:center">
				<table id="mooTest" class="dataTable" cellpadding="0" cellspacing="0" border="0" class="display" style="font-size:90%">
					<thead>
						<tr>
							<th> Favorite </th>
							<th> </th>
							<th> Title </th>
							<th> Rating </th>
							<th> Start Date </th>
							<th> Course Length </th>
							<th> Source </th>
							<th> Professor </th>
							<th> </th>
						</tr>
					</thead>
					<tbody>
						<?php /*
							while($row = mysqli_fetch_array($data)){
								echo "<tr>";
								echo "<td><button id=\"favorites\" onclick=\"addToFavorites(" . $row['course_id'] . ", 1)\">Add</button></td>";
								echo "<td><image src=\"" . $row['course_image'] . "\" alt=\"missing course image\" height=\"100\" width=\"100\"></td>";
								echo "<td width=\"500px\"><a href=\"" . "page.php?ref=" . $row['course_id'] . "\" target=\"_blank\">" . $row['title'] . "</a></td>";
								echo '<td nowrap><input style="
								background-image: url(images/thumbs_up.png);
								background-color: transparent; 
								background-repeat: no-repeat; 
								background-position: 0px 0px; 
								border: none;  
								cursor: pointer;    
								height: 24px; 
								padding-left: 24px;   
								vertical-align: middle; 
								" 
								type="button" onclick="rate(' . 
								$row['course_id'] . ', 1)"/><span style="margin-right: 8px;" class="rate_count_' .  
								$row['course_id'] . '">' . $row['rate'] . 
								'</span><input style="
								background-image: url(images/thumbs_down.png);
								background-color: transparent; 
								background-repeat: no-repeat; 
								background-position: 0px 0px; 
								border: none;  
								cursor: pointer;    
								height: 24px; 
								padding-left: 24px;   
								vertical-align: middle; 
								" type="button" onclick="rate(' . 
								$row['course_id'] . ', -1)"/></td>';
									
								echo "<td>" . $row['start_date'] . "</td>";
								echo "<td>" . $row['course_length'] . "</td>";
							//	echo "<td>" . $row['category'] . "</td>";
								echo "<td>" . $row['site'] . "</td>";
								echo "<td>" . $row['profname'] . "</td>";
								echo "<td><image src=\"" . $row['profimage'] . "\" alt=\"missing image\" height=\"100\" width=\"100\"></td>";
								echo "</tr>";
							}						
							mysqli_close($con);
							*/
						?>
					</tbody>
				</table>
			</div>

		<!-- Footer Wrapper -->
			<div id="footer-wrapper">

				<!-- Copyright -->
					<div id="copyright" class="container">
						<ul class="menu">
							<li>&copy; 2014 MOOCentral. All rights reserved.</li>
							<li>Design: <a href="http://html5up.net/">HTML5 UP</a></li>
						</ul>
					</div>

			</div>

	</body>
</html>
