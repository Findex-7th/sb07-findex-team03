package com.team3.findex.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 cursor token encoder/decoder utility.
 */
public final class CursorEncodingUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private CursorEncodingUtil() {
    }

    /**
     * Encode a numeric id into a Base64 JSON cursor token.
     *
     * @param id entity identifier
     * @return encoded token or null when id is null
     */
    public static String encodeId(Long id) {
        if (id == null) {
            return null;
        }
        String payload = String.format("{\"id\":%d}", id);
        return Base64.getEncoder()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decode a Base64 JSON cursor token into a numeric id.
     *
     * @param cursor Base64 encoded token
     * @return decoded id or null when cursor is blank
     */
    public static Long decodeId(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cursor);
            JsonNode node = OBJECT_MAPPER.readTree(decodedBytes);
            return node.hasNonNull("id") ? node.get("id").asLong() : null;
        } catch (IllegalArgumentException | IOException e) {
            throw new IllegalArgumentException("Invalid cursor token", e);
        }
    }
}
