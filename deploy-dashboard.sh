mvn clean package -Pproduction
docker build . -t iot-dashboard:latest
docker run -p 8080:8080 iot-dashboard:latest