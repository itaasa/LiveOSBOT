<?php
  include_once("connection.php");

  if( isset($_POST['txtUsername']) && isset($_POST['txtPassword']) ) {
       $username = $_POST['txtUsername'];
       $password = $_POST['txtPassword'];

       $query = "SELECT User, Pass FROM user WHERE User=? AND Pass=?";

        $stmt = mysqli_prepare($conn, "SELECT User, Pass FROM user WHERE User=? AND Pass=?");
        mysqli_stmt_bind_param($stmt, 'ss', $username, $password);
        mysqli_stmt_execute($stmt);
         mysqli_stmt_store_result($stmt);
        if (mysqli_stmt_num_rows($stmt) > 0)
          echo "success";
        else
          echo "failed";


	mysqli_stmt_close($stmt);
}
 ?>
