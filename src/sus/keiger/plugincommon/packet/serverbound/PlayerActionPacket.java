package sus.keiger.plugincommon.packet.serverbound;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.util.Vector;
import sus.keiger.plugincommon.packet.PacketBlockFace;

import java.util.Objects;

public class PlayerActionPacket extends ServerBoundGamePacket
{
    // Static fields.
    public static final int ID = 0x28;


    // Private fields.
    private PlayerActionStatus _action = PlayerActionStatus.FinishDigging;
    private Vector _position = new Vector();
    private PacketBlockFace _face = PacketBlockFace.Bottom;
    private int _sequence = 0;


    // Constructors.
    public PlayerActionPacket()
    {
        super(ID);
    }

    public PlayerActionPacket(PacketContainer packet)
    {
        super(ID);

        PlayerActionStatus.GetStatusByID(packet.getIntegers().read(0)).ifPresent(this::SetAction);
        SetPosition(packet.getBlockPositionModifier().read(0).toVector());
        PacketBlockFace.GetFaceByID(packet.getBytes().read(0)).ifPresent(this::SetFace);
        SetSequence(packet.getIntegers().read(1));
    }


    // Methods.
    public PlayerActionStatus GetAction()
    {
        return _action;
    }

    public void SetAction(PlayerActionStatus action)
    {
        _action = Objects.requireNonNull(action, "action is null");
    }

    public Vector GetPosition()
    {
        return _position;
    }

    public void SetPosition(Vector position)
    {
        _position = Objects.requireNonNull(position, "position is null");
    }

    public PacketBlockFace GetFace()
    {
        return _face;
    }

    public void SetFace(PacketBlockFace face)
    {
        _face = Objects.requireNonNull(face, "face is null");
    }

    public int GetSequence()
    {
        return _sequence;
    }

    public void SetSequence(int sequence)
    {
        _sequence = sequence;
    }


    // Inherited methods.
    @Override
    public PacketContainer CreatePacketContainer(ProtocolManager protocolManager)
    {
        return null;
    }
}