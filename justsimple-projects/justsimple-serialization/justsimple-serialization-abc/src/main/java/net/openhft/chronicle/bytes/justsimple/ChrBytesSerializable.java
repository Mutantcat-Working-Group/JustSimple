package net.openhft.chronicle.bytes.justsimple;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import org.mutantcat.justsimple.serialization.abc.io.AbcFactory;
import org.mutantcat.justsimple.serialization.abc.io.AbcSerializable;

public interface ChrBytesSerializable extends AbcSerializable<BytesIn, BytesOut> {
    @Override
    default AbcFactory<BytesIn, BytesOut> serializeFactory(){
        return ChrBytesFactory.getInstance();
    }
}