create table student (age integer, id integer not null, firstname varchar(255), lastname varchar(255), primary key (id)) engine=InnoDB;
create table student_seq (next_val bigint) engine=InnoDB;
insert into student_seq values ( 1 );
