<?php
$username = $_POST['username'];
$password1 = $_POST['password1'];
$password2 = $_POST['password2'];
$email = $_POST['email'];
 
if($password1 != $password2)
    header('Location: registration.html');
 
if(strlen($username) > 20)
    header('Location: registration.html');
 
$hash = hash('sha256', $password1);
 
function createSalt()
{
    $text = md5(uniqid(rand(), true));
    return substr($text, 0, 3);
}
 
$salt = createSalt();
$password = hash('sha256', $salt . $hash);
 
$conn = mysqli_connect('localhost', 'root', 'root', 'moocs160');
 
$username = mysqli_real_escape_string($conn, $username);
 
$query = "INSERT INTO member ( username, password, email, salt ) VALUES 
		( '$username', '$password', '$email', '$salt' )";
 
mysqli_query($conn, $query);
 
mysqli_close($conn);
 
header('Location: index.php');
?>