package space.nebulark.junisockets.operations;

import org.junit.Assert;
import org.junit.Test;

public class CandidateTest {
   
    @Test
    public void testGetAsJSON() {
        
        Candidate candidate = new Candidate("127.0.0.1", "127.0.0.2", "c1");

        Assert.assertEquals("{\"data\":{\"offererId\":\"127.0.0.1\",\"answererId\":\"127.0.0.2\",\"candidate\":\"c1\"},\"opcode\":\"candidate\"}", candidate.getAsJSON(candidate));
    }

}