#!/bin/bash

# Conference Management System - Local Development Startup Script
# This script starts services locally without Docker

set -e

echo "🚀 Starting Conference Management System (Local Development)"
echo "=========================================================="

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.8+."
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install JDK 17."
    exit 1
fi

echo "✅ Maven and Java are installed"

# Build all modules
echo ""
echo "🔨 Building all modules..."
mvn clean install -DskipTests

# Create logs directory
mkdir -p logs

# Function to start a service
start_service() {
    local service_name=$1
    local port=$2
    echo "🚀 Starting $service_name on port $port..."
    cd $service_name
    mvn spring-boot:run > ../logs/${service_name}.log 2>&1 &
    echo $! > ../logs/${service_name}.pid
    cd ..
    sleep 5
}

# Start services in order
echo ""
echo "📦 Starting services..."

# Discovery Service first
start_service "discovery-service" 8761
echo "⏳ Waiting for Discovery Service to be ready (30 seconds)..."
sleep 30

# Technical services
start_service "gateway-service" 8888
start_service "auth-service" 9999

echo "⏳ Waiting for technical services (20 seconds)..."
sleep 20

# Functional services
start_service "keynote-service-command" 8081
start_service "keynote-service-query" 8082
start_service "conference-service-command" 8083
start_service "conference-service-query" 8084
start_service "analytics-service" 8085

echo ""
echo "✅ All services started!"
echo "=========================================================="
echo ""
echo "🌐 Service URLs:"
echo "   - Gateway:            http://localhost:8888"
echo "   - Eureka Dashboard:   http://localhost:8761"
echo ""
echo "📋 View logs:"
echo "   tail -f logs/[service-name].log"
echo ""
echo "🛑 Stop all services:"
echo "   ./stop-local.sh"
echo ""

# Save all PIDs
echo "PIDs saved to logs/*.pid"
