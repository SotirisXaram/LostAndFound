# Lost and Found Management System

A desktop application for managing lost and found items with database management and web dashboard.

## Features

- User authentication with secure login
- Record management for lost and found items
- Return tracking and status updates
- Image support with automatic compression
- Statistics dashboard and reports
- Export to Excel, CSV, and PDF formats
- Automatic database backups
- Web dashboard for remote monitoring
- QR code generation for public record access

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Installation

1. Clone the repository
2. Build the application:
   ```bash
   mvn clean compile
   ```
3. Run the application:
   ```bash
   mvn javafx:run
   ```

## Default Login

- Username: `123456`
- Password: `123456`

## Usage

### Main Features
- Add new records with item details and photos
- Search and filter records
- Process item returns
- View statistics and reports
- Export data in multiple formats

### Web Dashboard
Access at `http://localhost:8080/admin`
- Username: `admin`
- Password: `admin123`

### QR Code Access
Records can be accessed publicly via QR codes at `http://localhost:8080/public/record/{uid}`

## Remote Access Setup

### Port Forwarding Configuration

To access the web dashboard from outside your local network, configure port forwarding on your router:

#### Step 1: Find Your Router's IP Address
- Windows: Open Command Prompt and run `ipconfig`
- Mac/Linux: Open Terminal and run `ifconfig` or `ip route`
- Look for "Default Gateway" (usually 192.168.1.1 or 192.168.0.1)

#### Step 2: Access Router Admin Panel
1. Open web browser and go to your router's IP address
2. Login with admin credentials (check router label or manual)
3. Common default credentials:
   - Username: `admin`, Password: `admin`
   - Username: `admin`, Password: `password`
   - Username: `admin`, Password: (blank)

#### Step 3: Configure Port Forwarding
1. Navigate to "Port Forwarding" or "Virtual Server" section
2. Create new port forwarding rule:
   - **Service Name**: Lost and Found
   - **External Port**: 8080
   - **Internal Port**: 8080
   - **Protocol**: TCP
   - **Internal IP**: Your computer's local IP address
3. Save and apply settings

#### Step 4: Find Your Public IP
- Visit `whatismyip.com` to get your public IP address
- Access the application using: `http://YOUR_PUBLIC_IP:8080/admin`

#### Common Router Brands
- **Netgear**: Advanced > Port Forwarding
- **Linksys**: Smart Wi-Fi Tools > Port Forwarding
- **TP-Link**: Advanced > NAT Forwarding > Port Forwarding
- **ASUS**: Advanced Settings > WAN > Virtual Server
- **D-Link**: Advanced > Port Forwarding

#### Troubleshooting
- Ensure Windows Firewall allows port 8080
- Check if your ISP blocks port 8080
- Try different external port (8081, 8082, etc.)
- Restart router after configuration

## Database

Uses SQLite database stored in `./lostAndFound.db`

## Configuration

- Database: `./lostAndFound.db`
- Images: `Desktop/export_data/images/`
- Backups: `data/backups/`
- Web server: Port 8080

## Building

Create executable JAR:
```bash
mvn clean package
```

## Support

For issues or questions, check the application logs or create an issue in the repository.

## Version 2.0.1

- Modern UI design
- Web dashboard
- QR code functionality
- Image management
- Automatic backups
- Export capabilities