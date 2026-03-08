package sus.keiger.plugincommon.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum PacketBlockFace
{
    Bottom(0),
    Top(1),
    North(2),
    South(3),
    West(4),
    East(5);


    // Private static fields.
    private static final Map<Integer, PacketBlockFace> _statusIDToStatus = new HashMap<>();


    // Private fields.
    private final int _faceID;


    // Initializers.
    static
    {
        for (PacketBlockFace status : values())
        {
            _statusIDToStatus.put(status._faceID, status);
        }
    }


    // Constructors.
    PacketBlockFace(int statusID)
    {
        _faceID = statusID;
    }


    // Static methods.
    public static Optional<PacketBlockFace> GetFaceByID(int faceID)
    {
        return Optional.ofNullable(_statusIDToStatus.get(faceID));
    }


    // Methods.
    public int GetFaceID()
    {
        return _faceID;
    }
}
