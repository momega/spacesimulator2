package com.momega.spacesimulator.web.controller;

import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.utils.TimeUtils;
import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

/**
 * Created by martin on 12/29/15.
 */
@Component
public class SimulationTransformer {

    @Autowired
    private FieldsService fieldsService;

    private static final Logger logger = LoggerFactory.getLogger(SimulationTransformer.class);

    public SimulationDto transform(Simulation<?, ?> simulation) {
        SimulationDto dto = new SimulationDto();
        dto.setName(simulation.getName());
        dto.setCompletedInputs(simulation.getCompletedInputs());
        dto.setFinishedAt(simulation.getFinishedAt());
        dto.setStartedAt(simulation.getStartedAt());
        dto.setSimulationState(simulation.getSimulationState());
        dto.setUuid(simulation.getUuid());
        Object fields = simulation.getFields();
        Map<String, PropertyDescriptor> propertyDescriptorMap = fieldsService.getPropertyDescriptors(fields.getClass());
        for (Map.Entry<String, PropertyDescriptor> entry : propertyDescriptorMap.entrySet()) {
            FieldValueDto fieldValueDto = getFieldValue(simulation.getFields(), entry.getValue());
            dto.getFieldValues().add(fieldValueDto);
        }
        return dto;
    }

    public Object updateFields(Object fieldsInstance, List<FieldValueDto> fieldValues) {
        try {
            Map<String, PropertyDescriptor> propertyDescriptorMap = fieldsService.getPropertyDescriptors(fieldsInstance.getClass());
            for(FieldValueDto fieldValue: fieldValues) {
                String fieldName = fieldValue.getName();
                PropertyDescriptor pd = propertyDescriptorMap.get(fieldName);
                Object o = getFieldValue(fieldValue);
                pd.getWriteMethod().invoke(fieldsInstance, o);
            }
            return fieldsInstance;
        } catch (Exception e) {
            throw new IllegalStateException("unable to create parameters instance ", e);
        }
    }

    public FieldType getFieldType(String type) {
        if (type.equals("double")) {
            return FieldType.DOUBLE;
        } else if (type.equals("com.momega.spacesimulator.model.Timestamp")) {
            return FieldType.TIMESTAMP;
        } else if (type.equals("int")) {
            return FieldType.INT;
        }
        throw new IllegalArgumentException("unknown type " + type);
    }

    public Object getFieldValue(FieldValueDto dto) {
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

    public FieldValueDto getFieldValue(Object object, PropertyDescriptor pd) {
        FieldType fieldType = getFieldType(pd.getPropertyType().getName());
        try {
            FieldValueDto result = new FieldValueDto();
            result.setType(fieldType);
            result.setName(pd.getName());
            Object value = pd.getReadMethod().invoke(object);
            if (value != null) {
                switch (fieldType) {
                    case DOUBLE:
                        result.setValue(value.toString());
                        break;
                    case INT:
                        result.setValue(value.toString());
                        break;
                    case TIMESTAMP:
                        result.setValue(TimeUtils.timeAsString((Timestamp) value));
                        break;
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
        for(Map.Entry<String, String> entry : fieldsService.getFieldsDefinition(def.getParametersClass()).entrySet()) {
            FieldDto fieldDto = new FieldDto();
            fieldDto.setName(entry.getKey());
            fieldDto.setType(getFieldType(entry.getValue()));
            dto.getFields().add(fieldDto);
        }
        return dto;
    }


}
