/*
 * Copyright Notice ====================================================
 * This file contains proprietary information of Hewlett-Packard Co.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2012 All rights reserved. =============================
 */

package org.jmock.tom.website;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class MockTest extends TestCase
{
    Mockery context = new Mockery();

    public void testSampleMock()
    {
        final Subscriber subscriber = context.mock(Subscriber.class);
        Publisher publisher = new Publisher();
        publisher.add(subscriber);
        context.checking(new Expectations()
        {
            {
                //                exactly(3).of(subscriber).add(2, 3);
                //                will(returnValue(1));
                //                oneOf(subscriber).add(1, 2);
                //                will(returnValue(3));
                //                oneOf(subscriber).add(2, 3);
                //                will(returnValue(4));

                oneOf(subscriber).add(2, 3);
                //will(returnValue(4));
                //                will(throwException(new Exception()));
            }
        });

        //        try {
        //            System.out.println(publisher.publish2(1, 1));
        //            fail("error");
        //        }
        //        catch (Exception e) {
        //            Assert.assertTrue(e != null);
        //        }
        try {
            System.out.println(publisher.publish2(2, 3));
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //        System.out.println(publisher.publish2(2, 3));
        //        System.out.println(publisher.publish2(2, 3));
        //        System.out.println(publisher.publish2(2, 3));
        //        System.out.println(publisher.publish2(2, 3));
        context.assertIsSatisfied();
    }

    /*public void testSampleMock2()
    {
        final Subscriber subscriber = context.mock(Subscriber.class);
        Publisher publisher = new Publisher();
        publisher.add(subscriber);
        context.checking(new Expectations()
        {
            {
                //one   The invocation is expected once and once only.
                //exactly(n).of   The invocation is expected exactly n times. Note: one is a convenient shorthand for exactly(1).
                //atLeast(n).of   The invocation is expected at least n times.
                //atMost(n).of    The invocation is expected at most n times.
                //between(min, max).of    The invocation is expected at least min times and at most max times.
                //allowing    The invocation is allowed any number of times but does not have to happen.
                //ignoring    The same as allowing. Allowing or ignoring should be chosen to make the test code clearly express intent.
                //never   The invocation is not expected at all. This is used to make tests more explicit and so easier to understand.
                atLeast(1).of(subscriber).add(2, 3);
                will(onConsecutiveCalls(returnValue(3), returnValue(4), returnValue(5)));
            }
        });

        System.out.println(publisher.publish2(2, 3));
        System.out.println(publisher.publish2(2, 3));
        System.out.println(publisher.publish2(2, 3));
        context.assertIsSatisfied();
    }

    public void testExceptionMock()
    {
        final Subscriber subscriber = context.mock(Subscriber.class);
        final Publisher publisher = new Publisher();
        publisher.add(subscriber);
        context.checking(new Expectations()
        {
            {
                oneOf(subscriber).add(2, 3);
                will(throwException(new IllegalStateException("test from Tom")));
            }
        });
        try
        {
        System.out.println(publisher.publish2(2, 3));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        context.assertIsSatisfied();
    }

    public void testMatchParameterMock()
    {
        final Subscriber subscriber = context.mock(Subscriber.class);
        Publisher publisher = new Publisher();
        publisher.add(subscriber);
        context.checking(new Expectations()
        {
            {
                allowing(subscriber).add(2, 3);
            }
        });
    }*/
}
