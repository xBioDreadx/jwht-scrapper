package fr.whimtrip.ext.jwhtscrapper.impl;

import fr.whimtrip.ext.jwhtscrapper.enm.ProxyStatus;
import fr.whimtrip.ext.jwhtscrapper.intfr.Proxy;


/**
 *
 * <p>Part of project jwht-scrapper</p>
 * <p>Created on 26/07/18</p>
 *
 * <p>
 *     Default POJO-like implementation of {@link Proxy}.
 * </p>
 *
 * @author Louis-wht
 * @since 1.0.0
 */
public class BasicProxy implements Proxy {

    private ProxyStatus status = ProxyStatus.WORKING;

    private String ipAdress;

    private int port;

    /**
     * <p>Public default Proxy implentation</p>
     * @param ipAdress the ip adress of the proxy server.
     * @param port the port to use to connect to the proxy server.
     */
    public BasicProxy(String ipAdress, int port) {
        this.ipAdress = ipAdress;
        this.port = port;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public ProxyStatus getStatus() {

        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatus(ProxyStatus status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIpAdress() {
        return ipAdress;
    }

    /**
     *
     * @param ipAdress the ip adress to set.
     */
    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set.
     */
    public void setPort(int port) {
        this.port = port;
    }


}
