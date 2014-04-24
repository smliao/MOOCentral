<?php
$username = $_POST['username'];
$password = $_POST['password'];
 
$conn = mysqli_connect('localhost', 'root', 'root', 'moocs160');
 
$username = mysqli_real_escape_string($conn, $username);
$query = "SELECT password, salt
        FROM member
        WHERE username = '$username';";
 
$result = mysqli_query($conn, $query);
 
if(mysqli_num_rows($result) == 0) // User not found. So, redirect to login_form again.
{
    header('Location: registration.html');
}
 
$userData = mysqli_fetch_array($result, MYSQL_ASSOC);
$hash = hash('sha256', $userData['salt'] . hash('sha256', $password) );
 
if($hash != $userData['password']) // Incorrect password. So, redirect to login_form again.
{
    header('Location: registration.html');
}else{ // Redirect to home page after successful login.
	header('Location: index.php');
}
?>