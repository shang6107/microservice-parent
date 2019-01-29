local listKey = 'red_package_list_p'
local redPackage = 'red_package_'..KEYS[1] 
local stock = tonumber(redis.call('hget', redPackage, 'stock')) 
if stock <= 0 then return 0 end 
stock = stock - 1 
redis.call('hset', redPackage, 'stock', tostring(stock)) 
redis.call('rpush', listKey, ARGV[1]) 
if stock == 0 then return 2 end 
return 1 
