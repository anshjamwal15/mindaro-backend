#!/bin/bash

# Mindaro Backend Deployment Script for Amazon Linux 2 (2GB RAM)
# This script optimizes the system and deploys the application

set -e

echo "🚀 Starting Mindaro Backend deployment for Amazon Linux 2..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if running as root or with sudo
if [[ $EUID -eq 0 ]]; then
   print_error "This script should not be run as root. Please run as ec2-user."
   exit 1
fi

# Check available memory
TOTAL_MEM=$(free -m | awk 'NR==2{printf "%.0f", $2}')
print_status "Total system memory: ${TOTAL_MEM}MB"

if [ "$TOTAL_MEM" -lt 1800 ]; then
    print_warning "System has less than 2GB RAM. Performance may be impacted."
fi

# System optimizations for 2GB RAM
print_status "Applying system optimizations..."

# Create swap file if it doesn't exist (512MB for 2GB RAM system)
if [ ! -f /swapfile ]; then
    print_status "Creating swap file..."
    sudo fallocate -l 512M /swapfile
    sudo chmod 600 /swapfile
    sudo mkswap /swapfile
    sudo swapon /swapfile
    echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
fi

# Optimize kernel parameters for low memory systems
print_status "Optimizing kernel parameters..."
sudo tee -a /etc/sysctl.conf > /dev/null <<EOF

# Memory optimization for 2GB RAM
vm.swappiness=10
vm.vfs_cache_pressure=50
vm.dirty_ratio=15
vm.dirty_background_ratio=5

# Network optimization
net.core.somaxconn=65535
net.core.netdev_max_backlog=5000
net.ipv4.tcp_max_syn_backlog=65535
net.ipv4.tcp_fin_timeout=30
net.ipv4.tcp_keepalive_time=1200
net.ipv4.tcp_max_tw_buckets=400000
net.ipv4.tcp_tw_reuse=1
net.ipv4.ip_local_port_range=1024 65000
EOF

# Apply sysctl changes
sudo sysctl -p

# Install required packages
# print_status "Installing required packages..."
# sudo yum update -y
# sudo yum install -y java-17-amazon-corretto mariadb105-server mariadb105

# Start and enable MariaDB
# print_status "Starting MariaDB service..."
# sudo systemctl start mariadb
# sudo systemctl enable mariadb
# 
# # Secure MariaDB installation
# print_status "Securing MariaDB installation..."
# sudo mysql_secure_installation
# 
# # Create database and user
# print_status "Setting up database..."
# sudo mysql -u root -p -e "
# CREATE DATABASE IF NOT EXISTS test_advj;
# CREATE USER IF NOT EXISTS 'mindaro_user'@'localhost' IDENTIFIED BY 'awsEE78';
# GRANT ALL PRIVILEGES ON test_advj.* TO 'mindaro_user'@'localhost';
# FLUSH PRIVILEGES;
# "

# Build the application
print_status "Building the application..."
./gradlew clean build -x test

# Create logs directory
mkdir -p logs

# Copy systemd service file
print_status "Setting up systemd service..."
sudo cp mindaro-backend.service /etc/systemd/system/
sudo systemctl daemon-reload

# Set proper permissions
sudo chown -R ec2-user:ec2-user /home/ec2-user/mindaro-backend

# Create production profile
print_status "Creating production profile..."
cat > src/main/resources/application-prod.properties <<EOF
# Production profile for Amazon Linux 2 with 2GB RAM
spring.application.name=mindaro-backend

# JVM Memory Configuration for 2GB RAM
spring.jvm.memory=-Xms512m -Xmx1536m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication

# Database Configuration
spring.datasource.url=jdbc:mariadb://localhost:3306/test_advj
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=awsEE78

# Database Connection Pool Optimization for 2GB RAM
spring.datasource.hikari.maximum-pool-size=8
spring.datasource.hikari.minimum-idle=3
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# Hibernate JPA Configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.jdbc.batch_size=15
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Server Configuration optimized for 2GB RAM
server.port=3000
server.address=0.0.0.0
server.tomcat.threads.max=40
server.tomcat.threads.min-spare=8
server.tomcat.max-connections=4096
server.tomcat.accept-count=50
server.connection-timeout=30s

# Performance and Caching
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=300,expireAfterWrite=600s

# Logging Configuration (minimal for production)
logging.level.root=WARN
logging.level.com.dekhokaun.mindarobackend=INFO
logging.level.org.springframework.web=WARN
logging.level.org.hibernate.SQL=WARN

# Session Management
server.servlet.session.timeout=30m
server.servlet.session.cookie.http-only=true

# Compression
server.compression.enabled=true
server.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json
server.compression.min-response-size=1024

# Disable Swagger in production
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
EOF

# Start the service
print_status "Starting Mindaro Backend service..."
sudo systemctl enable mindaro-backend
sudo systemctl start mindaro-backend

# Wait for service to start
sleep 10

# Check service status
if sudo systemctl is-active --quiet mindaro-backend; then
    print_status "✅ Mindaro Backend service started successfully!"
    print_status "📊 Service status:"
    sudo systemctl status mindaro-backend --no-pager -l
    
    print_status "📋 Useful commands:"
    echo "  - View logs: sudo journalctl -u mindaro-backend -f"
    echo "  - Stop service: sudo systemctl stop mindaro-backend"
    echo "  - Restart service: sudo systemctl restart mindaro-backend"
    echo "  - Check status: sudo systemctl status mindaro-backend"
    
    print_status "🌐 Application should be available at: http://localhost:3000"
    
else
    print_error "❌ Failed to start Mindaro Backend service"
    sudo systemctl status mindaro-backend --no-pager -l
    exit 1
fi

print_status "🎉 Deployment completed successfully!" 