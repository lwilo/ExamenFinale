#!/bin/bash

# Stop all locally running services

echo "🛑 Stopping all services..."

if [ -d "logs" ]; then
    for pid_file in logs/*.pid; do
        if [ -f "$pid_file" ]; then
            pid=$(cat "$pid_file")
            if ps -p $pid > /dev/null 2>&1; then
                echo "Stopping $(basename $pid_file .pid)..."
                kill $pid
            fi
            rm "$pid_file"
        fi
    done
    echo "✅ All services stopped"
else
    echo "No running services found"
fi
