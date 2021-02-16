# Cosmos Emulator For MacOS

*Install Docker Desktop For MacOS from https://hub.docker.com/editions/community/docker-ce-desktop-mac/*
### Running The Cosmos DB Emulator Image On Your Local Machine
###### Get the ip address of your local machine
```
ipaddr="`ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}' | head -n 1`"
```
###### Download the Docker image
```
docker pull mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator
```
###### Run the Docker image
```
docker run -p 8081:8081 -p 8900:8900 -p 8901:8901 -p 8902:8902 -p 10250:10250 -p 10251:10251 -p 10252:10252 -p 10253:10253 -p 10254:10254 -p 10255:10255 -p 10350:10350  -m 4g --cpus=2.0 --name=test-linux-emulator -e AZURE_COSMOS_EMULATOR_PARTITION_COUNT=3 -e AZURE_COSMOS_EMULATOR_IP_ADDRESS_OVERRIDE=$ipaddr -it mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator
```

Once it starts running

###### In a different termal window, again load the ip address of your local machine into a variable
```
ipaddr="`ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}' | head -n 1`"
```

###### Download the certificate of the emulator that you just started running
```
curl -k https://$ipaddr:8081/_explorer/emulator.pem > emulatorcert.pem
```

###### Trust the certificate, this is a self signed certificate. 
* Open Keychain Access
* File > Import Items > emulatorcert.pem
* Once the emulatorcert.pem is loaded into the Keychain
* Double click on the same, it should show up as "localhost"
* Change the trust settings to "Always Trust"

###### you can now browse to https://localhost:8081/_explorer/index.html or https://{your_local_ip}/_explorer/index.html and get the connection string to your cosmosDB emulator
