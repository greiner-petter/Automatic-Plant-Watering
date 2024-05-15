
# Installiere notwendige Pakete und entferne die Paketliste
apt-get update && apt-get install -y --no-install-recommends \
    curl \
    sudo \
    ca-certificates \
    maven \
    && rm -rf /var/lib/apt/lists/*

# Installiere nvm (Node Version Manager) und Node.js
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash && \
    . "$NVM_DIR/nvm.sh" && nvm install $NODE_VERSION && nvm use $NODE_VERSION && \
    npm install -g npm@latest

sudo apt install openjdk-21-jdk
nvm install 18
nvm use 18

mvn clean package -Pproduction

sudo docker compose up