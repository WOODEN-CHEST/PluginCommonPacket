package sus.keiger.plugincommon.packet.clientbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.util.Vector;

import java.util.Objects;

public class SetBlockDestroyStagePacket extends ClientBoundGamePacket
{
    // Static fields.
    public static final int ID = 0x05;
    public static final int STAGE_MIN = 0;
    public static final int STAGE_MAX = 9;


    // Fields.
    private int _stage = STAGE_MIN;
    private int _causeEntity = 0;
    private Vector _blockLocation = new Vector(0, 0, 0);


    // Constructors.
    public SetBlockDestroyStagePacket()
    {
        super(ID);
    }

    public SetBlockDestroyStagePacket(PacketContainer packet)
    {
        super(ID);

        SetCauseEntityID(packet.getIntegers().read(0));
        SetBlockLocation(packet.getBlockPositionModifier().read(0).toVector());
        SetStage(packet.getBytes().read(0));
    }


    // Methods.
    public int GetStage()
    {
        return _stage;
    }

    public void SetStage(int stage)
    {
        _stage = Math.max(STAGE_MIN, Math.min(STAGE_MAX, stage));
    }

    public int GetCauseEntityID()
    {
        return _causeEntity;
    }

    public void SetCauseEntityID(int entity)
    {
        _causeEntity = entity;
    }

    public Vector GetBlockLocation()
    {
        return _blockLocation;
    }

    public void SetBlockLocation(Vector location)
    {
        _blockLocation = Objects.requireNonNull(location, "location is null");
    }


    // Inherited methods.
    @Override
    public PacketContainer CreatePacketContainer(ProtocolManager protocolManager)
    {
        PacketContainer Packet = protocolManager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);

        Packet.getIntegers().write(0, _causeEntity);
        Packet.getBlockPositionModifier().write(0, new BlockPosition(_blockLocation));
        Packet.getBytes().write(0, (byte) _stage);

        return Packet;
    }
}