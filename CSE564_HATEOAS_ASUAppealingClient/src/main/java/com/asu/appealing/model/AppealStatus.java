package com.asu.appealing.model;

import javax.xml.bind.annotation.XmlEnumValue;


public enum AppealStatus {
    @XmlEnumValue(value="draft")
    DRAFT,
    @XmlEnumValue(value="submitted")
    SUBMITTED,
    @XmlEnumValue(value="followup")
    FOLLOWUP,
    @XmlEnumValue(value="approved")
    APPROVED, 
    @XmlEnumValue(value="rejected")
    REJECTED
}
