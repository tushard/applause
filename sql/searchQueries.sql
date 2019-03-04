# Country="ALL" and Device="iPhone 4"
select t.testerId, t.firstName, t.lastName, count(1) as experience
from testers t, devices d, bug b
where d.description in ('iPhone 4')
      and b.testerId = t.testerId
      and b.deviceId = d.deviceId
group by b.testerId
order by experience desc;

# Country="ALL" and Device="iPhone 4" or Device="iPhone 5"
select t.testerId, t.firstName, t.lastName, count(1) as experience
from testers t, devices d, bug b
where d.description in ('iPhone 4', 'iPhone 5')
      and b.testerId = t.testerId
      and b.deviceId = d.deviceId
group by b.testerId
order by experience desc;

# Country="US" and Device="ALL"
select t.testerId, t.firstName, t.lastName, count(1) as experience
from testers t, devices d, bug b
where t.country in ('US')
      and b.testerId = t.testerId
      and b.deviceId = d.deviceId
group by b.testerId
order by experience desc;

# Country="US" or Country= JP" and Device="iPhone 4" and Device = "iPhone 5", or Device ="Galaxy S4"
select t.testerId, t.firstName, t.lastName, count(1) as experience
from testers t, devices d, bug b
where t.country in ('US', 'JP')
      and d.description in ('iPhone 4', 'iPhone 5', 'Galaxy S4')
      and b.testerId = t.testerId
      and b.deviceId = d.deviceId
group by b.testerId
order by experience desc;

# Country="ALL" and Device="ALL"
select t.testerId, t.firstName, t.lastName, count(1) as experience
from testers t, devices d, bug b
where b.testerId = t.testerId
      and b.deviceId = d.deviceId
group by b.testerId
order by experience desc;