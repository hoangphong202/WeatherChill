CREATE DATABASE Weather
USE Weather

	CREATE TABLE role(
		id int AUTO_INCREMENT NOT NULL,
		name Nvarchar(255),
		primary key(id)
	);
	INSERT INTO role(name) VALUES ("Admin")
	INSERT INTO role(name) VALUES ("Client")

CREATE TABLE user(
    id int AUTO_INCREMENT NOT NULL,
    name Nvarchar(255),
    email Nvarchar(255),
    password Nvarchar(255),
    role_id int,
    primary key(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);
INSERT INTO user(name, email, password, role_id) VALUES ("admin","admin", "admin",1)
INSERT INTO user(name, email, password, role_id) VALUES ("lgp","lgp", "lgp",2)


CREATE TABLE category_image(
	id int AUTO_INCREMENT NOT NULL,
	name Nvarchar(255),
	primary key(id)
);
INSERT INTO category_image( name ) VALUES ("Genshin")
INSERT INTO category_image( name ) VALUES ("Chill")
INSERT INTO category_image( name ) VALUES ("pixel")
        
CREATE TABLE image(
    ID int AUTO_INCREMENT PRIMARY KEY,
    Path Nvarchar(255),
    info Nvarchar(255),
    category_image_id int,
    FOREIGN KEY (category_image_id) REFERENCES category_image(id)
);


CREATE TABLE category(
	id int AUTO_INCREMENT NOT NULL,
	name Nvarchar(255),
	primary key(id)
);
INSERT INTO category( name ) VALUES ("chill")
INSERT INTO category( name ) VALUES ("nighcore")
        
	CREATE TABLE music(
		id int AUTO_INCREMENT NOT NULL,
		path Nvarchar(255),
		name Nvarchar(255),
		category_id int,
		primary key(id),
		FOREIGN KEY (category_id) REFERENCES category(id)
	);



CREATE TABLE album_image(
	id int AUTO_INCREMENT NOT NULL,
	name Nvarchar(255),
	img_path Nvarchar(255),
	category_id int,
	FOREIGN KEY (category_id) REFERENCES category(id),
	primary key(id)
);



CREATE TABLE album(
	id int AUTO_INCREMENT NOT NULL,
	name Nvarchar(255),
	img_path Nvarchar(255),
	category_id int,
	FOREIGN KEY (category_id) REFERENCES category(id),
	primary key(id)
);

CREATE TABLE albuminfo(
	id int AUTO_INCREMENT NOT NULL,
	music_id int NOT NULL, 
	album_id int NOT NULL,
    image_id int NOT NULL,
	FOREIGN KEY (music_id) REFERENCES music(id),
	FOREIGN KEY (album_id) REFERENCES album(id),
    FOREIGN KEY (image_id) REFERENCES image(id),
	primary key(id)
)



INSERT INTO image(Path, info,category_image_id) VALUES ("/Image/bg2.gif","Musics/Auld Lang Syne.mp3",2)
INSERT INTO image(Path, info,category_image_id) VALUES ("/Image/bg3.gif","Musics/Auld Lang Syne.mp3",2)
INSERT INTO image(Path, info,category_image_id) VALUES ("/Image/bg4.gif","Musics/Auld Lang Syne.mp3",1)


INSERT INTO music(path, name, category_id ) VALUES ("ten.mp3","Musics/Auld Lang Syne",1)



