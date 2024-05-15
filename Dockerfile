# Verwende ein Ubuntu-basiertes OpenJDK 21 JDK Image
FROM eclipse-temurin:21-jdk

# Setze Umgebungsvariablen
ENV NVM_DIR /root/.nvm
ENV NODE_VERSION 18

# Installiere notwendige Pakete und entferne die Paketliste
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    sudo \
    ca-certificates \
    maven \
    && rm -rf /var/lib/apt/lists/*

# Installiere nvm (Node Version Manager) und Node.js
RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash && \
    . "$NVM_DIR/nvm.sh" && nvm install $NODE_VERSION && nvm use $NODE_VERSION && \
    npm install -g npm@latest

# Setze das Arbeitsverzeichnis im Container
WORKDIR /app

# Kopiere die aktuelle Verzeichnisinhalte in das Container-Verzeichnis /app
COPY . /app

# Führt den Maven-Befehl aus, um das Projekt zu bauen
RUN mvn clean package -Pproduction

# Expose Port 8080
EXPOSE 8080

# Startet die Anwendung
CMD ["java", "-jar", "target/iot-dashboard-1.0-SNAPSHOT.jar"]