package com.wce.barclays.model;

//public class Word {
//
//    private String text;
//    private List<Entities> entities = null;
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public List<Entities> getEntities() {
//        return entities;
//    }
//
//    public void setEntities(List<Entities> entities) {
//        this.entities = entities;
//    }
//
//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }
//
//}

import java.util.HashMap;
import java.util.Map;

public class Word
        {
            private String text;
            private String language;
            private Entities entities;
            private Map<String, Object> additionalProperties = new HashMap<String, Object>();

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public Entities getEntities() {
                return entities;
            }

            public void setEntities(Entities entities) {
                this.entities = entities;
            }

            public Map<String, Object> getAdditionalProperties() {
                return this.additionalProperties;
            }

            public void setAdditionalProperty(String name, Object value) {
                this.additionalProperties.put(name, value);
            }

        }
