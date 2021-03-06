CREATE TABLE Bot (
  BotID int NOT NULL,
  BotName varchar(12),
  IsOnline boolean,
  SkillID int,
  World char(3),
  PRIMARY KEY (BotID)
);

CREATE TABLE Skill (
  SkillID int NOT NULL,
  SkillName varchar (20),
  SkillImage varchar (255),
  PRIMARY KEY (SkillID)
);

CREATE TABLE Item (
  ItemID int NOT NULL,
  ItemName varchar (30),
  ItemXp int,
  ItemPrice int,
  SkillID int,
  PRIMARY KEY (ItemID)
);

CREATE TABLE Report (
  BotID int NOT NULL,
  ItemID int NOT NULL,
  NumOfItems int,
  XpPerHour float,
  GpPerHour float,
  CurrentLevel int,
  TotalXp int,
  XpNextLevel int,
  TimeNextLevel varchar(255),
  PRIMARY KEY (BotID, ItemID)
);

ALTER TABLE Bot
  ADD CONSTRAINT FK_BOTSKILL
  FOREIGN KEY (SkillID) REFERENCES SKILL (SkillID);

ALTER TABLE Item
  ADD CONSTRAINT FK_ITEMSKILL
  FOREIGN KEY (SkillID) REFERENCES SKILL (SkillID);
  
