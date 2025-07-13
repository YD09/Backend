#!/bin/bash

echo "🚀 Starting TradeCraft Simulator..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 16 or higher."
    exit 1
fi

# Check if MySQL is running
if ! mysqladmin ping -h localhost --silent; then
    echo "⚠️  Warning: MySQL might not be running. Please ensure MySQL is started."
fi

echo "📦 Installing backend dependencies..."
cd Backend
mvn clean install -DskipTests

echo "🔧 Starting backend server..."
mvn spring-boot:run &
BACKEND_PID=$!

echo "⏳ Waiting for backend to start..."
sleep 10

echo "📦 Installing frontend dependencies..."
cd ../test/vite-project
npm install

echo "🎨 Starting frontend development server..."
npm run dev &
FRONTEND_PID=$!

echo "✅ TradeCraft Simulator is starting..."
echo "🌐 Backend: http://localhost:8080"
echo "🎨 Frontend: http://localhost:5173"
echo ""
echo "Press Ctrl+C to stop all services"

# Wait for user to stop
trap "echo '🛑 Stopping services...'; kill $BACKEND_PID $FRONTEND_PID; exit" INT
wait 