<?php
    include_once("connection.php");

    //The following query will obtain a specific bots numerical data, along with name, performing skill name...

    if (isset($_POST['inputBotID']) && isset($_POST['inputItemID'])){

      $botID = $_POST['inputBotID'];
      $itemID = $_POST['inputItemID'];

      $query = "SELECT REPORT.*, BOT.BotName, BOT.IsActive, BOT.IsOnline, ITEM.ItemName, SKILL.SkillName
                    FROM REPORT
                    INNER JOIN BOT ON REPORT.BotID = BOT.BotID
                    INNER JOIN ITEM ON REPORT.ItemID = ITEM.ItemID
                    INNER JOIN SKILL ON ITEM.SkillID = SKILL.SkillID
                    WHERE REPORT.BotID = '$botID' AND REPORT.ItemID = '$itemID'";

      $result  = mysqli_query ($conn, $query);

      while($row = mysqli_fetch_assoc($result)){
        $data[] = $row;
      }

      echo json_encode($data);
  }
 ?>
