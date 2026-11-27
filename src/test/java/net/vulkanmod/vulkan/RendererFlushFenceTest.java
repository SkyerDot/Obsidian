package net.vulkanmod.vulkan;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RendererFlushFenceTest {
    private static final Path RENDERER = Path.of("src/main/java/net/vulkanmod/vulkan/Renderer.java");

    @Test
    void midFrameFlushDoesNotSubmitWithTheFrameFence() throws IOException {
        String body = flushCmdsBody();

        assertFalse(body.contains("inFlightFences"),
                "flushCmds runs mid-frame and must not reset or submit with the frame's in-flight fence");
        assertTrue(body.contains("flushFence"),
                "flushCmds must submit with a fence dedicated to mid-frame flushes");
    }

    private static String flushCmdsBody() throws IOException {
        List<String> lines = Files.readAllLines(RENDERER);
        int start = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("public void flushCmds()")) {
                start = i;
                break;
            }
        }
        assertTrue(start >= 0, "Renderer.flushCmds() not found at " + RENDERER.toAbsolutePath());

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
