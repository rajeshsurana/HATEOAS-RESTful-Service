package com.asu.appealing.activities;

import com.asu.appealing.representations.AsuUri;

public class UriExchange {

    public static AsuUri gradedItemForAppeal(AsuUri appealUri) {
        checkForValidAppealUri(appealUri);
        return new AsuUri(appealUri.getBaseUri() + "/gradedItem/" + appealUri.getId());
    }
    
    public static AsuUri appealForGradedItem(AsuUri gradedItemUri) {
        checkForValidGradedItemUri(gradedItemUri);
        return new AsuUri(gradedItemUri.getBaseUri() + "/appeal/" + gradedItemUri.getId());
    }

    private static void checkForValidAppealUri(AsuUri appealUri) {
        if(!appealUri.toString().contains("/appeal/")) {
            throw new RuntimeException("Invalid Appeal URI");
        }
    }
    
    private static void checkForValidGradedItemUri(AsuUri gradedItem) {
        if(!gradedItem.toString().contains("/gradedItem/")) {
            throw new RuntimeException("Invalid Graded Item URI");
        }
    }
}
