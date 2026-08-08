@echo off
REM Compiles and runs the chatbot in one step.
cd /d "%~dp0"
echo Compiling...
javac ChatbotGUI.java GeminiAPI.java
if errorlevel 1 (
    echo Compile failed. Check the errors above.
    pause
    exit /b 1
)
echo Starting chatbot...
java ChatbotGUI
pause
