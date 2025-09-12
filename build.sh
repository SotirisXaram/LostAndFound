#!/bin/bash

# LostAndFound Application Build Script
echo "🏗️  Building LostAndFound Application..."

# Clean previous build
echo "🧹 Cleaning previous build..."
mvn clean

# Compile the project
echo "📦 Compiling project..."
mvn compile

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    
    # Run the application
    echo "🚀 Starting application..."
    mvn javafx:run
else
    echo "❌ Compilation failed!"
    exit 1
fi
