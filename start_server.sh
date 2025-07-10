#!/bin/bash

# Format seconds as mm:ss
format_time() {
  local T=$1
  printf "%02d:%02d" $((T / 60)) $((T % 60))
}

# Progress bar with percentage and elapsed time
progress_bar() {
  local pid=$1
  local delay=0.2
  local max=50
  local percent=0
  local start_time=$(date +%s)

  echo -n "   ["
  while kill -0 "$pid" 2>/dev/null; do
    local elapsed=$(($(date +%s) - start_time))
    if [ $percent -lt 98 ]; then
      percent=$((percent + 2))
    fi
    local hashes=$((percent * max / 100))
    local spaces=$((max - hashes))
    local bar=$(printf "%0.s#" $(seq 1 $hashes))
    local space=$(printf "%0.s " $(seq 1 $spaces))
    printf "\r   [%s%s] %3d%% | Elapsed: %s" "$bar" "$space" "$percent" "$(format_time $elapsed)"
    sleep $delay
  done

  local elapsed=$(($(date +%s) - start_time))
  printf "\r✅ [%-50s] 100%% | Elapsed: %s\n" "##################################################" "$(format_time $elapsed)"
}

echo "=============================="
echo "🚀 Starting build process..."
echo "=============================="

# Remove existing logs directory
if [ -d "logs" ]; then
  rm -rf logs
  echo "🗑️  Old 'logs/' directory deleted."
fi

# Recreate logs directory
mkdir -p logs
echo "📁 'logs/' directory created."

timestamp=$(date '+%Y-%m-%d_%H-%M-%S')
logfile="logs/build_$timestamp.log"

# Check if build folder exists
if [ -d "build" ]; then
  echo "🧹 'build/' directory found. Cleaning project (output appended to build log)..."
  nohup bash -c './gradlew clean && echo "Clean finished."' >> "$logfile" 2>&1 &
  CLEAN_PID=$!
  progress_bar $CLEAN_PID

  if wait $CLEAN_PID; then
    echo "✅ Clean completed successfully."
  else
    echo "❌ Clean failed. Check $logfile for details."
    exit 1
  fi
else
  echo "📁 No 'build/' directory found. Skipping clean step."
fi

echo "🛠️ Building the app (skipping tests, output appended to build log)..."
nohup bash -c './gradlew build -x test && echo "Build finished."' >> "$logfile" 2>&1 &
BUILD_PID=$!
progress_bar $BUILD_PID

if wait $BUILD_PID; then
  echo "✅ Build completed successfully."
else
  echo "❌ Build failed. Check $logfile for details."
  exit 1
fi

app_logfile="logs/app_log_$timestamp.log"

echo "🚀 Starting the application..."
# JVM options optimized for Amazon Linux 2 with 2GB RAM
JAVA_OPTS="-Xms512m -Xmx1536m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -XX:+UseCompressedOops -XX:+UseCompressedClassPointers -Djava.awt.headless=true -Dfile.encoding=UTF-8"
nohup java $JAVA_OPTS -jar build/libs/*.jar 2>&1 | awk '{ print strftime("[%Y-%m-%d %H:%M:%S]"), $0; fflush(); }' >> "$app_logfile" &
APP_PID=$!

sleep 2

echo "======================================"
echo "✅ Application started successfully."
echo "📄 Build logs: $logfile"
echo "📄 App logs:   $app_logfile"
echo "📌 To follow build logs: tail -f $logfile"
echo "📌 To follow app logs:   tail -f $app_logfile"
echo "🛑 To stop the app, run: kill $APP_PID"
echo "======================================"

