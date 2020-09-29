create procedure AddMunicipalEnployee_s 
@EID int,
@LASTNAME varchar(255),
@FIRSTNAME varchar(255),
@BIRTHDATE date,
@STREETNAME varchar(255),
@NUMBER int,
@DOOR int,
@CITY varchar(255)
AS
BEGIN
INSERT INTO Employee(EID,LastName,FirstName,BirthDate,StreetName,Number,door,City)
values(@EID,@LASTNAME,@FIRSTNAME,@BIRTHDATE,@STREETNAME,@NUMBER,@DOOR,@CITY)
end



create procedure AddMunicipalEnployeeOfficial_s
@EID int,
@LASTNAME varchar(255),
@FIRSTNAME varchar(255),
@BIRTHDATE date,
@STREETNAME varchar(255),
@NUMBER int,
@DOOR int,
@CITY varchar(255),
@STARTDATE date ,
@DEGREE varchar(255) ,
@DEPARTMENTID int
AS
BEGIN
INSERT INTO Employee(EID,LastName,FirstName,BirthDate,StreetName,Number,door,City)
values(@EID,@LASTNAME,@FIRSTNAME,@BIRTHDATE,@STREETNAME,@NUMBER,@DOOR,@CITY)
INSERT INTO OfficialEmployee(EID,StartDate,Degree,DepartmentId)
values(@EID,@STARTDATE,@DEGREE,@DEPARTMENTID)
END



create procedure AddMunicipalEnployeeConstructor_s
@EID int,
@LASTNAME varchar(255),
@FIRSTNAME varchar(255),
@BIRTHDATE date,
@STREETNAME varchar(255),
@NUMBER int,
@DOOR int,
@CITY varchar(255),
@CompanyName varchar(255) ,
@SalaryPerDay int
AS
BEGIN
INSERT INTO Employee(EID,LastName,FirstName,BirthDate,StreetName,Number,door,City)
values(@EID,@LASTNAME,@FIRSTNAME,@BIRTHDATE,@STREETNAME,@NUMBER,@DOOR,@CITY)
INSERT INTO ConstructorEmployee(EID,CompanyName,SalaryPerDay)
values(@EID,@CompanyName,@SalaryPerDay)
END



CREATE PROCEDURE sp_StartParking
@CID int,
@STARTPARK datetime,
@PARKID int
AS
BEGIN
INSERT INTO CarParking(CID,StartTime,ParkingAreaID)
VALUES (@CID,@STARTPARK,@PARKID)
END



CREATE PROCEDURE sp_EndParking
@CID int,
@ENDPARK datetime,
@PARKID int
AS
BEGIN
INSERT INTO CarParking(CID,EndTime,ParkingAreaID)
VALUES (@CID,@ENDPARK,@PARKID)
END