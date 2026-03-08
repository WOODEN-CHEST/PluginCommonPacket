package sus.keiger.plugincommon.packet.serverbound;

import com.comphenix.protocol.wrappers.EnumWrappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum PlayerActionStatus
{
    StartDigging(EnumWrappers.PlayerDigType.START_DESTROY_BLOCK),
    CancelDigging(EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK),
    FinishDigging(EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK),
    DropItemStack(EnumWrappers.PlayerDigType.DROP_ITEM),
    DropItem(EnumWrappers.PlayerDigType.DROP_ALL_ITEMS),
    UpdateItemStatus(EnumWrappers.PlayerDigType.RELEASE_USE_ITEM),
    SwapItemInHand(EnumWrappers.PlayerDigType.SWAP_HELD_ITEMS);


    // Private static fields.
    private static final Map<EnumWrappers.PlayerDigType, PlayerActionStatus> _statusIDToStatus = new HashMap<>();


    // Private fields.
    private final EnumWrappers.PlayerDigType _digType;


    // Initializers.
    static
    {
        for (PlayerActionStatus status : values())
        {
            _statusIDToStatus.put(status._digType, status);
        }
    }


    // Constructors.
    PlayerActionStatus(EnumWrappers.PlayerDigType digType)
    {
        _digType = digType;
    }


    // Static methods.
    public static Optional<PlayerActionStatus> GetStatusByDigType(EnumWrappers.PlayerDigType digType)
    {
        return Optional.ofNullable(_statusIDToStatus.get(digType));
    }


    // Methods.
    public EnumWrappers.PlayerDigType GetDigType()
    {
        return _digType;
    }
}
