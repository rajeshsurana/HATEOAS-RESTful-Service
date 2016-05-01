package com.asu.appealing.representations;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Objects;

public class AsuUri {
    private URI uri;
    
    public AsuUri(String uri) {
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    
    public AsuUri(URI uri) {
        this(uri.toString());
    }

    public AsuUri(URI uri, int studentId, int assignmentId) {
        this(uri.toString() + "/" + String.valueOf(studentId) + String.valueOf(assignmentId));
    }

    public String getId() {
        String path = uri.getPath();
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }

    public URI getFullUri() {
        return uri;
    }
    
    @Override
    public String toString() {
        return uri.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.uri);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AsuUri) {
            return ((AsuUri)obj).uri.equals(uri);
        }
        return false;
    }

    public String getBaseUri() {
        String port = "";
        if(uri.getPort() != 80 && uri.getPort() != -1) {
            port = ":" + String.valueOf(uri.getPort());
        }
        
        return uri.getScheme() + "://" + uri.getHost() + port + "/CSE564_HATEAOS_ASUAppealingServer/webresources";
    }
}
