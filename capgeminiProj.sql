-- criar um banco de dados
create database dbanuncios;
-- abre o banco de dados dbinfox
use dbAnuncios;
-- cria uma tabela no banco de dados aberto
create table tbcriterios(
idcri int primary key,
nclic varchar(15) not null,
ncompartilha varchar(15) not null,
newvisu varchar(15) not null,
nvisu varchar(15) not null,
maxcomparti varchar(15) not null
);
insert into tbcriterios (idcri, nclic, ncompartilha, newvisu, nvisu, maxcomparti)
values (1, '0.12', '0.15', '40','30','4');
select * from tbcriterios;

CREATE TABLE `tbclientes` (
  `idcli` int(11) NOT NULL AUTO_INCREMENT,
  `nomecli` varchar(50) NOT NULL,
  `endcli` varchar(100) DEFAULT NULL,
  `fonecli` varchar(50) NOT NULL,
  `emailcli` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idcli`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
insert into tbclientes(nomecli,endcli,fonecli,emailcli)
values('Alvaro Gates','Rua do Beco, 2115','9903-7236','agates@hotline.com');
insert into tbclientes(nomecli,endcli,fonecli,emailcli)
values('Paulo Miranda','Avenida Meireles, 1134','9937-3224','pmiranda@gmail.com');
insert into tbclientes(nomecli,endcli,fonecli,emailcli)
values('Cinara Lenon','Rua Gomes, 168','9134-5294','clenon@gmail.com');
-- --------------------------------------------------------------
-- Se deletar um cliente, tem que deletar todos os anúncios dele
select * from tbclientes;
select * from tbanuncios;
select * from tbcriterios where idcri = 1;
delete from tbclientes where idcli=3;
delete from tbanuncios where idcli=3;
-- --------------------------------------------------------------

create table tbanuncios(
`idanu` int primary key AUTO_INCREMENT,
`nomeanu` varchar(50) NOT NULL,
`datainicio` varchar(11) NOT NULL,
`datatermino` varchar(11) NOT NULL,
investedia decimal (10,2),
idcli int not null,
foreign key(idcli) references tbclientes(idcli)
);
select * from tbanuncios;
describe tbclientes;
describe tbanuncios;
select 
O.os,equipamento,defeito,valor,
C.nomecli,fonecli
from tbos as O
inner join tbclientes as C
on (O.idcli = C.idcli);