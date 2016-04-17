-- Initial data for the account manager

-- See H2 SQL docs for details:
-- http://h2database.com/html/grammar.html
-- http://h2database.com/html/datatypes.html

CREATE TABLE IF NOT EXISTS UserDatabase (
      LoginName VARCHAR(40) PRIMARY KEY NOT NULL,
       Password VARCHAR(1024) NOT NULL
);
CREATE TABLE IF NOT EXISTS CharacterDatabase (
    CharacterId INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
      LoginName VARCHAR(40) NOT NULL,
  CharacterName VARCHAR(40),
   CharacterEXP INT
);
CREATE TABLE IF NOT EXISTS CharData (
    CharacterId INT NOT NULL,
       StatName VARCHAR(50) NOT NULL,
      StatValue INT NOT NULL,
);
CREATE TABLE IF NOT EXISTS Skills (
        SkillID INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
      SkillName VARCHAR(40) NOT NULL,
      SkillDesc VARCHAR(200)
);
CREATE TABLE IF NOT EXISTS SkillAssign (
    CharacterId INT NOT NULL,
        SkillID INT NOT NULL
);
ALTER TABLE CharacterDatabase ADD FOREIGN KEY (LoginName) REFERENCES UserDatabase(LoginName);
ALTER TABLE SkillAssign ADD FOREIGN KEY (SkillID) REFERENCES Skills(SkillID);
ALTER TABLE SkillAssign ADD FOREIGN KEY (CharacterId) REFERENCES CharacterDatabase(CharacterId);
ALTER TABLE CharData ADD FOREIGN KEY (CharacterId) REFERENCES CharacterDatabase(CharacterId);
--INSERT INTO UserDatabase(LoginName, Password) VALUES ('Alpha','12345');