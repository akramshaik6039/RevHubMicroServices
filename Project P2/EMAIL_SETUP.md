# Email Configuration Setup

## Gmail Setup (Recommended)

### 1. Enable 2-Factor Authentication
1. Go to your Google Account: https://myaccount.google.com/
2. Navigate to Security
3. Enable 2-Step Verification

### 2. Generate App Password
1. Go to: https://myaccount.google.com/apppasswords
2. Select "Mail" and "Other (Custom name)"
3. Enter "RevHub" as the name
4. Click "Generate"
5. Copy the 16-character password

### 3. Configure User Service

Update `backend/user-service/src/main/resources/application.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-16-char-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

OR set environment variables:

```bash
# Windows
set MAIL_USERNAME=your-email@gmail.com
set MAIL_PASSWORD=your-16-char-app-password

# Linux/Mac
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-16-char-app-password
```

## Alternative: Mailtrap (For Testing)

### 1. Create Mailtrap Account
1. Go to: https://mailtrap.io/
2. Sign up for free account
3. Go to Email Testing → Inboxes → My Inbox

### 2. Get SMTP Credentials
Copy the credentials from Mailtrap dashboard

### 3. Configure User Service

```yaml
spring:
  mail:
    host: smtp.mailtrap.io
    port: 2525
    username: your-mailtrap-username
    password: your-mailtrap-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

## Testing Email Functionality

### 1. Register New User
```bash
POST http://localhost:8080/api/auth/register
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "fullName": "Test User"
}
```

Response:
```json
{
  "message": "Registration successful. Please check your email for OTP verification."
}
```

### 2. Check Email for OTP
- Check your email inbox
- You should receive a 6-digit OTP
- OTP expires in 10 minutes

### 3. Verify OTP
```bash
POST http://localhost:8080/api/auth/verify-otp
{
  "email": "test@example.com",
  "otp": "123456"
}
```

Response:
```json
{
  "token": "jwt-token-here",
  "userId": 1,
  "username": "testuser"
}
```

### 4. Resend OTP (if expired)
```bash
POST http://localhost:8080/api/auth/resend-otp
{
  "email": "test@example.com"
}
```

### 5. Forgot Password
```bash
POST http://localhost:8080/api/auth/forgot-password
{
  "email": "test@example.com"
}
```

Response:
```json
{
  "message": "Password reset token sent to your email"
}
```

### 6. Reset Password
```bash
POST http://localhost:8080/api/auth/reset-password
{
  "email": "test@example.com",
  "token": "uuid-token-from-email",
  "newPassword": "newpassword123"
}
```

## Frontend Flow

### Registration Flow
1. User fills registration form → `/auth/register`
2. Submits form → Backend sends OTP to email
3. Redirected to → `/auth/verify-otp`
4. Enters OTP → Verified and logged in
5. Redirected to → `/feed`

### Forgot Password Flow
1. User clicks "Forgot Password?" on login page
2. Redirected to → `/auth/forgot-password`
3. Enters email → Backend sends reset token
4. Redirected to → `/auth/reset-password`
5. Enters token and new password → Password reset
6. Redirected to → `/auth/login`

## Troubleshooting

### Email Not Sending
1. Check SMTP credentials are correct
2. Verify 2FA is enabled (for Gmail)
3. Check firewall/antivirus blocking port 587
4. Check application logs for errors

### OTP Not Received
1. Check spam/junk folder
2. Verify email address is correct
3. Try resending OTP
4. Check OTP hasn't expired (10 min validity)

### Reset Token Issues
1. Token expires in 30 minutes
2. Request new token if expired
3. Ensure email matches registered account

## Security Notes

1. **Never commit credentials** - Use environment variables
2. **OTP expires in 10 minutes** - For security
3. **Reset token expires in 30 minutes** - For security
4. **Passwords are hashed** - Using BCrypt
5. **Email verification required** - Before login

## Production Recommendations

1. Use a dedicated email service (SendGrid, AWS SES, Mailgun)
2. Implement rate limiting on OTP/reset endpoints
3. Add CAPTCHA to prevent abuse
4. Log all authentication attempts
5. Monitor failed login attempts
6. Implement account lockout after multiple failures
