package net.vulkanmod.gl;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlTextureReadbackTest {
    private static final Path SOURCE = Path.of("src/main/java/net/vulkanmod/gl/GlTexture.java");

    @Test
    void texImageReadbackDoesNotSubmitTheFrameMidRender() throws IOException {
        String body = methodBody("public static void getTexImage(");

        assertFalse(body.contains("flushCmds"),
                "glGetTexImage must not submit the frame from renderTickPost: libMoltenVK segfaults in vkQueueSubmit");
        assertTrue(body.contains("ImageUtil.downloadTexture"),
                "the readback itself must still happen");
    }

    private static String methodBody(String signature) throws IOException {
        List<String> lines = Files.readAllLines(SOURCE);
        int start = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains(signature)) {
                start = i;
                break;
            }
        }
        assertTrue(start >= 0, signature + " not found at " + SOURCE.toAbsolutePath());

        StringBuilder body = new StringBuilder();
        int depth = 0;
        for (int i = start; i < lines.size(); i++) {
            String line = lines.get(i);
            body.append(line).append('\n');
            for (char c : line.toCharArray()) {
                if (c == '{') depth++;
                else if (c == '}') depth--;
            }
            if (i > start && depth == 0) break;
        }
        return body.toString();
    }
}
