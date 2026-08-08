# 🤖 AI Chatbot — Java Swing + Gemini API

A desktop AI chatbot built with **Java Swing** that connects to Google's **Gemini API** and displays AI-generated responses in a simple, user-friendly chat interface.

## ✨ Features

* 🤖 AI-powered conversations using Gemini API
* 🖥️ Java Swing graphical interface
* 💬 Real-time chat interface
* ⚡ Background API calls using `SwingWorker`
* 🌐 HTTP communication using Java `HttpClient`
* 🔐 API key through environment variable
* 📦 JSON request/response handling
* 🚫 No external Java libraries required

## 📁 Project Files

```text
AI-Chatbot/
├── ChatbotGUI.java
├── GeminiAPI.java
└── README.md
```

### `ChatbotGUI.java`

Contains the Swing user interface, chat window, input field, Send button, and application `main()` method.

### `GeminiAPI.java`

Handles communication with the Gemini API, sends user messages, and processes the AI response.

## 🔑 Setup

### 1. Get a Gemini API Key

Create an API key from Google's AI Studio.

**Important:** Never upload your API key to GitHub or put it directly inside your Java source code.

### 2. Set the API Key

**Windows CMD:**

```cmd
set GEMINI_API_KEY=your_api_key_here
```

**Windows PowerShell:**

```powershell
$env:GEMINI_API_KEY="your_api_key_here"
```

**Linux/macOS:**

```bash
export GEMINI_API_KEY=your_api_key_here
```

### 3. Compile

```bash
javac ChatbotGUI.java GeminiAPI.java
```

### 4. Run

```bash
java ChatbotGUI
```

## ⚙️ How It Works

1. **User Interface** — `ChatbotGUI` provides the chat window, input field, and Send button.
2. **User Message** — When the user presses Send or Enter, the message is passed to the API layer.
3. **Background Request** — `SwingWorker` performs the API request without freezing the Swing interface.
4. **Gemini API** — `GeminiAPI` sends the user's message to Gemini using Java's built-in `HttpClient`.
5. **Response Processing** — The API response is processed and the generated text is extracted.
6. **Display** — The AI response is displayed in the chat window.

## 🛠️ Technologies

* Java
* Java Swing
* Object-Oriented Programming
* HTTP Client
* REST API
* JSON
* SwingWorker
* Gemini API
* Git & GitHub

## 🎓 Learning Objectives

This project demonstrates practical use of:

* Java OOP
* GUI development
* API integration
* HTTP requests
* JSON processing
* Exception handling
* Multithreading/background tasks
* Git and GitHub

## 🔮 Future Improvements

* 💾 Save chat history
* 🔐 User authentication
* 🌙 Dark mode
* 🎙️ Voice input
* 📎 File support
* 🌐 Multi-language support
* 🧠 Conversation memory

## ⚠️ Important

An active internet connection is required to communicate with the Gemini API.

API availability, model names, pricing, and rate limits may change over time. Check Google's current Gemini API documentation for the latest information.

## 👨‍💻 Author

**Dev Solanki**

---

⭐ If you find this project useful, consider giving the repository a star!
