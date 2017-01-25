package com.ne0nx3r0.quantum.api.circuit;

import com.ne0nx3r0.quantum.api.IValidMaterials;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.api.receiver.CompatReceiver;
import com.ne0nx3r0.quantum.api.receiver.Receiver;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by Yannick on 24.01.2017.
 */
public interface Circuit extends IValidMaterials, ConfigurationSerializable {

    String getType();

    void addReceiver(Class<? extends AbstractReceiver> receiverClass, Location loc, int delay) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    void addReceiver(AbstractReceiver receiver);

    void addReceiver(Collection<AbstractReceiver> receivers);

    void addReceiver(AbstractReceiver... receivers);

    List<AbstractReceiver> getReceivers();

    int getReceiversCount();

    List<CompatReceiver> getInValidReceivers();

    int getWholeReceiverCount();

    int getInValidReceiversCount();

    void delReceiver(Receiver r);

    void delReceiver(Location location);

    boolean isReceiver(Location location);

    UUID getOwner();

    void setOwner(UUID playerUUID);

    boolean isValid();

    int getBlockCurrent();

    Location getLocation();

    void setLocation(Location location);

    int getDelay();

    void setDelay(int delay);

    void calculate(Receiver receiver, int oldCurrent, int newCurrent);

    void actvate(int oldCurrent, int newCurrent, int chain);
}
