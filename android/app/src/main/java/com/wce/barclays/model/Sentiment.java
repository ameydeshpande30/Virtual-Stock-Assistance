package com.wce.barclays.model;

import java.util.HashMap;
import java.util.Map;

public class Sentiment {

    private String polarity;
    private Double confidence;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}