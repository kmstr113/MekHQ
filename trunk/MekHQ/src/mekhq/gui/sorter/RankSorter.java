package mekhq.gui.sorter;

import java.util.Comparator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mekhq.campaign.Campaign;
import mekhq.campaign.personnel.Person;
import mekhq.campaign.personnel.Ranks;

 /**
     * A comparator for ranks written as strings with "-" sorted to the bottom always
     * @author Jay Lawson
     *
     */
    public class RankSorter implements Comparator<String> {

        private Ranks ranks;
        private Campaign campaign;
        private Pattern pattern;
        private Matcher matcher;
        
        public RankSorter(Campaign c) {
            ranks = c.getRanks();
            campaign = c;
        }
        
        @Override
        public int compare(String s0, String s1) {
    	    // get the numbers associated with each rank string, and compare
        	pattern = Pattern.compile("id=\"([^\"]+)\"");
        	matcher = pattern.matcher(s0);
        	matcher.find();
        	Person p0 = campaign.getPerson(UUID.fromString(matcher.group(1)));
        	matcher = pattern.matcher(s1);
        	matcher.find();
        	Person p1 = campaign.getPerson(UUID.fromString(matcher.group(1)));
        	if (p0.getRankOrder() == p1.getRankOrder()) {
        		// the rank orders match, try comparing the levels
            	return ((Comparable<Integer>)p0.getRankLevel()).compareTo(p1.getRankLevel());
        	}
            return ((Comparable<Integer>)p0.getRankOrder()).compareTo(p1.getRankOrder());
        }
    }