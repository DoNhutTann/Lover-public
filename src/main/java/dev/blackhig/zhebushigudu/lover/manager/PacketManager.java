package dev.blackhig.zhebushigudu.lover.manager;

import java.util.ArrayList;
import net.minecraft.network.Packet;
import java.util.List;
import dev.blackhig.zhebushigudu.lover.features.Feature;

public class PacketManager extends Feature
{
    private final List<Packet<?>> noEventPackets;
    
    public PacketManager() {
        this.noEventPackets = new ArrayList<Packet<?>>();
    }
    
    public void sendPacketNoEvent(final Packet<?> packet) {
        if (packet != null && !Feature.nullCheck()) {
            this.noEventPackets.add(packet);
            PacketManager.mc.player.connection.sendPacket((Packet)packet);
        }
    }
    
    public boolean shouldSendPacket(final Packet<?> packet) {
        if (this.noEventPackets.contains(packet)) {
            this.noEventPackets.remove(packet);
            return false;
        }
        return true;
    }
}
