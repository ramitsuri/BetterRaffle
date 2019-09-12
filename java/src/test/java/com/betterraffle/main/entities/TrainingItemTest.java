package com.betterraffle.main.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainingItemTest {
    List<TrainingItem> items;

    @Before
    public void setUp() throws Exception {
        items = new ArrayList<>();

        TrainingItem item = new TrainingItem();
        item.setImageName("1002/2");
        items.add(item);

        item = new TrainingItem();
        item.setImageName("1002/1");
        items.add(item);

        item = new TrainingItem();
        item.setImageName("1001/3");
        items.add(item);

        item = new TrainingItem();
        item.setImageName("1001/2");
        items.add(item);

        item = new TrainingItem();
        item.setImageName("1001/1");
        items.add(item);

        item = new TrainingItem();
        item.setImageName("1002/3");
        items.add(item);
    }

    @Test
    public void testSorting() {
        System.out.println(items);
        Collections.sort(items);
        System.out.println(items);
    }
}