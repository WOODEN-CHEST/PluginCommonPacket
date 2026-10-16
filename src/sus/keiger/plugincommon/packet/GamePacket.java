package sus.keiger.plugincommon.packet;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public abstract class GamePacket
{
    // Private fields.
    private final int _id;
    private boolean _isModified = false;



    // Constructors.
    public GamePacket(int id)
    {
        _id = id;
    }


    // Methods.
    public int GetID()
    {
        return _id;
    }

    /**
     * Determines whether the packet was changed since it was read from the network. A packet read from the network is
     * only written back when it was changed, as writing it back could lose what it couldn't read.
     * @return <code>true</code> if the packet was changed, <code>false</code> otherwise.
     */
    public boolean GetIsModified()
    {
        return _isModified;
    }

    public abstract PacketContainer CreatePacketContainer(ProtocolManager protocolManager);


    // Protected methods.
    /**
     * Records that the packet was changed. Every setter of a packet calls this.
     */
    protected final void MarkModified()
    {
        _isModified = true;
    }


    // Default methods.
    void SetIsModified(boolean isModified)
    {
        _isModified = isModified;
    }
}