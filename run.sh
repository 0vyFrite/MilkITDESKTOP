#!/usr/bin/env bash
set -euo pipefail

# Compile project (if not compiled)
mvn -DskipTests compile

# Run the application with classes and all jars in lib/
java -cp "target/classes:lib/*" Main
