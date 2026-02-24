package sus.keiger.plugincommon.packet;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import sus.keiger.plugincommon.PCPluginEvent;
import sus.keiger.plugincommon.packet.clientbound.*;
import sus.keiger.plugincommon.packet.serverbound.RenameItemPacket;
import sus.keiger.plugincommon.packet.serverbound.ServerBoundGamePacket;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class PCGamePacketController implements IGamePacketController, PacketListener
{
    // Private fields.
    private final ProtocolManager _protocolManager;
    private final Plugin _plugin;
    private final ListeningWhitelist _sendingWhitelist;
    private final ListeningWhitelist _receivingWhitelist;
    private final Set<Class<? extends GamePacket>> _listenedPackets = new HashSet<>();



    /* Packet types. */
    private final PCPluginEvent<GamePacketEvent<? extends GamePacket>> _packetSendEvent = new PCPluginEvent<>();
    private final PCPluginEvent<GamePacketEvent<? extends GamePacket>> _packetReceiveEvent = new PCPluginEvent<>();
    private final Map<PacketType, PacketData<?>> _recognizedPackets = new HashMap<>();


    // Constructors.
    public PCGamePacketController(Plugin plugin, ProtocolManager protocolManager)
    {
        _protocolManager = Objects.requireNonNull(protocolManager, "protocolManager is null");
        _plugin = Objects.requireNonNull(plugin, "plugin is null");

        RegisterPacket(PacketType.Play.Server.UPDATE_HEALTH, SetHealthPacket.class, SetHealthPacket::new);
        RegisterPacket(PacketType.Play.Server.PLAYER_INFO, PlayerInfoUpdatePacket.class, PlayerInfoUpdatePacket::new);
        RegisterPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE, PlayerInfoRemovePacket.class, PlayerInfoRemovePacket::new);
        RegisterPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION, SetBlockDestroyStagePacket.class, SetBlockDestroyStagePacket::new);
        RegisterPacket(PacketType.Play.Client.ITEM_NAME, RenameItemPacket.class, RenameItemPacket::new);

        _sendingWhitelist = ListeningWhitelist.newBuilder().types(GetRegisteredPacketTypes(false)).build();
        _receivingWhitelist = ListeningWhitelist.newBuilder().types(GetRegisteredPacketTypes(true)).build();
    }



    // Private methods.
    private PacketType[] GetRegisteredPacketTypes(boolean isServerSentPacket)
    {
        return _recognizedPackets.keySet()
                .stream()
                .filter(type -> isServerSentPacket ? type.isServer() : type.isClient())
                .toArray(PacketType[]::new);
    }


    /* Packet generic methods. */
    private <T extends GamePacket> void RegisterPacket(PacketType type,
                                                       Class<T> packetClass,
                                                       Function<PacketContainer, T> packetConstructor)
    {
        PacketData<T> Data = new PacketData<>(type,
                packetConstructor,
                packetClass,
                new PCPluginEvent<>());

        _recognizedPackets.put(type, Data);
    }

    private void PropagatePacket(PacketEvent event)
    {
        PacketData<?> Data = _recognizedPackets.get(event.getPacketType());
        if (Data == null)
        {
            return;
        }

        PCPluginEvent<GamePacketEvent<? extends GamePacket>> GenericEvent =
                Data.PacketType.isClient() ? _packetReceiveEvent : _packetSendEvent;

        FirePacketEvent(event, GenericEvent, Data);
    }

    private <T extends GamePacket>
    void FirePacketEvent(PacketEvent sourceEvent,
                         PCPluginEvent<GamePacketEvent<? extends GamePacket>> genericEvent,
                         PacketData<T> packetData)
    {
        if (!_listenedPackets.contains(packetData.PacketClass))
        {
            return;
        }

        T Packet = packetData.Constructor.apply(sourceEvent.getPacket());

        GamePacketEvent<T> PacketEvent = new GamePacketEvent<>(Packet, sourceEvent.getPlayer());
        genericEvent.FireEvent(PacketEvent);
        packetData.IOEvent.FireEvent(PacketEvent);
        sourceEvent.setCancelled(PacketEvent.GetIsCancelled());

        if (!PacketEvent.GetIsCancelled())
        {
            sourceEvent.setPacket(Packet.CreatePacketContainer(_protocolManager));
        }
    }

    @SuppressWarnings("unchecked") // Yay!
    private <T> PCPluginEvent<T> GetPacketByTypeOrThrow(PacketType type)
    {
        return (PCPluginEvent<T>) Optional.of(type)
                .map(_recognizedPackets::get)
                .map(PacketData::IOEvent)
                .orElseThrow();
    }


    // Inherited methods.
    @Override
    public void SendPacket(ClientBoundGamePacket packet, Player player)
    {
        Objects.requireNonNull(packet, "packet is null");
        Objects.requireNonNull(player, "player is null");

        _protocolManager.sendServerPacket(player, packet.CreatePacketContainer(_protocolManager));
    }

    @Override
    public void SendPacket(ClientBoundGamePacket packet, Collection<? extends Player> players)
    {
        Objects.requireNonNull(packet, "packet is null");
        Objects.requireNonNull(players, "players is null");

        _protocolManager.broadcastServerPacket(packet.CreatePacketContainer(_protocolManager), players);
    }


    @Override
    public void ReceiveClientPacket(ServerBoundGamePacket packet, Player player)
    {
        Objects.requireNonNull(packet, "packet is null");
        Objects.requireNonNull(player, "player is null");

        _protocolManager.receiveClientPacket(player, packet.CreatePacketContainer(_protocolManager));
    }

    @Override
    public void StartListeningForPackets()
    {
        _protocolManager.addPacketListener(this);
    }

    @Override
    public void StopListeningForPackets()
    {
        _protocolManager.removePacketListener(this);
    }

    @Override
    public void AddPacketToListen(Class<? extends GamePacket> packetClass)
    {
        _listenedPackets.add(Objects.requireNonNull(packetClass, "packetClass is null"));
    }

    @Override
    public void RemovePacketFromListen(Class<? extends GamePacket> packetClass)
    {
        _listenedPackets.remove(Objects.requireNonNull(packetClass, "packetClass is null"));
    }

    @Override
    public void ClearPacketListenList()
    {
        _listenedPackets.clear();
    }

    @Override
    public PCPluginEvent<GamePacketEvent<? extends GamePacket>> GetPacketSendEvent()
    {
        return _packetSendEvent;
    }

    @Override
    public PCPluginEvent<GamePacketEvent<? extends GamePacket>> GetPacketReceiveEvent()
    {
        return _packetReceiveEvent;
    }

    @Override
    public PCPluginEvent<GamePacketEvent<SetHealthPacket>> GetSetHealthPacketEvent()
    {
        return GetPacketByTypeOrThrow(PacketType.Play.Server.UPDATE_HEALTH);
    }

    @Override
    public PCPluginEvent<GamePacketEvent<PlayerInfoUpdatePacket>> GetPlayerInfoUpdatePacketEvent()
    {
        return GetPacketByTypeOrThrow(PacketType.Play.Server.PLAYER_INFO);
    }

    @Override
    public PCPluginEvent<GamePacketEvent<PlayerInfoRemovePacket>> GetPlayerInfoRemovePacketEvent()
    {
        return GetPacketByTypeOrThrow(PacketType.Play.Server.PLAYER_INFO_REMOVE);
    }

    @Override
    public PCPluginEvent<GamePacketEvent<UpdateAttributePacket>> GetUpdateAttributesPacketEvent()
    {
        return GetPacketByTypeOrThrow(PacketType.Play.Server.UPDATE_ATTRIBUTES);
    }

    @Override
    public PCPluginEvent<GamePacketEvent<SetBlockDestroyStagePacket>> GetBlockDestroyStageEvent()
    {
        return GetPacketByTypeOrThrow(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
    }

    @Override
    public PCPluginEvent<GamePacketEvent<RenameItemPacket>> GetRenameItemPacketEvent()
    {
        return GetPacketByTypeOrThrow(PacketType.Play.Client.ITEM_NAME);
    }

    @Override
    public void onPacketSending(PacketEvent event)
    {
        PropagatePacket(event);
    }

    @Override
    public void onPacketReceiving(PacketEvent event)
    {
        PropagatePacket(event);
    }

    @Override
    public ListeningWhitelist getSendingWhitelist()
    {
        return _sendingWhitelist;
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist()
    {
        return _receivingWhitelist;
    }

    @Override
    public Plugin getPlugin()
    {
        return _plugin;
    }


    // Types.
    private record PacketData<T extends GamePacket>(PacketType PacketType,
                                                    Function<PacketContainer, T> Constructor,
                                                    Class<T> PacketClass,
                                                    PCPluginEvent<GamePacketEvent<T>> IOEvent)
    {
    }
}
