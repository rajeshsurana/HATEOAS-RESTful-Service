package com.asu.appealing.repositories;

import com.asu.appealing.model.Appeal;
import java.util.HashMap;
import java.util.Map.Entry;


public class AppealRepository {

    private static final AppealRepository theRepository = new AppealRepository();
    private HashMap<String, Appeal> backingStore = new HashMap<String, Appeal>(); // Default implementation, not suitable for production!

    public static AppealRepository current() {
        return theRepository;
    }
    
    private AppealRepository(){}
    
    public Appeal get(String identifier) {
        return backingStore.get(identifier);
     }
    
    public Appeal take(String identifier) {
        Appeal appeal = backingStore.get(identifier);
        remove(identifier);
        return appeal;
    }

    public String store(Appeal appeal) {
        String id = String.valueOf(appeal.getStudentId()) + String.valueOf(appeal.getAssignmentId());
        backingStore.put(id, appeal);
        return id;
    }
    
    public void store(String appealIdentifier, Appeal appeal) {
        backingStore.put(appealIdentifier, appeal);
    }

    public boolean has(String identifier) {
        boolean result =  backingStore.containsKey(identifier);
        return result;
    }

    public void remove(String identifier) {
        backingStore.remove(identifier);
    }
    
    public boolean appealPlaced(String identifier) {
        return AppealRepository.current().has(identifier);
    }
    
    public boolean appealNotPlaced(String identifier) {
        return !appealPlaced(identifier);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, Appeal> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<String, Appeal>();
    }

    public int size() {
        return backingStore.size();
    }
}
