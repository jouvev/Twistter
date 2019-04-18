create table twister_user (
    id int(16) not null AUTO_INCREMENT,
    username varchar(32),
    password BLOB,
    name varchar(32),
    firstName varchar(32),
    email varchar(64),
    root BOOLEAN,
    nbTwists int(16),
    nbLikes int(16),
    primary key (id),
    constraint unU unique(username),
    constraint unE unique(email)
);

create table twister_session (
	keySession varchar(64) not null,
	idUser int(16),
    dateCreation TIMESTAMP,
    root BOOLEAN,
    isValid BOOLEAN,
    primary key (keySession),
    constraint fk_user foreign key (idUser) references twister_user(id) on delete cascade
);

create table twister_friend (
    idUser1 int(16),
    idUser2 int(16),
    constraint pk primary key (idUser1, idUser2),
    constraint fk_user1 foreign key (idUser1) references twister_user(id) on delete cascade,
    constraint fk_user2 foreign key (idUser2) references twister_user(id) on delete cascade
);

insert into twister_user(username, password, name, firstName, email, root, nbTwists, nbLikes) values ("ToLluvo", "12345", "Antoine", "Cadiou", "gpasdmail@gmail.com", true, 0, 0);
insert into twister_user(username, password, name, firstName, email, root, nbTwists, nbLikes) values ("Skitry", "12345", "Vincent", "Jouve", "gpasdmail2@gmail.com", true, 0, 0);
insert into twister_user(username, password, name, firstName, email, root, nbTwists, nbLikes) values ("test", "test", "test", "test", "test", false, 0, 0);
insert into twister_friend values (1, 2);
insert into twister_friend values (2, 1);
insert into twister_session values ("1", 1, curdate(), 1, 1);
insert into twister_session values ("2", 2, curdate(), 1, 1);
