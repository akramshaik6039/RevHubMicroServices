# Micro-Frontend Setup Guide

## Architecture

### Backend (✅ Confirmed Individual Spring Boot Projects)
- config-server (Port 8888)
- api-gateway (Port 8080)
- user-service (Port 8081)
- post-service (Port 8082)
- feed-service (Port 8083)
- follow-service (Port 8084)
- notification-service (Port 8085)
- chat-service (Port 8086)
- search-service (Port 8087)

Each service has its own `pom.xml` and runs independently.

### Frontend (Module Federation Micro-Frontends)
- **shell** (Port 4200) - Host application
- **mfe-auth** (Port 4201) - Authentication micro-frontend
- **mfe-feed** (Port 4202) - Feed micro-frontend
- **mfe-profile** (Port 4203) - Profile micro-frontend
- **mfe-chat** (Port 4204) - Chat micro-frontend
- **mfe-notifications** (Port 4205) - Notifications micro-frontend

## Setup Instructions

### 1. Install Dependencies

```bash
# Shell
cd frontend/shell
npm install

# MFE-Auth
cd frontend/mfe-auth
npm install
```

### 2. Start Micro-Frontends

```bash
# Terminal 1 - Shell (Host)
cd frontend/shell
npm start
# Runs on http://localhost:4200

# Terminal 2 - Auth MFE
cd frontend/mfe-auth
npm start
# Runs on http://localhost:4201
```

### 3. How It Works

- **Shell** loads remote micro-frontends dynamically
- **Module Federation** shares Angular dependencies
- Each MFE is an independent Angular 18 standalone app
- MFEs expose routes that shell consumes

## Current Status

✅ **Backend**: All 9 services are individual Spring Boot projects
✅ **MFE-Auth**: Created as standalone Angular 18 app with Module Federation
⏳ **Other MFEs**: Need to be created (feed, profile, chat, notifications)

## Benefits

1. **Independent Development**: Each team works on separate MFE
2. **Independent Deployment**: Deploy MFEs without affecting others
3. **Technology Flexibility**: Each MFE can use different versions
4. **Scalability**: Load MFEs on demand
5. **Team Autonomy**: Clear boundaries between features

## Next Steps

To complete the micro-frontend architecture:

1. Create mfe-feed (copy feed components from shell)
2. Create mfe-profile (copy profile components from shell)
3. Create mfe-chat (copy chat components from shell)
4. Create mfe-notifications (copy notifications components from shell)
5. Update shell to load all remote MFEs
6. Remove feature modules from shell (keep only routing)

## Note

The current implementation has:
- ✅ Backend: True microservices (9 independent projects)
- ✅ MFE-Auth: Standalone micro-frontend
- ⏳ Other features: Still in shell (can be extracted to MFEs)

This hybrid approach allows gradual migration to full micro-frontend architecture.
