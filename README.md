# kafka-producer-VideoConverter
Server hosting site on localhost:8081 that enables conversion MP4 files to AVI (using Jave library). The server is prepared to send the video as a message to kafka bitnami server (docker container).

The server is prepared to listen to topic on bitnami/kafka container and after receiving the video, converts it from MP4 format to AVI. Then it sends it back to kafka server on another topic.

You can try it out by running docker-compose.yml The images of producer and consumer are here: https://hub.docker.com/u/krokiet97
