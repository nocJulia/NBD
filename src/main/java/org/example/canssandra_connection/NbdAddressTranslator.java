package org.example.canssandra_connection;

import com.datastax.oss.driver.api.core.addresstranslation.AddressTranslator;
import com.datastax.oss.driver.api.core.context.DriverContext;

import java.net.InetSocketAddress;

public class NbdAddressTranslator implements AddressTranslator {
    public NbdAddressTranslator(DriverContext dctx){
    }
    public InetSocketAddress translate(InetSocketAddress address){
        String hostAddress = address.getAddress().getHostAddress();
        String hostName = address.getHostName();
        return switch (hostAddress){
            case "172.24.0.2" -> new InetSocketAddress("cassandra1",9042);
            case "172.24.0.3" -> new InetSocketAddress("cassandra2",9043);
            default -> throw new RuntimeException("Wrong address");
        };
    }
    @Override
    public void close(){}
}