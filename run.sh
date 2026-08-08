#!/bin/bash
# Compiles and runs the chatbot in one step.
cd "$(dirname "$0")"
echo "Compiling..."
javac ChatbotGUI.java GeminiAPI.java
if [ $? -ne 0 ]; then
    echo "Compile failed. Check the errors above."
    exit 1
fi
echo "Starting chatbot..."
java ChatbotGUI
