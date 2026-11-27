package net.vulkanmod.gl;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Bgr565ReadbackTest {
    @Test
    void convertsRgba8ToBgr565WithoutOverwritingNeighbourPixels() {
        ByteBuffer src = ByteBuffer.allocateDirect(8).order(ByteOrder.LITTLE_ENDIAN);
        src.put(new byte[]{(byte) 255, 0, 0, (byte) 255, 0, (byte) 255, 0, (byte) 255});
        ByteBuffer dst = ByteBuffer.allocateDirect(4).order(ByteOrder.LITTLE_ENDIAN);

        GlUtil.rgba8ToBgr565(src, dst, 2);

        assertEquals(0x001F, dst.getShort(0) & 0xFFFF, "red occupies the low 5 bits");
        assertEquals(0x07E0, dst.getShort(2) & 0xFFFF, "green occupies the middle 6 bits");
    }
}
