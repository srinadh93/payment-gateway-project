# Payment Gateway - Razorpay/Stripe Clone

A production-ready payment gateway system with merchant onboarding, payment order management, multi-method payment processing (UPI and Cards), and hosted checkout page.

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     Payment Gateway System                   │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────┐      ┌──────────────┐      ┌───────────┐ │
│  │   Dashboard  │      │   Checkout   │      │    API    │ │
│  │  (Port 3000) │      │  (Port 3001) │      │(Port 8000)│ │
│  │              │      │              │      │           │ │
│  │  - Login     │      │  - Payment   │◄─────┤  Express  │ │
│  │  - Stats     │      │    Forms     │      │  REST API │ │
│  │  - Txns      │      │  - Status    │      │           │ │
│  └──────┬───────┘      └──────┬───────┘      └─────┬─────┘ │
│         │                     │                     │        │
│         └─────────────────────┴─────────────────────┘        │
│                               │                              │
│                               ▼                              │
│                    ┌─────────────────────┐                  │
│                    │    PostgreSQL DB     │                  │
│                    │   (Port 5432)        │                  │
│                    │                      │                  │
│                    │  - merchants         │                  │
│                    │  - orders            │                  │
│                    │  - payments          │                  │
│                    └─────────────────────┘                  │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Docker Desktop installed and running
- Git

### Setup & Run

1. **Clone the repository**
   ```bash
   git clone <https://github.com/srinadh93/payment-gateway-project>
   cd payment-gateway
   ```

2. **Start all services**
   ```bash
   docker-compose up -d --build
   ```

3. **Verify all services are running**
   ```bash
   docker-compose ps
   ```

4. **Access the applications**
   - **API**: http://localhost:8000
   - **Dashboard**: http://localhost:3000
   - **Checkout**: http://localhost:3001

5. **Health Check**
   ```bash
   curl http://localhost:8000/health
   ```

### Test Merchant Credentials

The system automatically seeds a test merchant on startup:

- **Email**: test@example.com
- **API Key**: key_test_abc123
- **API Secret**: secret_test_xyz789

## 📡 API Documentation

### Base URL
```
http://localhost:8000
```

### Authentication
All API endpoints (except `/health` and test endpoints) require authentication via headers:

```http
X-Api-Key: key_test_abc123
X-Api-Secret: secret_test_xyz789
```

---

### 1. Health Check

**Endpoint**: `GET /health`

**Description**: Check API and database connectivity

**Authentication**: Not required

**Response** (200):
```json
{
  "status": "healthy",
  "database": "connected",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

### 2. Create Order

**Endpoint**: `POST /api/v1/orders`

**Description**: Create a payment order

**Authentication**: Required

**Request Body**:
```json
{
  "amount": 50000,
  "currency": "INR",
  "receipt": "receipt_123",
  "notes": {
    "customer_name": "John Doe"
  }
}
```

**Parameters**:
- `amount` (required): Amount in paise (minimum 100)
- `currency` (optional): Currency code, defaults to "INR"
- `receipt` (optional): Receipt identifier
- `notes` (optional): Additional metadata as JSON object

**Response** (201):
```json
{
  "id": "order_NXhj67fGH2jk9mPq",
  "merchant_id": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 50000,
  "currency": "INR",
  "receipt": "receipt_123",
  "notes": {
    "customer_name": "John Doe"
  },
  "status": "created",
  "created_at": "2024-01-15T10:30:00Z"
}
```

**Error Responses**:
- `400`: Validation error (amount < 100)
- `401`: Invalid API credentials

---

### 3. Get Order

**Endpoint**: `GET /api/v1/orders/{order_id}`

**Description**: Retrieve order details

**Authentication**: Required

**Response** (200):
```json
{
  "id": "order_NXhj67fGH2jk9mPq",
  "merchant_id": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 50000,
  "currency": "INR",
  "receipt": "receipt_123",
  "notes": {},
  "status": "created",
  "created_at": "2024-01-15T10:30:00Z",
  "updated_at": "2024-01-15T10:30:00Z"
}
```

**Error Responses**:
- `404`: Order not found
- `401`: Invalid API credentials

---

### 4. Create Payment

**Endpoint**: `POST /api/v1/payments`

**Description**: Process a payment for an order

**Authentication**: Required

**Request Body (UPI)**:
```json
{
  "order_id": "order_NXhj67fGH2jk9mPq",
  "method": "upi",
  "vpa": "user@paytm"
}
```

**Request Body (Card)**:
```json
{
  "order_id": "order_NXhj67fGH2jk9mPq",
  "method": "card",
  "card": {
    "number": "4111111111111111",
    "expiry_month": "12",
    "expiry_year": "2025",
    "cvv": "123",
    "holder_name": "John Doe"
  }
}
```

**Response (UPI)** (201):
```json
{
  "id": "pay_H8sK3jD9s2L1pQr",
  "order_id": "order_NXhj67fGH2jk9mPq",
  "amount": 50000,
  "currency": "INR",
  "method": "upi",
  "vpa": "user@paytm",
  "status": "processing",
  "created_at": "2024-01-15T10:31:00Z"
}
```

**Response (Card)** (201):
```json
{
  "id": "pay_H8sK3jD9s2L1pQr",
  "order_id": "order_NXhj67fGH2jk9mPq",
  "amount": 50000,
  "currency": "INR",
  "method": "card",
  "card_network": "visa",
  "card_last4": "1111",
  "status": "processing",
  "created_at": "2024-01-15T10:31:00Z"
}
```

**Payment Status Flow**:
- Payment created with status `processing`
- After 5-10 seconds, status updates to `success` (90% UPI, 95% Card) or `failed`

**Error Responses**:
- `400`: Invalid VPA format, invalid card, expired card
- `404`: Order not found
- `401`: Invalid API credentials

---

### 5. Get Payment

**Endpoint**: `GET /api/v1/payments/{payment_id}`

**Description**: Retrieve payment details

**Authentication**: Required

**Response** (200):
```json
{
  "id": "pay_H8sK3jD9s2L1pQr",
  "order_id": "order_NXhj67fGH2jk9mPq",
  "amount": 50000,
  "currency": "INR",
  "method": "upi",
  "vpa": "user@paytm",
  "status": "success",
  "created_at": "2024-01-15T10:31:00Z",
  "updated_at": "2024-01-15T10:31:10Z"
}
```

**Error Responses**:
- `404`: Payment not found
- `401`: Invalid API credentials

---

### 6. Get Test Merchant

**Endpoint**: `GET /api/v1/test/merchant`

**Description**: Retrieve test merchant details (for evaluation)

**Authentication**: Not required

**Response** (200):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "test@example.com",
  "api_key": "key_test_abc123",
  "seeded": true
}
```

---

### Error Codes

| Code | Description |
|------|-------------|
| `AUTHENTICATION_ERROR` | Invalid API credentials |
| `BAD_REQUEST_ERROR` | Validation errors |
| `NOT_FOUND_ERROR` | Resource not found |
| `PAYMENT_FAILED` | Payment processing failed |
| `INVALID_VPA` | VPA format invalid |
| `INVALID_CARD` | Card validation failed |
| `EXPIRED_CARD` | Card expiry date invalid |

## 🖥️ Frontend Applications

### Dashboard (Port 3000)

**Login Page** (`/login`)
- Email: test@example.com
- Password: Any password (not validated in Deliverable 1)

**Dashboard Home** (`/dashboard`)
- Displays API credentials
- Shows transaction statistics
- Total transactions count
- Total amount processed
- Success rate percentage

**Transactions Page** (`/dashboard/transactions`)
- Lists all payments
- Shows payment ID, order ID, amount, method, status, and timestamp
- Real-time data from database

### Checkout Page (Port 3001)

**Checkout Flow** (`/checkout?order_id=xxx`)
- Fetches order details
- Displays order amount and ID
- Payment method selection (UPI/Card)
- Payment form submission
- Processing state with spinner
- Status polling every 2 seconds
- Success/Failure pages

## 🗄️ Database Schema

### Merchants Table
```sql
id              UUID PRIMARY KEY (auto-generated)
name            VARCHAR(255) NOT NULL
email           VARCHAR(255) UNIQUE NOT NULL
api_key         VARCHAR(64) UNIQUE NOT NULL
api_secret      VARCHAR(64) NOT NULL
webhook_url     TEXT
is_active       BOOLEAN DEFAULT TRUE
created_at      TIMESTAMP DEFAULT NOW()
updated_at      TIMESTAMP DEFAULT NOW()
```

### Orders Table
```sql
id              VARCHAR(64) PRIMARY KEY (format: order_XXXXXXXXXXXXXXXX)
merchant_id     UUID REFERENCES merchants(id)
amount          INTEGER NOT NULL (minimum 100 paise)
currency        VARCHAR(3) DEFAULT 'INR'
receipt         VARCHAR(255)
notes           JSONB
status          VARCHAR(20) DEFAULT 'created'
created_at      TIMESTAMP DEFAULT NOW()
updated_at      TIMESTAMP DEFAULT NOW()

INDEX ON merchant_id
```

### Payments Table
```sql
id                  VARCHAR(64) PRIMARY KEY (format: pay_XXXXXXXXXXXXXXXX)
order_id            VARCHAR(64) REFERENCES orders(id)
merchant_id         UUID REFERENCES merchants(id)
amount              INTEGER NOT NULL
currency            VARCHAR(3) DEFAULT 'INR'
method              VARCHAR(20) NOT NULL (upi/card)
status              VARCHAR(20) DEFAULT 'processing'
vpa                 VARCHAR(255) (UPI only)
card_network        VARCHAR(20) (Card only)
card_last4          VARCHAR(4) (Card only)
error_code          VARCHAR(50)
error_description   TEXT
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()

INDEX ON order_id
INDEX ON status
```

## 🔒 Payment Validation

### UPI VPA Validation
- Pattern: `^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+$`
- Valid: `user@paytm`, `john.doe@okhdfcbank`, `user_123@phonepe`
- Invalid: `user @paytm`, `@paytm`, `user@@bank`

### Card Validation (Luhn Algorithm)
1. Remove spaces and dashes
2. Verify length between 13-19 digits
3. Apply Luhn algorithm:
   - Start from rightmost digit
   - Double every second digit from right
   - If doubled digit > 9, subtract 9
   - Sum all digits
   - Valid if sum % 10 === 0

### Card Network Detection
- **Visa**: Starts with `4`
- **Mastercard**: Starts with `51-55`
- **Amex**: Starts with `34` or `37`
- **RuPay**: Starts with `60`, `65`, or `81-89`

### Expiry Validation
- Month: 1-12
- Year: 2-digit (20XX) or 4-digit format
- Must be current month or future

## 🧪 Testing

### Test Mode Configuration
Set environment variables for deterministic testing:

```bash
TEST_MODE=true
TEST_PAYMENT_SUCCESS=true
TEST_PROCESSING_DELAY=1000
```

### Testing Payment Flow

1. **Create an order**:
```bash
curl -X POST http://localhost:8000/api/v1/orders \
  -H "X-Api-Key: key_test_abc123" \
  -H "X-Api-Secret: secret_test_xyz789" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50000,
    "currency": "INR",
    "receipt": "test_receipt_001"
  }'
```

2. **Open checkout page**:
```
http://localhost:3001/checkout?order_id=<order_id_from_step_1>
```

3. **Test UPI Payment**:
   - Select UPI method
   - Enter VPA: `test@paytm`
   - Submit payment
   - Wait for status update

4. **Test Card Payment**:
   - Select Card method
   - Enter card number: `4111111111111111`
   - Expiry: `12/25`
   - CVV: `123`
   - Name: `Test User`
   - Submit payment
   - Wait for status update

### Test Cards

| Card Number | Network | Expected Result |
|-------------|---------|-----------------|
| 4111111111111111 | Visa | Valid |
| 5555555555554444 | Mastercard | Valid |
| 378282246310005 | Amex | Valid |
| 6011111111111117 | RuPay | Valid |
| 1234567890123456 | Unknown | Invalid (Luhn) |

## 🛠️ Technology Stack

### Backend
- **Runtime**: Node.js 18
- **Framework**: Express.js
- **Database**: PostgreSQL 15
- **ORM**: pg (node-postgres)

### Frontend
- **Framework**: React 18
- **Build Tool**: Vite
- **Routing**: React Router
- **Server**: Nginx (production)

### DevOps
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **CI/CD**: Ready for GitHub Actions

## 📁 Project Structure

```
payment-gateway/
├── docker-compose.yml          # Service orchestration
├── README.md                   # This file
├── .env.example               # Environment variables template
│
├── backend/                   # API Server (Port 8000)
│   ├── Dockerfile
│   ├── package.json
│   └── src/
│       ├── server.js          # Express app entry point
│       ├── config/
│       │   └── db.js          # PostgreSQL connection
│       ├── controllers/       # Request handlers
│       ├── middleware/
│       │   └── auth.js        # API key authentication
│       ├── models/
│       │   ├── init.js        # DB initialization
│       │   └── schema.sql     # Database schema
│       ├── routes/
│       │   ├── orderRoutes.js
│       │   ├── paymentRoutes.js
│       │   └── testRoutes.js
│       ├── services/
│       │   └── seedService.js # Test merchant seeding
│       └── utils/
│           └── validation.js  # Payment validation logic
│
├── frontend/dashboard/        # Merchant Dashboard (Port 3000)
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── package.json
│   └── src/
│       ├── App.jsx
│       ├── main.jsx
│       ├── pages/
│       │   ├── Login.jsx
│       │   ├── Dashboard.jsx
│       │   └── Transactions.jsx
│       └── styles/
│
└── checkout/                  # Checkout Page (Port 3001)
    ├── Dockerfile
    ├── package.json
    └── src/
        ├── App.jsx
        ├── main.jsx
        ├── pages/
        │   ├── Checkout.jsx
        │   ├── Success.jsx
        │   └── Failure.jsx
        └── styles/
```

## 🔧 Development

### Local Development (without Docker)

1. **Start PostgreSQL**:
```bash
docker run -d \
  --name payment-gateway-db \
  -e POSTGRES_DB=payment_gateway \
  -e POSTGRES_USER=gateway_user \
  -e POSTGRES_PASSWORD=gateway_pass \
  -p 5432:5432 \
  postgres:15-alpine
```

2. **Backend**:
```bash
cd backend
npm install
npm run dev
```

3. **Dashboard**:
```bash
cd frontend/dashboard
npm install
npm run dev
```

4. **Checkout**:
```bash
cd checkout
npm install
npm run dev
```

### Environment Variables

Copy `.env.example` and configure:

```bash
# Backend
DATABASE_URL=postgresql://gateway_user:gateway_pass@localhost:5432/payment_gateway
PORT=8000

# Test Mode (optional)
TEST_MODE=false
TEST_PAYMENT_SUCCESS=true
TEST_PROCESSING_DELAY=1000
```

## 🚢 Deployment

### Production Considerations

1. **Security**:
   - Use strong database passwords
   - Enable SSL/TLS for database connections
   - Implement rate limiting on API endpoints
   - Add CORS configuration for production domains
   - Use secrets management (AWS Secrets Manager, HashiCorp Vault)

2. **Scalability**:
   - Add Redis for caching and session management
   - Implement connection pooling
   - Use CDN for frontend assets
   - Add load balancer for API instances

3. **Monitoring**:
   - Add logging (Winston, Bunyan)
   - Implement error tracking (Sentry)
   - Set up APM (New Relic, Datadog)
   - Add health check monitoring

4. **Database**:
   - Enable automated backups
   - Set up read replicas
   - Implement database connection pooling
   - Add database migration management

## 📊 Performance Metrics

- **Payment Processing Time**: 5-10 seconds (simulated)
- **Success Rates**: 
  - UPI: 90%
  - Card: 95%
- **API Response Time**: < 100ms (excluding payment processing)
- **Database Query Time**: < 50ms average

## 🐛 Troubleshooting

### Services won't start
```bash
# Check Docker daemon is running
docker info

# Check for port conflicts
netstat -ano | findstr "8000 3000 3001 5432"

# View service logs
docker-compose logs api
docker-compose logs postgres
```

### Database connection issues
```bash
# Check PostgreSQL is ready
docker-compose exec postgres pg_isready -U gateway_user

# Connect to database
docker-compose exec postgres psql -U gateway_user -d payment_gateway
```

### API returning 500 errors
```bash
# Check API logs
docker-compose logs -f api

# Restart API service
docker-compose restart api
```
