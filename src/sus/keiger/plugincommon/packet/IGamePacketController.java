package sus.keiger.plugincommon.packet;

import org.bukkit.entity.Player;
import sus.keiger.plugincommon.PCPluginEvent;
import sus.keiger.plugincommon.packet.clientbound.*;
import sus.keiger.plugincommon.packet.serverbound.RenameItemPacket;
import sus.keiger.plugincommon.packet.serverbound.ServerBoundGamePacket;

import java.util.Collection;

public interface IGamePacketController
{
    // Methods.
    void SendPacket(ClientBoundGamePacket packet, Player player);
    void SendPacket(ClientBoundGamePacket packet, Collection<? extends  Player> players);
    void ReceiveClientPacket(ServerBoundGamePacket packet, Player player);
    void StartListeningForPackets();
    void StopListeningForPackets();
    void AddPacketToListen(Class<? extends GamePacket> packetClass);
    void RemovePacketFromListen(Class<? extends GamePacket> packetClass);
    void ClearPacketListenList();


    PCPluginEvent<GamePacketEvent<? extends GamePacket>> GetPacketSendEvent();
    PCPluginEvent<GamePacketEvent<? extends GamePacket>> GetPacketReceiveEvent();

    PCPluginEvent<GamePacketEvent<SetHealthPacket>> GetSetHealthPacketEvent();
    PCPluginEvent<GamePacketEvent<PlayerInfoUpdatePacket>> GetPlayerInfoUpdatePacketEvent();
    PCPluginEvent<GamePacketEvent<PlayerInfoRemovePacket>> GetPlayerInfoRemovePacketEvent();
    PCPluginEvent<GamePacketEvent<UpdateAttributePacket>> GetUpdateAttributesPacketEvent();
    PCPluginEvent<GamePacketEvent<SetBlockDestroyStagePacket>> GetBlockDestroyStageEvent();

    PCPluginEvent<GamePacketEvent<RenameItemPacket>> GetRenameItemPacketEvent();
}