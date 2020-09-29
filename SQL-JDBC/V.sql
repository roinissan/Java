create view ApartmentNumberInNeighborhood AS
SELECT NID, COUNT(distinct concat(Apartment.Door,Apartment.Number,Apartment.StreetName)) as ApartmentNumber
FROM Neighborhood
join 
Apartment on Neighborhood.NID = Apartment.NeighborhoodID
group by Neighborhood.NID

create view ConstructionEmployeeOverFifty AS
SELECT ConstructorEmployee.EID,CompanyName,SalaryPerDay
FROM ConstructorEmployee
inner join Employee on ConstructorEmployee.EID = Employee.EID and DATEDIFF(year,Employee.BirthDate,GETDATE())>49

create view MaxParking as
select x.ParkingAreaID as AID,x.CID,max(x.countap) as MaxParkingCount
from (select ParkingAreaID,CID,count(CID) as countap from CarParking
group by ParkingAreaID,CID)x
group by x.ParkingAreaID,x.CID

