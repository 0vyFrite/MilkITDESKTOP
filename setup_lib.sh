#!/usr/bin/env bash
set -euo pipefail

# Récupère les dépendances Maven dans le dossier lib/
mkdir -p lib
mvn dependency:copy-dependencies -DoutputDirectory=lib -DincludeScope=runtime

echo "Dependencies copied to lib/"