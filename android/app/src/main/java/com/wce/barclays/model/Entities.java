package com.wce.barclays.model;

//public class Entities {
//
//
//    private List<Mention> mentions = null;
//    private OverallSentiment overallSentiment;
//    private String type;
//    private List<Link> links = null;
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
//
//    public List<Mention> getMentions() {
//        return mentions;
//    }
//
//    public void setMentions(List<Mention> mentions) {
//        this.mentions = mentions;
//    }
//
//    public OverallSentiment getOverallSentiment() {
//        return overallSentiment;
//    }
//
//    public void setOverallSentiment(OverallSentiment overallSentiment) {
//        this.overallSentiment = overallSentiment;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public List<Link> getLinks() {
//        return links;
//    }
//
//    public void setLinks(List<Link> links) {
//        this.links = links;
//    }
//
//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }
//}

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entities
{

    private List<String> location = null;
    private List<String> keyword = null;
    private List<String> organization = null;
    private List<String> person = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }

    public List<String> getOrganization() {
        return organization;
    }

    public void setOrganization(List<String> organization) {
        this.organization = organization;
    }

    public List<String> getPerson() {
        return person;
    }

    public void setPerson(List<String> person) {
        this.person = person;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}