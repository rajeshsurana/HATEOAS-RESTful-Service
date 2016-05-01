package com.asu.appealing.repositories;

import com.asu.appealing.model.GradedItem;
import java.util.HashMap;
import java.util.Map.Entry;


public class GradedItemRepository {

    private static final GradedItemRepository theRepository = new GradedItemRepository();
    private HashMap<String, GradedItem> backingStore = new HashMap<String, GradedItem>(); // Default implementation, not suitable for production!

    public static GradedItemRepository current() {
        return theRepository;
    }
    
    private GradedItemRepository(){}
    
    public GradedItem get(String identifier) {
        return backingStore.get(identifier);
     }
    
    public GradedItem take(String identifier) {
        GradedItem gradedItem = backingStore.get(identifier);
        remove(identifier);
        return gradedItem;
    }

    public String store(GradedItem gradedItem) {
        String id = String.valueOf(gradedItem.getStudentId()) + String.valueOf(gradedItem.getAssignmentId());
        backingStore.put(id, gradedItem);
        return id;
    }
    
    public void store(String gradedItemIdentifier, GradedItem gradedItem) {
        backingStore.put(gradedItemIdentifier, gradedItem);
    }

    public boolean has(String identifier) {
        boolean result =  backingStore.containsKey(identifier);
        return result;
    }

    public void remove(String identifier) {
        backingStore.remove(identifier);
    }
    
    public boolean gradedItemPlaced(String identifier) {
        return GradedItemRepository.current().has(identifier);
    }
    
    public boolean gradedItemNotPlaced(String identifier) {
        return !gradedItemPlaced(identifier);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, GradedItem> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<String, GradedItem>();
    }

    public int size() {
        return backingStore.size();
    }
}
