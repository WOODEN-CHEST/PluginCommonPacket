package sus.keiger.plugincommon.packet.serverbound;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum PlayerActionStatus
{
    StartDigging(0),
    CancelDigging(1),
    FinishDigging(2),
    DropItemStack(3),
    DropItem(4),
    UpdateItemStatus(5),
    SwapItemInHand(6);


    // Private static fields.
    private static final Map<Integer, PlayerActionStatus> _statusIDToStatus = new HashMap<>();


    // Private fields.
    private final int _statusID;


    // Initializers.
    static
    {
        for (PlayerActionStatus status : values())
        {
            _statusIDToStatus.put(status._statusID, status);
        }
    }


    // Constructors.
    PlayerActionStatus(int statusID)
    {
        _statusID = statusID;
    }


    // Static methods.
    public static Optional<PlayerActionStatus> GetStatusByID(int statusID)
    {
        return Optional.ofNullable(_statusIDToStatus.get(statusID));
    }


    // Methods.
    public int GetStatusID()
    {
        return _statusID;
    }
}
