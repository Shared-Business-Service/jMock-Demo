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
    //1 ���ȣ����ǽ���һ��test�����Ķ���  
    Mockery context = new Mockery();
    final IAddressService addressServcie = context.mock(IAddressService.class);
    @Test
    public void testFindAddress() {
        //mock��ķ�ʽ��
        context.setImposteriser(ClassImposteriser.INSTANCE);

        //2 �����mockery context������һ��mock������mock AddressService

        //3 ����UserManager��������addressService������findAddress��
        UserManager manager = new UserManager();
        manager.setAddressService(addressServcie);
        final String name = "allen";

        //final Sequence sequenceName = context.sequence("sequence-name");

        // ����������   
        context.checking(new Expectations() {
            {
                //4 ���������mock AddressService��findAddressӦ�ñ�����1�Σ����Ҳ���Ϊ"allen"��  
                oneOf(addressServcie).findAddress(name);
                will(returnValue(prepareAddress()));
            }
        });
        Address result = manager.findAddress("allen");
        //5 ��֤���������㡣
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

        // ����������   
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
        // ����������   
        context.checking(new Expectations() {
            {
                // ������Ϊ"allen"��ʱ��addressServcie�����findAddress��������һ��Adress����   
                allowing(addressServcie).findAddress("allen");
                will(returnValue(new Address("Shanghai")));

                // ������Ϊ"dandan"��ʱ��addressServcie�����findAddress��������һ��Adress����   
                allowing(addressServcie).findAddress(with(equal("dandan")));
                will(returnValue(new Address("Hangzhou")));

                // ������Ϊ�����κ�ֵ��ʱ��addressServcie�����findAddress��������һ��Adress����   
                allowing(addressServcie).findAddress(with(any(String.class)));
                will(returnValue(new Address("Shanxi")));
            }
        });
        Address result = manager.findAddress("allen");
        Assert.assertEquals(new Address("Shanxi").getName(), result.getName());
    }

    protected void sequenceTest() {

        final Sequence sequence = context.sequence("mySeq_01");

        // ����������   
        context.checking(new Expectations() {
            {
                // ������Ϊ"allen"��ʱ��addressServcie�����findAddress��������һ��Adress����   
                oneOf(addressServcie).findAddress("allen");
                inSequence(sequence);
                will(returnValue(new Address("Shanghai")));

                // ������Ϊ"dandan"��ʱ��addressServcie�����findAddress��������һ��Adress����   
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
        // ����������   
        context.checking(new Expectations() {
            {
                // ״̬Ϊs1��������allen��ʱ�򷵻�Shanghai
                allowing(addressServcie)
                    .findAddress(with(StringContains.containsString("allen")));
                when(states.is("s1"));
                will(returnValue(new Address("Shanghai")));

                // ״̬Ϊs1��������dandan��ʱ�򷵻�Hangzhou����ת��s2��   
                allowing(addressServcie)
                    .findAddress(with(StringContains.containsString("dandan")));
                when(states.is("s1"));
                will(returnValue(new Address("Hang zhou")));
                then(states.is("s2"));

                // ״̬Ϊs2��������allen��ʱ�򷵻�Shanxi 
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
        // s1״̬   
        ///assertFindAddress("allen", Result.Xian);   
        //assertFindAddress("allen0", Result.Xian);   

        // ״̬��ת�� s2   
        //assertFindAddress("dandan", Result.HangZhou);   

        // s2״̬   
        //assertFindAddress("allen", Result.ShangHai);  
    }
}
