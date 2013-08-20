/*
 * Copyright Notice ====================================================
 * This file contains proprietary information of Hewlett-Packard Co.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2012 All rights reserved. =============================
 */

package org.jmock.tom.demo;

import java.util.Iterator;


public interface IAddressService {
    public Address findAddress(String userName);

    public Iterator<Address> findAddresses(String userName);
}
