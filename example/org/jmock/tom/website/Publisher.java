package org.jmock.tom.website;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Subscriber> subscribers = new ArrayList<Subscriber>();

    public void add(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void publish(String message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.receive(message);
        }
    }

    public int publish2(int a, int b) throws Exception
    {
        int sum = 0;
        try {

            for (Subscriber subscriber: subscribers) {
                sum += subscriber.add(a, b);
                System.out.println(sum);
                sum = a + b;
            }
        }
        catch (Exception e){
            throw e;
        }
        return sum;
    }
}
