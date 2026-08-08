# AI Chatbot — Java Swing + Gemini API

A desktop chatbot built with Java Swing that sends your messages to Google's
Gemini API and shows the AI's reply in a chat window.

## Files
- `ChatbotGUI.java` — the Swing UI (chat window, input box, Send button) and `main()`
- `GeminiAPI.java` — talks to the Gemini API over HTTP and reads the reply

## 1. Get a free API key
1. Go to https://aistudio.google.com/apikey
2. Sign in with a Google account and click "Create API key" (no credit card needed)
3. Copy the key (starts with `AIza...`)

## 2. Compile
```
javac ChatbotGUI.java GeminiAPI.java
```

## 3. Run
```
java ChatbotGUI
```
On first launch it will ask you to paste the API key (or set it once as an
environment variable so you're not asked every time):
```
export GEMINI_API_KEY=your_key_here      # Linux/macOS
set GEMINI_API_KEY=your_key_here         # Windows cmd
```

## How it works (for viva / explanation)
1. **UI (`ChatbotGUI`)** — `JTextPane` shows the chat history, `JTextField` +
   `JButton` take input. Pressing Enter or Send calls `onSend()`.
2. **Background call** — the API call runs on a `SwingWorker`, not the UI
   thread, so the window doesn't freeze while waiting for a reply.
3. **Request (`GeminiAPI.getReply`)** — builds a small JSON body with the
   user's message and sends it with `java.net.http.HttpClient` (built into
   the JDK — no external library needed) to Gemini's `generateContent`
   endpoint, with the API key in the `x-goog-api-key` header.
4. **Response parsing** — `extractStringField()` is a small hand-written
   JSON reader that pulls the `"text"` value out of Gemini's reply (or the
   `"message"` field if there's an error). It's written by hand instead of
   using a JSON library, so the project has zero external dependencies.
5. **Display** — the reply is appended to the chat pane, styled in a
   different color from the user's messages.

## Notes
- Model used: `gemini-2.5-flash` (Google's current free-tier model). If
  Google renames or retires it, change the `MODEL` constant at the top of
  `GeminiAPI.java`.
- Free tier has a rate limit (a handful of requests per minute) — if you see
  a `429` error, wait a few seconds and try again.
- Needs an internet connection to reach the API.
