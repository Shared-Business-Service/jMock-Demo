/*
 * Copyright Notice ====================================================
 * This file contains proprietary information of Hewlett-Packard Co.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2012 All rights reserved. =============================
 */

package org.jmock.tom.demo;

import org.hamcrest.core.StringContains;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Test;

public class UserManagerTest {
    //1 首先，我们建立一个test上下文对象  
    Mockery context = new Mockery();
    final IAddressService addressServcie = context.mock(IAddressService.class);
    @Test
    public void testFindAddress() {
        //mock类的方式。
        context.setImposteriser(ClassImposteriser.INSTANCE);

        //2 用这个mockery context建立了一个mock对象来mock AddressService

        //3 生成UserManager对象，设置addressService，调用findAddress。
        UserManager manager = new UserManager();
        manager.setAddressService(addressServcie);
        final String name = "allen";

        //final Sequence sequenceName = context.sequence("sequence-name");

        // 设置期望。   
        context.checking(new Expectations() {
            {
                //4 设置了这个mock AddressService的findAddress应该被调用1次，并且参数为"allen"。  
                oneOf(addressServcie).findAddress(name);
                will(returnValue(prepareAddress()));
            }
        });
        Address result = manager.findAddress("allen");
        //5 验证期望被满足。
        Assert
            .assertEquals(new Address("Shanghai").getName(), result.getName());
    }

    private Address prepareAddress() {
        return new Address("Shanghai");
    }

    @Test
    public void doAllTest() {
        UserManager manager = new UserManager();
        manager.setAddressService(addressServcie);

        // 设置期望。   
        context.checking(new Expectations() {
            {
                // doAllAction   
                allowing(addressServcie).findAddress("allen");
                will(doAll(returnValue(new Address("Shanghai")),
                           returnValue(new Address("Shan xi"))));

                // ActionSequence   
                //                allowing(addressServcie).findAddress("dandan");
                //                will(onConsecutiveCalls(returnValue(new Address("Shanghai")),
                //                                        returnValue(new Address("Hangzhou"))));
            }
        });

        Address result = manager.findAddress("allen");
        Assert.assertEquals(new Address("Shan xi").getName(), result.getName());
    }

    @Test
    public void augTest() {
        UserManager manager = new UserManager();
        manager.setAddressService(addressServcie);
        // 设置期望。   
        context.checking(new Expectations() {
            {
                // 当参数为"allen"的时候，addressServcie对象的findAddress方法返回一个Adress对象。   
                allowing(addressServcie).findAddress("allen");
                will(returnValue(new Address("Shanghai")));

                // 当参数为"dandan"的时候，addressServcie对象的findAddress方法返回一个Adress对象。   
                allowing(addressServcie).findAddress(with(equal("dandan")));
                will(returnValue(new Address("Hangzhou")));

                // 当参数为其他任何值的时候，addressServcie对象的findAddress方法返回一个Adress对象。   
                allowing(addressServcie).findAddress(with(any(String.class)));
                will(returnValue(new Address("Shanxi")));
            }
        });
        Address result = manager.findAddress("allen");
        Assert.assertEquals(new Address("Shanxi").getName(), result.getName());
    }

    protected void sequenceTest() {

        final Sequence sequence = context.sequence("mySeq_01");

        // 设置期望。   
        context.checking(new Expectations() {
            {
                // 当参数为"allen"的时候，addressServcie对象的findAddress方法返回一个Adress对象。   
                oneOf(addressServcie).findAddress("allen");
                inSequence(sequence);
                will(returnValue(new Address("Shanghai")));

                // 当参数为"dandan"的时候，addressServcie对象的findAddress方法返回一个Adress对象。   
                oneOf(addressServcie).findAddress("dandan");
                inSequence(sequence);
                will(returnValue(new Address("Hang zhou")));

            }
        });

        //assertFindAddress("allen", new Address("Shanghai"));
        //only invoking "dandan", will throw exception
        //assertFindAddress("dandan", new Address("Hang zhou"));
    }

    @Test
    public void statusTest() {

        final States states = context.states("sm").startsAs("s1");
        UserManager manager = new UserManager();
        manager.setAddressService(addressServcie);
        // 设置期望。   
        context.checking(new Expectations() {
            {
                // 状态为s1参数包含allen的时候返回Shanghai
                allowing(addressServcie)
                    .findAddress(with(StringContains.containsString("allen")));
                when(states.is("s1"));
                will(returnValue(new Address("Shanghai")));

                // 状态为s1参数包含dandan的时候返回Hangzhou，跳转到s2。   
                allowing(addressServcie)
                    .findAddress(with(StringContains.containsString("dandan")));
                when(states.is("s1"));
                will(returnValue(new Address("Hang zhou")));
                then(states.is("s2"));

                // 状态为s2参数包含allen的时候返回Shanxi 
                allowing(addressServcie)
                    .findAddress(with(StringContains.containsString("allen")));
                when(states.is("s2"));
                will(returnValue(new Address("Shan xi")));
            }
        });

        Address result = manager.findAddress("allen");
        Assert
            .assertEquals(new Address("Shanghai").getName(), result.getName());

        Address result2 = manager.findAddress("dandan");
        Assert.assertEquals(new Address("Hang zhou").getName(),
                            result2.getName());

        Address result3 = manager.findAddress("allen");
        Assert
            .assertEquals(new Address("Shan xi").getName(), result3.getName());
        // s1状态   
        ///assertFindAddress("allen", Result.Xian);   
        //assertFindAddress("allen0", Result.Xian);   

        // 状态跳转到 s2   
        //assertFindAddress("dandan", Result.HangZhou);   

        // s2状态   
        //assertFindAddress("allen", Result.ShangHai);  
    }
}
