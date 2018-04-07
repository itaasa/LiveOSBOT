
INSERT INTO SKILL (SkillID, SkillName)
  VALUES
  (8, 'Woodcutting'),
  (10, 'Fishing'),
  (14, 'Mining');


INSERT INTO ITEM (ItemID, ItemName, ItemXP, ItemPrice, SkillID)
  VALUES
  (1511, 'Log', 25, 30, 8),
  (1519, 'Willow Log', 67, 11, 8),
  (1515, 'Yew Log', 175, 298, 8),
  (317, 'Raw Shrimp', 10, 17, 10),
  (377, 'Raw Lobster', 90, 131, 10),
  (383, 'Raw Shark', 110, 566, 10),
  (440, 'Iron ore', 35, 108, 14),
  (453, 'Coal', 50, 183, 14),
  (447, 'Mithril ore', 80, 175, 14);


INSERT INTO BOT (BotID, BotName, IsActive, IsOnline, SkillID, World)
  VALUES
  (0, 'Polygon' , 1, 1, 10, 133),
  (1, 'Pangwin' , 1, 1, 8, 123),
  (2, 'LebronJames' , 1, 1, 10, 222),
  (3, 'James Bot' , 1, 1, 10, 314),
  (4, 'Vcheckz1337', 1, 1, 14, 432),
  (5, 'Happy4Horses', 1, 1, 14, 111),
  (6, 'RickCaron', 1, 1, 8, 008),
  (7, 'Kate', 1, 1, 14, 155),
  (8, 'RackCityBot', 1, 1, 10, 010),
  (9, 'RudeRob2008', 1, 1, 8, 003),
  (10, 'Fishgutz', 1, 1, 8, 001),
  (11, 'Microwave', 1, 1, 14, 221),
  (12, 'SoppingMost', 1, 1, 14, 343),
  (13, 'Squatch Dad', 1, 1, 8, 100),
  (14, 'VeganGirl33', 1, 1, 14, 055),
  (15, 'Nick Veggie', 1, 1, 14, 098);


INSERT INTO REPORT (BotID, ItemID, NumOfItems, XpPerHour, GpPerHour)
  VALUES
  (0, 377, 0, 0, 0),
  (1, 1515, 0, 0, 0),
  (2, 383, 0, 0, 0),
  (3, 317, 0, 0, 0),
  (4, 453, 0, 0, 0),
  (5, 447, 0, 0, 0),
  (6, 1519, 0, 0, 0),
  (7, 440, 0, 0, 0),
  (8, 377, 0, 0, 0),
  (9, 1515, 0, 0, 0),
  (10, 1519, 0, 0, 0),
  (11, 440, 0, 0, 0),
  (12, 447, 0, 0, 0),
  (13, 1515, 0, 0, 0),
  (14, 453, 0, 0, 0),
  (15, 447, 0, 0, 0);
