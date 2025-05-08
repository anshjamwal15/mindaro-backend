#!/bin/bash

# Exit if any command fails
set -e

# Step 1: Clean and build the project (skip tests)
echo "Cleaning and building project (skipping tests)..."
./gradlew clean build -x test

# Step 2: Find the correct SNAPSHOT jar (excluding *-plain.jar)
JAR_FILE=$(find build/libs -name "*SNAPSHOT.jar" ! -name "*-plain.jar" | head -n 1)

if [[ -z "$JAR_FILE" ]]; then
  echo "ERROR: No SNAPSHOT JAR file found in build/libs"
  exit 1
fi

# Step 3: Create log file with timestamp
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
LOG_FILE="app_log_$TIMESTAMP.log"

echo "Running JAR: $JAR_FILE"
echo "Logging output to: $LOG_FILE"

# Step 4: Run the application and redirect output to log file
nohup java -jar "$JAR_FILE" > "$LOG_FILE" 2>&1 &

# Step 5: Output tail instruction
echo "Application started in background."
echo "Use the following command to see live logs:"
echo "tail -f $LOG_FILE"
