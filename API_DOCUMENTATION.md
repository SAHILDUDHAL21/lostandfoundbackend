# 📘 Lost & Found REST API Documentation

**Base URL**: `http://localhost:8080/api`  
**Database**: Aiven Cloud PostgreSQL (`pg-1771b631-lostandfound.b.aivencloud.com:24159/defaultdb`)  
**Authentication**: JWT Bearer Token (`Authorization: Bearer <token>`)

---

## 🔑 Authentication Endpoints (`/api/auth`)

### 1. User Registration
Creates a new user account and returns a signed 24-hour JWT token.

- **Method**: `POST`
- **Path**: `/api/auth/register`
- **Request Body**:
  ```json
  {
    "fullName": "Priya Sharma",
    "email": "priya@example.com",
    "password": "password123",
    "phone": "+91 9876543210",
    "avatar": "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg"
  }
  ```
- **Response (200 OK)**:
  ```json
  {
    "success": true,
    "message": "Registration successful!",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcml5YUBleGFtcGxlLmNvbSIsImlhdCI6MTY5...",
    "user": {
      "id": 1,
      "fullName": "Priya Sharma",
      "email": "priya@example.com",
      "phone": "+91 9876543210",
      "avatar": "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg",
      "role": null
    }
  }
  ```

---

### 2. User Login
Authenticates user credentials and issues a signed JWT token.

- **Method**: `POST`
- **Path**: `/api/auth/login`
- **Request Body**:
  ```json
  {
    "email": "priya@example.com",
    "password": "password123"
  }
  ```
- **Response (200 OK)**:
  ```json
  {
    "success": true,
    "message": "Login successful!",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcml5YUBleGFtcGxlLmNvbSIsImlhdCI6MTY5...",
    "user": {
      "id": 1,
      "fullName": "Priya Sharma",
      "email": "priya@example.com"
    }
  }
  ```

---

### 3. Verify JWT Token
Validates the JWT token passed in the `Authorization` header.

- **Method**: `GET`
- **Path**: `/api/auth/verify`
- **Headers**:
  ```http
  Authorization: Bearer <token>
  ```
- **Response (200 OK)**:
  ```json
  {
    "success": true,
    "message": "Token valid!",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "fullName": "Priya Sharma",
      "email": "priya@example.com"
    }
  }
  ```

---

## 📦 Found Items Endpoints (`/api/items`)

### 1. Get All Found Items
Retrieves all reported found items ordered by latest ID, including auto-populated nested public ownership claims and comments.

- **Method**: `GET`
- **Path**: `/api/items`
- **Response (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "type": "FOUND",
      "title": "Titan Leather Wallet & Delhi Metro Smart Card",
      "description": "Found a brown bi-fold Titan leather wallet containing a Delhi Metro Smart Card...",
      "location": "Connaught Place Metro Station, New Delhi",
      "timeAgo": "2 hours ago",
      "image": "https://images.pexels.com/photos/915915/pexels-photo-915915.jpeg",
      "finderName": "Priya Sharma",
      "finderHandle": "@priya_s",
      "finderAvatar": "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg",
      "createdAt": "2026-08-31T15:58:15",
      "claims": [
        {
          "id": 1,
          "itemId": 1,
          "user": "Aarav Mehta",
          "avatar": "https://images.pexels.com/photos/1681010/pexels-photo-1681010.jpeg",
          "time": "1 hour ago",
          "status": "PENDING_VERIFICATION",
          "note": "Lost my brown Titan wallet near Gate 2 at 2 PM! Has my Delhi Metro pass ending in #4092."
        }
      ],
      "comments": [
        {
          "id": 1,
          "itemId": 1,
          "user": "Rajesh Kumar",
          "avatar": "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg",
          "time": "1h 45m ago",
          "text": "Did you hand it over to Delhi Metro CISF security desk?"
        }
      ]
    }
  ]
  ```

---

### 2. Get Item by ID
- **Method**: `GET`
- **Path**: `/api/items/{id}`
- **Response (200 OK)**: Returns the matching item object with claims and comments.

---

### 3. Report / Create Found Item
*(Note: Only FOUND items can be reported on this network)*

- **Method**: `POST`
- **Path**: `/api/items`
- **Headers**: `Authorization: Bearer <token>` (Optional)
- **Request Body**:
  ```json
  {
    "title": "Found Royal Enfield Bike Keys",
    "description": "Found a set of keys on a brass ring with a Royal Enfield key fob near Bandra Promenade.",
    "location": "Bandra West Promenade, Mumbai",
    "image": "https://images.pexels.com/photos/101808/pexels-photo-101808.jpeg",
    "finderName": "Vikramaditya Reddy",
    "finderHandle": "@vikram_r",
    "finderAvatar": "https://images.pexels.com/photos/1516680/pexels-photo-1516680.jpeg"
  }
  ```
- **Response (200 OK)**: Returns the saved `Item` entity object.

---

### 4. Update Item
- **Method**: `PUT`
- **Path**: `/api/items/{id}`
- **Request Body**: Updated item properties.

---

### 5. Delete Item
- **Method**: `DELETE`
- **Path**: `/api/items/{id}`
- **Response (244 No Content / 204)**: Item deleted successfully.

---

## 📜 Ownership Claims Endpoints (`/api/claims`)

### 1. Get Claims by Item ID
- **Method**: `GET`
- **Path**: `/api/claims/item/{itemId}`
- **Response (200 OK)**: Array of public ownership claim objects.

---

### 2. Submit Ownership Claim Proof
Allows a user to submit ownership proof for a found item.

- **Method**: `POST`
- **Path**: `/api/claims`
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
  ```json
  {
    "itemId": 1,
    "user": "Aarav Mehta",
    "avatar": "https://images.pexels.com/photos/1681010/pexels-photo-1681010.jpeg",
    "time": "Just now",
    "status": "PENDING_VERIFICATION",
    "note": "Dropped my wallet near Gate 2. Contains Metro Card #4092."
  }
  ```
- **Response (200 OK)**: Returns saved `Claim` entity.

---

### 3. Update Claim Status
Update a claim's status (e.g. `VERIFIED`, `REJECTED`, `UNDER_REVIEW`).

- **Method**: `PUT`
- **Path**: `/api/claims/{id}/status?status=VERIFIED`
- **Response (200 OK)**: Returns updated `Claim` object.

---

## 💬 Community Comments Endpoints (`/api/comments`)

### 1. Get Comments by Item ID
- **Method**: `GET`
- **Path**: `/api/comments/item/{itemId}`
- **Response (200 OK)**: Array of comment objects.

---

### 2. Post a Comment
- **Method**: `POST`
- **Path**: `/api/comments`
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
  ```json
  {
    "itemId": 1,
    "user": "Priya Sharma",
    "avatar": "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg",
    "time": "Just now",
    "text": "Holding it safely! Please submit claim proof above with exact card details."
  }
  ```
- **Response (200 OK)**: Returns saved `Comment` object.

---

## 🚨 Scammer Leaderboard Endpoints (`/api/scammers`)

### 1. Get Scammer Leaderboard
Retrieves public profile list of users flagged for submitting fake ownership claims.

- **Method**: `GET`
- **Path**: `/api/scammers`
- **Response (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "fullName": "Rohan Verma",
      "username": "@rohan_v99",
      "avatar": "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg",
      "failedClaimsCount": 4,
      "reason": "Fake Amazon Invoice & Unmatched Serial #",
      "status": "Flagged Scammer 🚨"
    },
    {
      "id": 2,
      "fullName": "Karan Malhotra",
      "username": "@karan_m",
      "avatar": "https://images.pexels.com/photos/1222271/pexels-photo-1222271.jpeg",
      "failedClaimsCount": 3,
      "reason": "Incorrect Wallet Contents & Brand",
      "status": "Suspicious Claimant ⚠️"
    }
  ]
  ```

---

### 2. Flag / Add Scammer Profile
- **Method**: `POST`
- **Path**: `/api/scammers`
- **Request Body**: Scammer profile details.
- **Response (200 OK)**: Returns saved `ScammerProfile` entity.

