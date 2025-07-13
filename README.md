# TradeCraft Simulator

A comprehensive virtual trading simulator for Indian retail traders, featuring real-time market data, strategy testing, and portfolio management.

## 🚀 Features

### Core Features
- **Mobile Number Authentication** - Secure OTP-based login
- **Real-time Market Data** - Live quotes and option chain data
- **Virtual Trading Engine** - Simulate trades with ₹10,00,000 virtual capital
- **Watchlist Management** - Add/remove favorite instruments
- **Portfolio Tracking** - Real-time P&L and position monitoring
- **Trade History** - Complete trading history with analytics
- **Strategy Builder** - Create and test trading strategies
- **Order Management** - Track active and completed orders

### Technical Stack
- **Backend**: Spring Boot (Java 17)
- **Frontend**: React.js with Vite
- **Database**: MySQL
- **Authentication**: JWT with OTP
- **Real-time**: WebSocket
- **Styling**: Tailwind CSS
- **Charts**: Lightweight Charts

## 📋 Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- MySQL 8.0 or higher
- Maven 3.6+

## 🛠️ Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd TradeCraft-Simulator
```

### 2. Backend Setup

#### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE trading_sim;
```

2. Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/trading_sim
spring.datasource.username=your_username
spring.datasource.password=your_password
```

#### Build and Run Backend
```bash
# Navigate to backend directory
cd Backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd test/vite-project

# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend will start on `http://localhost:5173`

## 🔧 Configuration

### FYERS API Configuration
Update the FYERS API credentials in `src/main/resources/application.properties`:

```properties
fyers.client_id=your_client_id
fyers.secret_key=your_secret_key
fyers.redirect_uri=http://127.0.0.1:5000/callback
```

### JWT Configuration
```properties
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

## 📱 Usage

### 1. Authentication
- Open the application in your browser
- Enter your mobile number
- Verify with OTP sent to your phone
- Access the trading dashboard

### 2. Trading
- Navigate to the Home page to view live market data
- Use the Option Chain to place trades
- Monitor your portfolio in real-time
- Add instruments to your watchlist

### 3. Features
- **Home**: Live market data and option chain
- **Watchlist**: Manage favorite instruments
- **Strategy**: Build and test trading strategies
- **Orders**: View and manage orders
- **Portfolio**: Track P&L and positions
- **Trade History**: View complete trading history
- **Profile**: Manage account settings

## 🏗️ Project Structure

```
TradeCraft-Simulator/
├── Backend/
│   ├── src/main/java/com/tradesim/
│   │   ├── controller/     # REST API controllers
│   │   ├── service/        # Business logic
│   │   ├── model/          # Data models
│   │   ├── repository/     # Data access layer
│   │   ├── config/         # Configuration classes
│   │   ├── security/       # Authentication & authorization
│   │   └── util/           # Utility classes
│   └── src/main/resources/
│       └── application.properties
├── test/vite-project/      # Frontend React application
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── context/        # React context
│   │   └── App.jsx         # Main application
│   └── package.json
└── README.md
```

## 🔒 Security Features

- JWT-based authentication
- OTP verification for mobile login
- CORS configuration for frontend-backend communication
- Input validation and sanitization
- Secure password hashing (BCrypt)

## 📊 API Endpoints

### Authentication
- `POST /api/auth/send-otp` - Send OTP
- `POST /api/auth/verify-otp` - Verify OTP and login
- `POST /api/auth/register` - Register new user

### Trading
- `POST /api/trade/place` - Place a trade
- `POST /api/trade/close` - Close a trade
- `GET /api/trade/open` - Get open trades
- `GET /api/trade/history` - Get trade history
- `GET /api/trade/pnl` - Get P&L summary

### Watchlist
- `GET /api/watchlist` - Get user watchlist
- `POST /api/watchlist/add` - Add to watchlist
- `DELETE /api/watchlist/remove` - Remove from watchlist

### Portfolio
- `GET /api/wallet/balance` - Get wallet balance
- `POST /api/wallet/update` - Update wallet

## 🚀 Deployment

### Backend Deployment
1. Build the JAR file:
```bash
mvn clean package
```

2. Deploy to your preferred platform (Heroku, AWS, etc.)

### Frontend Deployment
1. Build the production version:
```bash
npm run build
```

2. Deploy the `dist` folder to your hosting platform

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team

## 🔮 Roadmap

- [ ] Real-time chart integration
- [ ] Advanced strategy builder
- [ ] Paper trading competitions
- [ ] Social trading features
- [ ] Mobile app development
- [ ] Advanced analytics and reporting
- [ ] Integration with more brokers
- [ ] AI-powered trading suggestions

---

**Note**: This is a virtual trading simulator for educational purposes. No real money is involved in trading activities. 