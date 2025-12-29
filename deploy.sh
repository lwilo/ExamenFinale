#!/bin/bash

# Conference Management System - Complete Deployment Script
# This script deploys the entire distributed microservices system

set -e

echo "🚀 Starting Conference Management System Deployment"
echo "=================================================="

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo "✅ Docker and Docker Compose are installed"

# Stop any existing containers
echo ""
echo "🛑 Stopping existing containers..."
docker-compose down -v 2>/dev/null || true

# Build all services
echo ""
echo "🔨 Building all services..."
docker-compose build

# Start infrastructure services first
echo ""
echo "🏗️  Starting infrastructure services..."
docker-compose up -d mysql axon-server zookeeper kafka keycloak

echo "⏳ Waiting for infrastructure to be ready (60 seconds)..."
sleep 60

# Start Discovery Service
echo ""
echo "🔍 Starting Discovery Service..."
docker-compose up -d discovery-service

echo "⏳ Waiting for Discovery Service (30 seconds)..."
sleep 30

# Start technical services
echo ""
echo "⚙️  Starting technical services..."
docker-compose up -d gateway-service auth-service

echo "⏳ Waiting for technical services (20 seconds)..."
sleep 20

# Start functional services
echo ""
echo "📊 Starting functional services..."
docker-compose up -d keynote-command-service keynote-query-service \
                    conference-command-service conference-query-service \
                    analytics-service

echo "⏳ Waiting for functional services (30 seconds)..."
sleep 30

# Start frontend
echo ""
echo "🎨 Starting frontend..."
docker-compose up -d frontend

echo ""
echo "✅ Deployment Complete!"
echo "=================================================="
echo ""
echo "🌐 Access the services:"
echo "   - Frontend:           http://localhost:3000"
echo "   - Gateway:            http://localhost:8888"
echo "   - Eureka Dashboard:   http://localhost:8761"
echo "   - Axon Server:        http://localhost:8124"
echo "   - Keycloak:           http://localhost:8080"
echo ""
echo "📝 Check service status:"
echo "   docker-compose ps"
echo ""
echo "📋 View logs:"
echo "   docker-compose logs -f [service-name]"
echo ""
echo "🛑 Stop all services:"
echo "   docker-compose down"
echo ""
