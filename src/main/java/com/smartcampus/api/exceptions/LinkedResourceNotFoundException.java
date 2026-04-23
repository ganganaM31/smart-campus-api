package com.smartcampus.api.exceptions;

public class LinkedResourceNotFoundException extends RuntimeException {
    private String resourceId;
    private String resourceType;
    
    public LinkedResourceNotFoundException(String resourceType, String resourceId) {
        super(resourceType + " with ID " + resourceId + " not found");
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    public String getResourceId() {
        return resourceId;
    }
    
    public String getResourceType() {
        return resourceType;
    }
}