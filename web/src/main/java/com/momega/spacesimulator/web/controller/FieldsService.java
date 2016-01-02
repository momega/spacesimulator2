package com.momega.spacesimulator.web.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martin on 1/1/16.
 */
@Component
public class FieldsService {

    public Map<String, PropertyDescriptor> getPropertyDescriptors(Class<?> parametersClass) {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(parametersClass);
        Map<String, PropertyDescriptor> result = new HashMap<>();
        for(PropertyDescriptor d : descriptors) {
            if (!d.getName().equals("class")) {
                result.put(d.getName(), d);
            }
        }
        return result;
    }

    public Map<String, String> getFieldsDefinition(Class<?> parametersClass) {
        Map<String, PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(parametersClass);
        Map<String, String> result = new HashMap<>();
        for(Map.Entry<String, PropertyDescriptor> entry : propertyDescriptors.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getPropertyType().getName());
        }
        return result;
    }

}
