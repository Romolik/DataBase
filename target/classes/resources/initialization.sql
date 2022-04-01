drop table Type_Point_of_Sale;

create table Type_Point_of_Sale (
                                    ID_Type_Point_of_Sale varchar2(50) primary key,
                                    Name_Type_Point_of_Sale varchar2(50) not null
);

insert into Type_Point_of_Sale values ('1','Shop');
insert into Type_Point_of_Sale values ('2','Department Store');

drop table Point_of_Sale;

create table Point_of_Sale (
                               ID_Point_of_Sale varchar2(50) primary key,
                               ID_Type_Point_of_Sale varchar2(50) not null,
                               Number_Sections number(12,0) not null,
                               Number_Halls number(12,0) not null,
                               Number_Stalls number(12,0) not null,
                               "SIZE(m^2)" number(12,4) not null,
                               "Rental_Price($)" number(12,4) not null,
                               "Utilities($)" number(12,4) not null,
                               FOREIGN KEY (ID_Type_Point_of_Sale)  REFERENCES Type_Point_of_Sale (ID_Type_Point_of_Sale)
);

insert into Point_of_Sale values ('1', '2', 1, 1, 4, 1000, 5000, 400);
insert into Point_of_Sale values ('2', '1', 0, 1, 2, 500, 3000, 250);


drop table Post;

create table Post (
                            ID_Post varchar2(50) primary key,
                            Name_Post varchar2(50) not null
);

insert into Post values ('1', 'Seller');
insert into Post values ('2', 'Manager');

drop table Employee;

create table Employee (
                          ID_Employee varchar2(50) primary key,
                          ID_Post varchar2(50) not null,
                          Salary number(12,4) not null,
                          First_Name varchar2(50) not null,
                          Last_Name varchar2(50) not null,
                          Number_Phone varchar2(20) not null,
                          FOREIGN KEY (ID_Post)  REFERENCES Post (ID_Post)
);

insert into Employee values ('1', '1', 500, 'Ivan', 'Ivanov', '89537649060');
insert into Employee values ('2', '2', 1000, 'Ivan', 'Sergeev', '89537749060');
insert into Employee values ('3', '1', 600, 'Alina', 'Kuznetsova', '89535667667');

drop table Invoice;

create table Invoice (
                         ID_Invoice varchar2(50) primary key,
                         ID_Employee varchar2(50) not null,
                         "Date" DATE not null,
                         FOREIGN KEY (ID_Employee)  REFERENCES Employee (ID_Employee)
);

insert into Invoice values ('1','1',to_date('20220325','YYYYMMDD'));
insert into Invoice values ('2','1',to_date('20220325','YYYYMMDD'));

drop table Supplier;

create table Supplier (
                         ID_Supplier varchar2(50) primary key,
                         Name_Supplier varchar2(50) not null
);

insert into Supplier values ('1','Ozon');
insert into Supplier values ('2','SibirPromTorg');

drop table Buyer;

create table Buyer (
                       ID_Buyer varchar2(50) primary key,
                       First_Name varchar2(50) not null,
                       Last_Name varchar2(50) not null,
                       "Money_Spent($)" number(12,4)
);

insert into Buyer values ('1','Arkadiy', 'Ponomarev', 40);
insert into Buyer values ('2','Boris','Nemov', 70);

drop table Type_Product;

create table Type_Product (
                       ID_Type_Product varchar2(50) primary key,
                       Name_Type_Product varchar2(50) not null
);

insert into Type_Product values ('1','Potato');
insert into Type_Product values ('2','Lipstick');

drop table Product;

create table Product (
                         ID_Product varchar2(50) primary key,
                         ID_Type_Product varchar2(50) not null,
                         Article_Number varchar2(50) not null,
                         Number_of_Pieces number(12,0),
                         "Quantity(weight_in_grams)" number(12,4),
                         FOREIGN KEY (ID_Type_Product)  REFERENCES Type_Product (ID_Type_Product)
);

insert into Product values ('1','1',100000,0,10000);
insert into Product values ('2','2',100001,1,0);

drop table Purchase;

create table Purchase (
                          ID_Purchase varchar2(50) primary key,
                          ID_Point_of_Sale varchar2(50) not null,
                          ID_Product varchar2(50)  not null,
                          ID_Invoice varchar2(50) not null,
                          ID_Buyer varchar2(50) not null,
                          "Cost($)" number(12,4) not null,
                          FOREIGN KEY (ID_Point_of_Sale)  REFERENCES Point_of_Sale (ID_Point_of_Sale),
                          FOREIGN KEY (ID_Product)  REFERENCES Product (ID_Product),
                          FOREIGN KEY (ID_Invoice)  REFERENCES Invoice (ID_Invoice),
                          FOREIGN KEY (ID_Buyer)  REFERENCES Buyer (ID_Buyer)
);

insert into Purchase values ('1', '1', '1', '1', '1', 40);
insert into Purchase values ('2', '2', '2', '2', '2', 70);

drop table Order_shop;

create table Order_shop (
                            ID_Composition_of_Invoice varchar2(50) primary key,
                            ID_Point_of_Sale varchar2(50) not null,
                            ID_Product varchar2(50)  not null,
                            ID_Invoice varchar2(50) not null,
                            ID_Supplier varchar2(50) not null,
                            "Cost($)" number(12,4) not null,
                            FOREIGN KEY (ID_Point_of_Sale)  REFERENCES Point_of_Sale (ID_Point_of_Sale),
                            FOREIGN KEY (ID_Product)  REFERENCES Product (ID_Product),
                            FOREIGN KEY (ID_Invoice)  REFERENCES Invoice (ID_Invoice),
                            FOREIGN KEY (ID_Supplier)  REFERENCES Supplier (ID_Supplier)
);