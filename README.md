# 🎯 Lost and Found Management System

A comprehensive JavaFX-based desktop application for managing lost and found items with modern UI, database management, and web dashboard capabilities.

## ✨ Features

### 🏠 **Main Features**
- **User Authentication** - Secure login with encrypted passwords
- **Record Management** - Add, edit, delete, and search lost/found items
- **Return Management** - Track item returns and status updates
- **Image Support** - Attach photos to records with automatic compression
- **Statistics Dashboard** - View comprehensive statistics and reports
- **Export Functionality** - Export data to Excel, CSV, and PDF formats
- **Automatic Backups** - Configurable automatic database backups
- **Web Dashboard** - Real-time monitoring via web interface

### 🎨 **User Interface**
- **Modern Design** - Clean, professional UI with responsive layout
- **Full Screen Support** - F11 key to toggle full screen mode
- **Quick Actions** - Fast access to common functions
- **Search & Filter** - Advanced search capabilities with multiple filters
- **Real-time Updates** - Live data synchronization

### 🔧 **Technical Features**
- **SQLite Database** - Lightweight, embedded database
- **JavaFX UI** - Modern desktop application framework
- **Maven Build** - Easy dependency management and building
- **Unit Testing** - Comprehensive test coverage
- **Activity Logging** - Complete audit trail of all actions
- **Web Server** - Optional embedded web server for remote access

## 🚀 Quick Start

### Prerequisites
- **Java 18+** (OpenJDK or Oracle JDK)
- **Maven 3.6+**
- **Git** (for cloning the repository)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/lostAndFound.git
   cd lostAndFound
   ```

2. **Build the application**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Default Login Credentials
- **Admin Account**: AM `287874`, Password `287874Sotiris!`
- **Regular User**: Create new accounts through the admin panel

## 📱 Usage Guide

### 🏠 **Main Dashboard**
- **Quick Actions**: Access common functions with one click
- **Statistics Overview**: View total records, pending returns, monthly stats
- **Menu Bar**: Full navigation menu with all features

### 📝 **Record Management**
- **Add New Record**: Click "Νέα Εγγραφή" or use quick action
- **Edit Record**: Select record and click "Edit"
- **Search Records**: Use the search bar with multiple filters
- **Image Upload**: Attach photos with automatic compression
- **Export Data**: Export to Excel, CSV, or PDF formats

### 🔄 **Return Management**
- **Process Returns**: Mark items as returned
- **Track Status**: Monitor return status and dates
- **Generate Reports**: Create return statistics

### ⚙️ **Admin Settings**
- **User Management**: Add, edit, or remove users
- **Backup Settings**: Configure automatic backups
- **Web Dashboard**: Enable/disable web monitoring
- **Database Reset**: Reset SQLite ID sequences

## 🌐 Web Dashboard

Access the web dashboard at `http://localhost:8080` (when enabled)

### Features:
- **Real-time Monitoring** - Live updates of all activities
- **Records Table** - View and search all records
- **Returns Table** - Monitor return status
- **Activity Logs** - Complete audit trail
- **Search Functionality** - Advanced filtering options

### Authentication:
- **Username**: `admin`
- **Password**: `admin123`

## 🗄️ Database Schema

### Main Tables:
- **`users`** - User accounts and authentication
- **`records`** - Lost and found item records
- **`returns`** - Item return tracking
- **`activities`** - System activity logs

### Key Fields:
- **Records**: ID, date/time, founder info, item details, location, images
- **Returns**: Record ID, return date, status, notes
- **Users**: AM number, password hash, role, permissions

## 🔧 Configuration

### Application Settings
- **Database Path**: `data/lostAndFound.db`
- **Image Storage**: `Desktop/export_data/images/`
- **Backup Location**: `data/backups/`
- **Web Server Port**: `8080` (configurable)

### Backup Configuration
- **Automatic Backups**: Configurable intervals (1 hour default)
- **Manual Backups**: Available through admin settings
- **Backup Format**: SQLite database files with timestamps

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

### Test Coverage
- **Unit Tests**: Model classes, utilities, business logic
- **Integration Tests**: Database operations, API endpoints
- **UI Tests**: User interface functionality

## 📦 Dependencies

### Core Dependencies
- **JavaFX 21** - UI framework
- **SQLite JDBC** - Database connectivity
- **BCrypt** - Password hashing
- **iText7** - PDF generation
- **Apache POI** - Excel export
- **Gson** - JSON processing

### Web Dependencies
- **Jetty Server** - Embedded web server
- **WebSocket** - Real-time communication
- **Servlet API** - Web interface

## 🚀 Building and Deployment

### Create JAR File
```bash
mvn clean package
```

### Run JAR File
```bash
java -jar target/lostAndFound-2.0.1.jar
```

### Create Executable
```bash
mvn jpackage:package
```

## 🔒 Security Features

- **Password Encryption** - BCrypt hashing
- **User Authentication** - Secure login system
- **Activity Logging** - Complete audit trail
- **Input Validation** - Data sanitization
- **SQL Injection Protection** - Prepared statements

## 📊 Performance Features

- **Image Compression** - Automatic image optimization
- **Database Indexing** - Optimized queries
- **Lazy Loading** - Efficient data loading
- **Caching** - Improved response times
- **Background Processing** - Non-blocking operations

## 🐛 Troubleshooting

### Common Issues

1. **Application Won't Start**
   - Check Java version (18+ required)
   - Verify Maven installation
   - Run `mvn clean compile`

2. **Database Errors**
   - Check database file permissions
   - Verify SQLite installation
   - Reset database if corrupted

3. **Image Upload Issues**
   - Check image file permissions
   - Verify storage directory exists
   - Check file size limits

4. **Web Dashboard Not Working**
   - Check port 8080 availability
   - Verify firewall settings
   - Check web server logs

### Log Files
- **Application Logs**: Console output
- **Database Logs**: SQLite logs
- **Web Server Logs**: Jetty logs

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Support

For support and questions:
- **Issues**: Create a GitHub issue
- **Documentation**: Check this README
- **Email**: support@lostandfound.com

## 🔄 Version History

### v2.0.1 (Current)
- ✅ Full screen support
- ✅ Web dashboard with real-time updates
- ✅ Comprehensive activity logging
- ✅ Modern UI redesign
- ✅ Image management system
- ✅ Automatic backup functionality
- ✅ Unit and integration tests

### v2.0.0
- ✅ SQLite database migration
- ✅ User authentication system
- ✅ Record management
- ✅ Return tracking
- ✅ Statistics dashboard
- ✅ Export functionality

## 🎯 Roadmap

### Upcoming Features
- [ ] Mobile app integration
- [ ] Cloud synchronization
- [ ] Advanced reporting
- [ ] Multi-language support
- [ ] API endpoints
- [ ] Email notifications

---

**Made with ❤️ for efficient lost and found management**