<?php
  include_once("connection.php");

  if( isset($_POST['txtUsername']) && isset($_POST['txtPassword']) ) {
       $username = $_POST['txtUsername'];
       $password = $_POST['txtPassword'];

       $query = "SELECT User, Pass FROM user WHERE User = '$username' AND Pass = '$password'";

        $result = mysqli_query($conn, $query);

        if ($result->num_rows > 0)
          echo "success";
        else
          echo "failed";
}
 ?>
