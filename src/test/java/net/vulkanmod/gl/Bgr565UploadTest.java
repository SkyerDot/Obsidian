package net.vulkanmod.gl;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Bgr565UploadTest {
    @Test
    void expandsBgr565ToOpaqueRgba8() {
        ByteBuffer src = ByteBuffer.allocateDirect(4).order(ByteOrder.LITTLE_ENDIAN);
        src.putShort((short) 0x001F);
        src.putShort((short) 0x07E0);
        src.position(0);

        ByteBuffer rgba = ByteBuffer.allocateDirect(8);
        GlUtil.bgr565ToRgba8(src, rgba, 2);

        assertEquals(0xF8, rgba.get(0) & 0xFF, "red expands from 5 bits");
        assertEquals(0x00, rgba.get(1) & 0xFF);
        assertEquals(0x00, rgba.get(2) & 0xFF);
        assertEquals(0xFF, rgba.get(3) & 0xFF, "alpha is opaque");
        assertEquals(0x00, rgba.get(4) & 0xFF);
        assertEquals(0xFC, rgba.get(5) & 0xFF, "green expands from 6 bits");
        assertEquals(0x00, rgba.get(6) & 0xFF);
        assertEquals(0xFF, rgba.get(7) & 0xFF);
    }
}
