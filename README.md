# youtube_downloader
## Run 
`sbt run`  
then, server is ready on 0.0.0.0:8080.

## Dockerize
`sbt docker`  
it will produce a docker image named `hello/hello`    
`docker run -Pid hello/hello`  
then an docker container is running in the background, sounds and kicks  
`-i`   
is used here, as the interactive mode keep script running in the background  
instead of terminating at the end of the script
