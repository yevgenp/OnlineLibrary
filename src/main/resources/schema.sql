/* 
 * Spring Security default authorization schema (modified)
 */

drop all objects;

create table if not exists users (
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(50) not null,
	enabled boolean not null,
	fullname varchar(50),
	email varchar(50)
);

create table if not exists authorities (
	id identity,
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint if not exists fk_authorities_users foreign key(username) references users(username)
);

create unique index if not exists ix_auth_username on authorities (username,authority);

/*
 * Prepare user store
 */
/*
 * delete from authorities where username <> 'admin';
 * delete from users where username <> 'admin';
 */
merge into users key(username) values('admin','admin',true, 'Administrator','');
merge into authorities key(username) values(0,'admin','ROLE_ADMIN');


/*
 * Custom app tables
 */
create table if not exists books (
	id identity,
	genre varchar(50),
	author varchar(100),
	title varchar(100),
	description varchar(500),
	filename varchar(256),
	content blob
);
