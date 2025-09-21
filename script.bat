@echo off
echo Stopping wigell-gym
docker stop wigell-gym
echo Deleting container wigell-gym
docker rm wigell-gym
echo Deleting image wigell-gym
docker rmi wigell-gym
echo Running mvn package
call mvn package
echo Creating image wigell-gym
docker build -t wigell-gym:TryNr2 .
echo Creating and running container wigell-gym
docker run -d -p 6565:6565 --name wigell-gym --network services-network wigell-gym:TryNr2
echo Done!