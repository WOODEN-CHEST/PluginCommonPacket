package sus.keiger.plugincommon.packet;

import com.comphenix.protocol.wrappers.EnumWrappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum PacketBlockFace
{
    Bottom(EnumWrappers.Direction.DOWN),
    Top(EnumWrappers.Direction.UP),
    North(EnumWrappers.Direction.NORTH),
    South(EnumWrappers.Direction.SOUTH),
    West(EnumWrappers.Direction.WEST),
    East(EnumWrappers.Direction.EAST);


    // Private static fields.
    private static final Map<EnumWrappers.Direction, PacketBlockFace> _statusIDToStatus = new HashMap<>();


    // Private fields.
    private final EnumWrappers.Direction _direction;


    // Initializers.
    static
    {
        for (PacketBlockFace status : values())
        {
            _statusIDToStatus.put(status._direction, status);
        }
    }


    // Constructors.
    PacketBlockFace(EnumWrappers.Direction direction)
    {
        _direction = direction;
    }


    // Static methods.
    public static Optional<PacketBlockFace> GetFaceByDirection(EnumWrappers.Direction direction)
    {
        return Optional.ofNullable(_statusIDToStatus.get(direction));
    }


    // Methods.
    public EnumWrappers.Direction GetDirection()
    {
        return _direction;
    }
}
