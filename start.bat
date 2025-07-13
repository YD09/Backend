@echo off
echo 🚀 Starting TradeCraft Simulator...

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Java is not installed. Please install Java 17 or higher.
    pause
    exit /b 1
)

REM Check if Node.js is installed
node --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Node.js is not installed. Please install Node.js 16 or higher.
    pause
    exit /b 1
)

echo 📦 Installing backend dependencies...
cd Backend
call mvn clean install -DskipTests

echo 🔧 Starting backend server...
start "Backend Server" mvn spring-boot:run

echo ⏳ Waiting for backend to start...
timeout /t 10 /nobreak >nul

echo 📦 Installing frontend dependencies...
cd ..\test\vite-project
call npm install

echo 🎨 Starting frontend development server...
start "Frontend Server" npm run dev

echo ✅ TradeCraft Simulator is starting...
echo 🌐 Backend: http://localhost:8080
echo 🎨 Frontend: http://localhost:5173
echo.
echo Press any key to stop all services...
pause >nul

echo 🛑 Stopping services...
taskkill /f /im java.exe >nul 2>&1
taskkill /f /im node.exe >nul 2>&1 