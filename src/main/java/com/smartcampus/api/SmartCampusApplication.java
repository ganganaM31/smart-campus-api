package com.smartcampus.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import com.smartcampus.api.discovery.DiscoveryResource;
import com.smartcampus.api.resources.RoomResource;
import com.smartcampus.api.resources.SensorResource;
import com.smartcampus.api.mappers.*;
import com.smartcampus.api.filters.LoggingFilter;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        classes.add(DiscoveryResource.class);
        classes.add(RoomResource.class);
        classes.add(SensorResource.class);
        
        classes.add(RoomNotEmptyExceptionMapper.class);
        classes.add(LinkedResourceNotFoundExceptionMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(GenericExceptionMapper.class);
        
        classes.add(LoggingFilter.class);
        
        return classes;
    }
}