create trigger DeleteProject on project
after delete 
as
begin
DELETE x FROM Employee x
inner join
ConstructorEmployee y ON x.EID  = y.EID 
left join 
ProjectConstructorEmployee z ON y.EID=z.EID and z.PID is null
END



create trigger ParkingDiscount on CarParking 
after insert
as
begin 
DECLARE @X int;
SET @X = (SELECT CID from inserted)
DECLARE @Y int;
SET @Y= (SELECT ID FROM Cars
WHERE @X = Cars.CID)
if(@Y in (SELECT EID FROM OfficialEmployee))
begin
	UPDATE CarParking
	SET Cost = Cost*0.8 
	WHERE CID = @X
end
END



create trigger Park on CarParking
after insert
as
begin
DECLARE @ENDDATE datetime;
SET @ENDDATE = (SELECT EndTime from inserted)
DECLARE @STARTDATE datetime;
SET @STARTDATE = (SELECT StartTime from inserted)
DECLARE @PARKID int;
SET @PARKID = (SELECT ParkingAreaID FROM inserted)
DECLARE @PRICE int;
SET @PRICE = (SELECT priceperhour FROM ParkingArea WHERE @PARKID=AID)
DECLARE @TOTAL int;
SET @TOTAL = @PRICE*datediff(hour,@STARTDATE,@ENDDATE)
if(@ENDDATE is not null and @STARTDATE is not null)
begin
	UPDATE CarParking
	set Cost = @TOTAL
	WHERE CID = (SELECT CID from inserted)
end
END
