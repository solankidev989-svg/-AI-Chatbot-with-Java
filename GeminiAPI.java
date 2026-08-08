import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Talks to Google's Gemini API over plain HTTP.
 * Uses only classes built into the JDK (java.net.http) — no external
 * libraries to install, and no Maven/Gradle setup needed.
 */
public class GeminiAPI {

    // Free-tier model as of 2026. Change here if Google renames/retires it.
    private static final String MODEL = "gemini-2.5-flash";
    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL + ":generateContent";

    private final String apiKey;
    private final HttpClient client;

    public GeminiAPI(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    /**
     * Sends the user's message to Gemini and returns the reply text.
     * Throws RuntimeException with a readable message if anything goes wrong.
     */
    public String getReply(String userMessage) throws Exception {
        String systemPrompt = "You are a friendly, helpful assistant in a simple Java chatbot demo. "
                + "Keep answers short and clear.";

        String requestBody = "{"
                + "\"contents\":[{\"parts\":[{\"text\":\"" + escapeJson(userMessage) + "\"}]}],"
                + "\"systemInstruction\":{\"parts\":[{\"text\":\"" + escapeJson(systemPrompt) + "\"}]}"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", apiKey)
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String msg = extractStringField(response.body(), "\"message\":");
            throw new RuntimeException("API error (" + response.statusCode() + "): "
                    + (msg != null ? msg : response.body()));
        }

        String reply = extractStringField(response.body(), "\"text\":");
        if (reply == null) {
            throw new RuntimeException("Couldn't parse a reply from the API response.");
        }
        return reply;
    }

    /** Escapes a string so it can be safely embedded inside a JSON string literal. */
    private String escapeJson(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /**
     * Minimal hand-written JSON string-field extractor: finds "key":"value"
     * and returns the unescaped value. Good enough for this small project
     * without pulling in an external JSON library.
     */
    private String extractStringField(String json, String key) {
        int idx = json.indexOf(key);
        if (idx == -1) return null;
        int i = idx + key.length();
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (i >= json.length() || json.charAt(i) != '"') return null;
        i++; // skip opening quote

        StringBuilder sb = new StringBuilder();
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(i + 1);
                switch (next) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case 'u':
                        if (i + 6 <= json.length()) {
                            String hex = json.substring(i + 2, i + 6);
                            sb.append((char) Integer.parseInt(hex, 16));
                            i += 4;
                        }
                        break;
                    default: sb.append(next);
                }
                i += 2;
            } else if (c == '"') {
                break; // end of string value
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }
}
