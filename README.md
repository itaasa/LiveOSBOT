# LiveOSBOT

### What?

Actions in-game are automated through bots.
The following project allows the user to monitor their OldSchool RuneScape bots live through their Android phones! Every few seconds a MySQL database updated with bot data is sent by a Java application I created, which establishes a connection with each bot. It can access each bot's data such as current skill levels, items collected, time to level up, etc. Now, along with an Android Application rightly named LiveOSBOT, the user can view the database from their phones. Thus, live data from each bot can now be read at your fingertips, without being invested into the game.

### Why?


When a player automates in-game actions, there is no need to physically be in front of the game. Thus, the player loses that connection with the data the bots provide, which is essential. However, with this application you can view live bot data with just your phone. 

### How?


The following project consists of four main components:
* Bot script written in Java
* Java package to connect each bot script with database
* Database created and managed using MySQL
* Android Application that will display data from the database to an Android phone.

#### Languages/Programs Used:
Java, MySQL, a bit of PHP, Android Studio, TriBot, Eclipse



