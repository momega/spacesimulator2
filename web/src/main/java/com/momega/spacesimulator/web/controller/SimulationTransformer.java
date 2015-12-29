package com.momega.spacesimulator.web.controller;

import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.utils.TimeUtils;
import com.momega.spacesimulator.simulation.SimulationDefinition;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by martin on 12/29/15.
 */
@Component
public class SimulationTransformer {

    protected FieldType getFieldType(String type) {
        if (type.equals("double")) {
            return FieldType.DOUBLE;
        } else if (type.equals("com.momega.spacesimulator.model.Timestamp")) {
            return FieldType.TIMESTAMP;
        } else if (type.equals("int")) {
            return FieldType.INT;
        }
        throw new IllegalArgumentException("unknown type " + type);
    }

    protected Object getFieldValue(FieldValueDto dto) {
        switch (dto.getType()) {
            case DOUBLE:
                return Double.valueOf(dto.getValue());
            case INT:
                return Integer.valueOf(dto.getValue());
            case TIMESTAMP:
                return TimeUtils.parseTimestamp(dto.getValue());
        }
        throw new IllegalArgumentException("unknown type");
    }

    protected FieldValueDto getFieldValue(Object object, PropertyDescriptor pd) {
        FieldType fieldType = getFieldType(pd.getPropertyType().getName());
        try {
            FieldValueDto result = new FieldValueDto();
            result.setType(fieldType);
            result.setName(pd.getName());
            Object value = pd.getReadMethod().invoke(object);
            if (value != null) {
                switch (fieldType) {
                    case DOUBLE:
                        result.setValue(((Double) value).toString());
                    case INT:
                        result.setValue(((Integer) value).toString());
                    case TIMESTAMP:
                        result.setValue(TimeUtils.timeAsString((Timestamp) value));
                }
            }
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("unable to read property", e);
        }
    }

    public DefinitionDto transform(SimulationDefinition def) {
        DefinitionDto dto = new DefinitionDto();
        dto.setName(def.getName());
        for(Map.Entry<String, String> entry : def.getParametersDefinition().entrySet()) {
            FieldDto parameterDto = new FieldDto();
            parameterDto.setName(entry.getKey());
            parameterDto.setType(getFieldType(entry.getValue()));
            dto.getFields().add(parameterDto);
        }
        return dto;
    }


}
