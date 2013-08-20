/*
 * Copyright Notice ====================================================
 * This file contains proprietary information of Hewlett-Packard Co.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2012 All rights reserved. =============================
 */

package org.jmock.tom.demo;

import java.util.Iterator;

public class UserManager {
    public IAddressService addressService;

    public Address findAddress(String userName) {
        return addressService.findAddress(userName);
    }

    public Iterator<Address> findAddresses(String userName) {
        return addressService.findAddresses(userName);
    }

    public final void setAddressService(IAddressService addressService) {
        this.addressService = addressService;
    }
}
