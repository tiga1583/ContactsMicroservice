package com.tilomicroservice.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UtilitiesService {


    private static final String[] states = {"AK", "AL","AR","AS","AZ","CA","CO","CT","DC","DE","FL","GA","GU","HI","IA","ID","IL","IN","KS","KY","LA","MA","MD","ME","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV","NY","OH","OK","OR","PA","PR","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA","WI","WV","WY"};
    private static final Set<String> statesSet = new HashSet<>(Arrays.asList(states));

    public boolean isValidStateAbbrev(String state) {
        return statesSet.contains(state);
    }


}
